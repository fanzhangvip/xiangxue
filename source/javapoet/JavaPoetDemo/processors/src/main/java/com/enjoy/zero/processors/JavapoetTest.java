package com.enjoy.zero.processors;


import com.enjoy.zero.bean.Anim;
import com.enjoy.zero.bean.Human;
import com.enjoy.zero.javapoet.AnnotationSpec;
import com.enjoy.zero.javapoet.ClassName;
import com.enjoy.zero.javapoet.CodeBlock;
import com.enjoy.zero.javapoet.FieldSpec;
import com.enjoy.zero.javapoet.JavaFile;
import com.enjoy.zero.javapoet.MethodSpec;
import com.enjoy.zero.javapoet.ParameterSpec;
import com.enjoy.zero.javapoet.ParameterizedTypeName;
import com.enjoy.zero.javapoet.TypeName;
import com.enjoy.zero.javapoet.TypeSpec;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

import javax.lang.model.element.Modifier;


/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明:
 */
public class JavapoetTest {

    public static void main(String[] args) throws Exception {

//        testHelloWorld();
//        testControlFlow();
//        StringFor$STest();
//        TypesFor$T();
//        testNamesFor$N();
        testAbstractMethods();
//        testConstructors();
//        testInnerClasses();
//        testInterfacess();

    }

    private static MethodSpec newMyMethod(int value) {
        MethodSpec.Builder method =
                MethodSpec.methodBuilder("test")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        ClassName arrayList = ClassName.get("android.util", "Log");

        method.addStatement("$T.i(\"123s\",\"helloworld:\" + $S)", arrayList,value);
//        method.addStatement("return $S",value);
        return method.build();
    }


    public static void testHelloWorld() {
        //第一步 生成main函数
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();
        //第二步 生成类
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();
        //第三步 生成java文件对象
        JavaFile javaFile = JavaFile.builder("com.zero.helloworld", helloWorld).build();
        //第四步 输出到控制台
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testControlFlow() {
        //对与无需类引入的极简代码可以直接使用addCode
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addCode(""
                        + "int total = 0;\n"
                        + "for (int i = 0; i < 10; i++){\n"
                        + " total += i;\n"
                        + "}\n")
                .build();

        MethodSpec main1 = MethodSpec.methodBuilder("main1")
                .addStatement("int total = 0;")
                .beginControlFlow("for (int i = 0; i < 10; i++)")//流开启
                .addStatement("total += i")//处理语句
                .endControlFlow()//流结束
                .build();

        MethodSpec display = MethodSpec.methodBuilder("display")
                .addStatement("$T human = new $T();", Anim.class, Human.class)
                .addStatement("human.name = \"fanzhang\"")
                .beginControlFlow("for (int i = 0; i < name.length; i++)")
                .addStatement("$T.out.println($S)", System.class, "name.charAt(i)")
                .endControlFlow()
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .addMethod(main1)
                .addMethod(computeRange$L("Literals$L", 0, 10, "+"))
                .addMethod(display)
                .build();
        JavaFile javaFile = JavaFile.builder("com.zero.controlflow", typeSpec).build();
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static MethodSpec computeRange(String name, int from, int to, String op) {
        return MethodSpec.methodBuilder(name)
                .returns(int.class)
                .addStatement("int result = 1;")
                .beginControlFlow("for (int i = " + from + "; i < " + to + "; i++" + ")")
                .addStatement("result = result" + op + "i")
                .endControlFlow()
                .addStatement("return result")
                .build();
    }

    //占位符  $L 字面常量（Literals）
    private static MethodSpec computeRange$L(String name, int from, int to, String op) {
        return MethodSpec.methodBuilder(name)
                .returns(int.class)
                .addStatement("int result = 0")
                .beginControlFlow("for (int i = $L; i < $L; i++)", from, to)
                .addStatement("result = result $L i", op)
                .endControlFlow()
                .addStatement("return result")
                .build();
    }

    public static void StringFor$STest() throws Exception {
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(whatsMyName("slimShady"))
                .addMethod(whatsMyName("eminem"))
                .addMethod(whatsMyName("marshallMathers"))
                .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                .build();

        javaFile.writeTo(System.out);
    }

    //$S 字符串常量（String）
    private static MethodSpec whatsMyName(String name) {
        return MethodSpec.methodBuilder(name)
                .returns(String.class)
                .addStatement("return $S", name)
                .build();
    }


    //$T 类型(Types) 最大的特点是自动导入包，
    public static void TypesFor$T() throws IOException {
        MethodSpec today = MethodSpec.methodBuilder("today")
                .returns(Date.class)
                .addStatement("return new $T()", Date.class)
                .build();

        //想要导入自己写的类怎么办？
        //通过ClassName.get（”类的路径”，”类名“），结合$T可以生成
        ClassName hoverboard = ClassName.get("com.mattel", "Hoverboard");
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        //添加泛型
        TypeName listOfHoverboards = ParameterizedTypeName.get(list, hoverboard);

        MethodSpec beyond = MethodSpec.methodBuilder("beyond")
                .returns(listOfHoverboards)
                .addStatement("$T result = new $T<>()", listOfHoverboards, arrayList)
                .addStatement("result.add(new $T())", hoverboard)
                .addStatement("result.add(new $T())", hoverboard)
                .addStatement("result.add(new $T())", hoverboard)
                .addStatement("return result")
                .build();

        ClassName namedBoards = ClassName.get("com.mattel", "Hoverboard", "Boards");

        MethodSpec beyond1 = MethodSpec.methodBuilder("beyond1")
                .returns(listOfHoverboards)
                .addStatement("$T result = new $T<>()", listOfHoverboards, arrayList)
                .addStatement("result.add($T.createNimbus(2000))", hoverboard)
                .addStatement("result.add($T.createNimbus(\"2001\"))", hoverboard)
                .addStatement("result.add($T.createNimbus($T.THUNDERBOLT))", hoverboard, namedBoards)
                .addStatement("$T.sort(result)", Collections.class)
                .addStatement("return result.isEmpty() ? $T.emptyList() : result", Collections.class)
                .build();


        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(today)
                .addMethod(beyond)
                .addMethod(beyond1)
                .build();

        //支持import static,通过addStaticImport
        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                .addStaticImport(hoverboard, "createNimbus")
                .addStaticImport(namedBoards, "*")
                .addStaticImport(Collections.class, "*")
                .build();

        javaFile.writeTo(System.out);
    }

    //$N 命名(Names)  通常指我们自己生成的方法名或者变量名等等比如这样的代码块
    public static void testNamesFor$N() {
        MethodSpec hexDigit = MethodSpec.methodBuilder("hexDigit")
                .addParameter(int.class, "i")
                .returns(char.class)
                .addStatement("return (char) (i < 10 ? i + '0' : i - 10 + 'a')")
                .build();


        Map<String, Object> map = new LinkedHashMap<>();
        map.put("food", "tacos");
        map.put("count", 3);

        MethodSpec byteToHex = MethodSpec.methodBuilder("byteToHex")
                .addParameter(int.class, "b")
                .returns(String.class)
                .addStatement("char[] result = new char[2]")
                .addStatement("result[0] = $N((b >>> 4) & 0xf)", hexDigit)
                .addStatement("result[1] = $N(b & 0xf)", hexDigit)
                .addStatement("return new String(result)")
                .addStatement("$T.out.println($S)", System.class, CodeBlock.builder().add("I ate $L $L", 3, "tacos").build())
                .addStatement("$T.out.println($S)", System.class, CodeBlock.builder().add("I ate $2L $1L", "tacos", 3).build())
                .addStatement(CodeBlock.builder().addNamed("//I ate $count:L $food:L", map).build())
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(hexDigit)
                .addMethod(byteToHex)
                .build();
        JavaFile javaFile = JavaFile.builder("com.zero.controlflow", typeSpec).build();
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 方法的修饰，如Modifiers.ABSTRACT
    public static void testAbstractMethods() {
        MethodSpec flux = MethodSpec.methodBuilder("flux")
                .addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED)
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(flux)
                .build();
        JavaFile javaFile = JavaFile.builder("com.zero.controlflow", helloWorld).build();
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 构造器
    public static void testConstructors() {
        MethodSpec flux = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "greeting")
                .addStatement("this.$N = $N", "greeting", "greeting")
                .build();

        //ParameterSpec，我们可以使用ParameterSpec.builder()来生成参数
        ParameterSpec android = ParameterSpec.builder(String.class, "android")
                .addModifiers(Modifier.FINAL)
                .build();

        MethodSpec welcomeOverlords = MethodSpec.methodBuilder("welcomeOverlords")
                .addParameter(android)
                .addParameter(String.class, "robot", Modifier.FINAL)
                .build();

        //可以使用FieldSpec去声明字段，然后加到Method中处理
        FieldSpec androidfield = FieldSpec.builder(String.class, "android")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();

        //初始化成员变量
        FieldSpec testinit = FieldSpec.builder(String.class, "android1")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$S + $L", "Lollipop v.", 5.0d)
                .build();


        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC)
                .addField(String.class, "greeting", Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(flux)
                .addMethod(welcomeOverlords)
                .addField(androidfield)
                .addField(String.class, "robot", Modifier.PRIVATE, Modifier.FINAL)
                .addField(testinit)
                .build();
        JavaFile javaFile = JavaFile.builder("com.zero.controlflow", helloWorld).build();
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //接口 接口方法必须是PUBLIC ABSTRACT并且接口字段必须是PUBLIC STATIC FINAL ，使用TypeSpec.interfaceBuilder
    public static void testInterfacess() {
        TypeSpec helloWorld = TypeSpec.interfaceBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(String.class, "ONLY_THING_THAT_IS_CONSTANT")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$S", "change")
                        .build())
                .addMethod(MethodSpec.methodBuilder("beep")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .build())
                .build();
        JavaFile javaFile = JavaFile.builder("com.zero.controlflow", helloWorld).build();
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //枚举 使用TypeSpec.enumBuilder来创建，使用addEnumConstant来添加
    public static void testEnums() {
        TypeSpec helloWorld = TypeSpec.enumBuilder("Roshambo")
                .addModifiers(Modifier.PUBLIC)
                .addEnumConstant("ROCK")
                .addEnumConstant("SCISSORS")
                .addEnumConstant("PAPER")
                .build();
        JavaFile javaFile = JavaFile.builder("com.zero.controlflow", helloWorld).build();
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //可以支持，如重写、注解等
    public static void testEnums1() {
        TypeSpec helloWorld = TypeSpec.enumBuilder("Roshambo")
                .addModifiers(Modifier.PUBLIC)
                .addEnumConstant("ROCK", TypeSpec.anonymousClassBuilder("$S", "fist")
                        .addMethod(MethodSpec.methodBuilder("toString")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .addStatement("return $S", "avalanche!")
                                .returns(String.class)
                                .build())
                        .build())
                .addEnumConstant("SCISSORS", TypeSpec.anonymousClassBuilder("$S", "peace")
                        .build())
                .addEnumConstant("PAPER", TypeSpec.anonymousClassBuilder("$S", "flat")
                        .build())
                .addField(String.class, "handsign", Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(String.class, "handsign")
                        .addStatement("this.$N = $N", "handsign", "handsign")
                        .build())
                .build();
        JavaFile javaFile = JavaFile.builder("com.zero.controlflow", helloWorld).build();
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //匿名内部类 Type.anonymousInnerClass(""),通常可以使用$L占位符来指代
    public static void testInnerClasses() {
        TypeSpec comparator = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(Comparator.class, String.class))
                .addMethod(MethodSpec.methodBuilder("compare")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(String.class, "a")
                        .addParameter(String.class, "b")
                        .returns(int.class)
                        .addStatement("return $N.length() - $N.length()", "a", "b")
                        .build())
                .build();

        //注解
        MethodSpec toString = MethodSpec.methodBuilder("toString")
                .addAnnotation(Override.class)
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $S", "Hoverboard")
                .build();

        //AnnotationSpec.builder()  注解同样可以注解其他注解，通过$L引用如
        MethodSpec logRecord = MethodSpec.methodBuilder("recordEvent")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(AnnotationSpec.builder(Headers.class)
                        .addMember("accept", "$S", "application/json; charset=utf-8")
                        .addMember("userAgent", "$S", "Square Cash")
                        .build())
                .addParameter(LogRecord.class, "logRecord")
                .returns(LogReceipt.class)
                .build();

        MethodSpec dismiss = MethodSpec.methodBuilder("dismiss")
                .addJavadoc("Hides {@code human} from the caller's history. Other\n"
                        + "participants in the conversation will continue to see the\n"
                        + "message in their own history unless they also delete it.\n")
                .addJavadoc("\n")
                .addJavadoc("<p>Use {@link #delete($T)} to delete the entire\n"
                        + "conversation for all participants.\n", Human.class)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(Human.class, "human")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(MethodSpec.methodBuilder("sortByLength")
                        .addParameter(ParameterizedTypeName.get(List.class, String.class), "strings")
                        .addStatement("$T.sort($N, $L)", Collections.class, "strings", comparator)
                        .build())
                .addMethod(toString)
                .addMethod(logRecord)
                .addMethod(dismiss)
                .build();
        JavaFile javaFile = JavaFile.builder("com.zero.controlflow", helloWorld).build();
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
