package com.enjoy.zero.processors;

import com.enjoy.zero.annotations.BindView;
import com.enjoy.zero.javapoet.ClassName;
import com.enjoy.zero.javapoet.JavaFile;
import com.enjoy.zero.javapoet.MethodSpec;
import com.enjoy.zero.javapoet.ParameterizedTypeName;
import com.enjoy.zero.javapoet.TypeName;
import com.enjoy.zero.javapoet.TypeSpec;
import com.google.auto.service.AutoService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class MyViewBinderProcessor extends AbstractProcessor {


    static class AnnotatedClass {
        private static class TypeUtil {
            static final ClassName BINDER = ClassName.get("com.enjoy.zero.api", "ViewBinder");
            static final ClassName PROVIDER = ClassName.get("com.enjoy.zero.api", "ViewFinder");

        }

        private TypeElement mTypeElement;
        private ArrayList<BindViewField> mFields;
        private Elements mElements;

        AnnotatedClass(TypeElement typeElement, Elements elements) {
            mTypeElement = typeElement;
            mElements = elements;
            mFields = new ArrayList<>();
            System.out.println("mTypeElement: " + mTypeElement);
            System.out.println("mElements: " + mElements);
            System.out.println("mFields: " + mFields);
        }

        void addField(BindViewField field) {
            mFields.add(field);
            System.out.println("mFields: " + mFields);
        }

        JavaFile generateFile() {
            //generateMethod
            MethodSpec.Builder bindViewMethod = MethodSpec.methodBuilder("bindView")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(TypeName.get(mTypeElement.asType()), "host")
                    .addParameter(TypeName.OBJECT, "source")
                    .addParameter(TypeUtil.PROVIDER, "finder");

            for (BindViewField field : mFields) {
                // find views
                bindViewMethod.addStatement("host.$N = ($T)(finder.findView(source, $L))", field.getFieldName(), ClassName.get(field.getFieldType()), field.getResId());
            }

            MethodSpec.Builder unBindViewMethod = MethodSpec.methodBuilder("unBindView")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.get(mTypeElement.asType()), "host")
                    .addAnnotation(Override.class);
            for (BindViewField field : mFields) {
                unBindViewMethod.addStatement("host.$N = null", field.getFieldName());
            }

            //generaClass
            TypeSpec injectClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$ViewBinder")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ParameterizedTypeName.get(TypeUtil.BINDER, TypeName.get(mTypeElement.asType())))
                    .addMethod(bindViewMethod.build())
                    .addMethod(unBindViewMethod.build())
                    .build();

            String packageName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();
            System.out.println("generateFile packageName: " + packageName);
            JavaFile result = JavaFile.builder(packageName, injectClass).build();
            try {
                result.writeTo(System.out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    static class BindViewField {
        private VariableElement mVariableElement;
        private int mResId;

        BindViewField(Element element) throws IllegalArgumentException {
            if (element.getKind() != ElementKind.FIELD) {
                throw new IllegalArgumentException(String.format("Only fields can be annotated with @%s",
                        BindView.class.getSimpleName()));
            }
            mVariableElement = (VariableElement) element;
            System.out.println("BindViewField mVariableElement: " + mVariableElement);
            BindView bindView = mVariableElement.getAnnotation(BindView.class);
            mResId = bindView.value();
            System.out.println("BindViewField mResId: " + mResId);
            if (mResId < 0) {
                throw new IllegalArgumentException(
                        String.format("value() in %s for field %s is not valid !", BindView.class.getSimpleName(),
                                mVariableElement.getSimpleName()));
            }
        }

        /**
         * 获取变量名称
         *
         * @return
         */
        Name getFieldName() {
            return mVariableElement.getSimpleName();
        }

        /**
         * 获取变量id
         *
         * @return
         */
        int getResId() {
            return mResId;
        }

        /**
         * 获取变量类型
         *
         * @return
         */
        TypeMirror getFieldType() {
            return mVariableElement.asType();
        }
    }

    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类
    private Map<String, AnnotatedClass> mAnnotatedClassMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mAnnotatedClassMap = new TreeMap<>();
        System.out.println("init mFiler: " + mFiler);
        System.out.println("init mElementUtils: " + mElementUtils);
        System.out.println("init mMessager: " + mMessager);
        System.out.println("init mAnnotatedClassMap: " + mAnnotatedClassMap);
    }


    @Override
    public boolean process(Set annotations, RoundEnvironment roundEnv) {
        mAnnotatedClassMap.clear();
        try {
            processBindView(roundEnv);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            error(e.getMessage());
        }

        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                System.out.println("process ->  annotatedClass: " + annotatedClass);//id
                annotatedClass.generateFile().writeTo(mFiler);
            } catch (IOException e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }
        return true;
    }

    private void processBindView(RoundEnvironment roundEnv) throws IllegalArgumentException {

        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            BindViewField bindViewField = new BindViewField(element);
            annotatedClass.addField(bindViewField);
            System.out.println("processBindView annotatedClass: " + annotatedClass);
            System.out.println("processBindView bindViewField: " + bindViewField);
        }
    }

    private AnnotatedClass getAnnotatedClass(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String fullName = typeElement.getQualifiedName().toString();
        System.out.println("getAnnotatedClass typeElement: " + typeElement);
        System.out.println("getAnnotatedClass fullName: " + fullName);
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(typeElement, mElementUtils);
            mAnnotatedClassMap.put(fullName, annotatedClass);
        }
        return annotatedClass;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public Set getSupportedAnnotationTypes() {
        Set types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        return types;
    }
}
