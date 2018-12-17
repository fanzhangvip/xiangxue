package com.enjoy.zero.processors;

import com.enjoy.zero.annotations.MyAnnotation;
import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**方案1
 * 运行注解处理器
 * 1、在 processors 库的 main 目录下新建 resources 资源文件夹；
 * 2、在 resources文件夹下建立 META-INF/services 目录文件夹；
 * 3、在 META-INF/services 目录文件夹下创建 javax.annotation.processing.Processor 文件；
 * 4、在 javax.annotation.processing.Processor 文件写入注解处理器的全称，包括包路径；
 */


/** 方案2
 * 每一个注解处理器类都必须有一个空的构造函数，默认不写就行;
 */
@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {

    /**
     * Types是一个用来处理TypeMirror的工具
     */
    private Types typeUtils;
    /**
     * Elements是一个用来处理Element的工具
     */
    private Elements elementUtils;
    /**
     * 生成java源码
     */
    private Filer filer;
    /**
     * Messager提供给注解处理器一个报告错误、警告以及提示信息的途径。
     * 它不是注解处理器开发者的日志工具，
     * 而是用来写一些信息给使用此注解器的第三方开发者的
     */
    private Messager messager;

    /**
     * init()方法会被注解处理工具调用，并输入ProcessingEnviroment参数。
     * ProcessingEnviroment提供很多有用的工具类Elements, Types 和 Filer
     * @param processingEnv 提供给 processor 用来访问工具框架的环境
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    /**
     * 这相当于每个处理器的主函数main()，你在这里写你的扫描、评估和处理注解的代码，以及生成Java文件。
     * 输入参数RoundEnviroment，可以让你查询出包含特定注解的被注解元素
     * @param annotations   请求处理的注解类型
     * @param roundEnvironment  有关当前和以前的信息环境
     * @return  如果返回 true，则这些注解已声明并且不要求后续 Processor 处理它们；
     *          如果返回 false，则这些注解未声明并且可能要求后续 Processor 处理它们
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        // roundEnv.getElementsAnnotatedWith()返回使用给定注解类型的元素
        for (Element element : roundEnvironment.getElementsAnnotatedWith(MyAnnotation.class)) {
            System.out.println("------------------------------");
            // 判断元素的类型为Class
            if (element.getKind() == ElementKind.CLASS) {
                // 显示转换元素类型
                TypeElement typeElement = (TypeElement) element;
                // 输出元素名称
                System.out.println("Zero: " + typeElement.getSimpleName() + " : " + System.currentTimeMillis());
                // 输出注解属性值
                System.out.println("Zero: " +typeElement.getAnnotation(MyAnnotation.class).value());
            }
            System.out.println("------------------------------");
        }
        return true;
    }

    /**
     * 这里必须指定，这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称
     * @return  注解器所支持的注解类型集合，如果没有这样的类型，则返回一个空集合
     */
    @Override
    public Set getSupportedAnnotationTypes() {
        Set annotataions = new LinkedHashSet();
        annotataions.add(MyAnnotation.class.getCanonicalName());
        return annotataions;
    }

    /**
     * 指定使用的Java版本，通常这里返回SourceVersion.latestSupported()，默认返回SourceVersion.RELEASE_6
     * @return  使用的Java版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
