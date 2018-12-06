package com.enjoy.zero.processors;


import com.enjoy.zero.bean.Human;
import com.enjoy.zero.javapoet.JavaFile;
import com.enjoy.zero.javapoet.MethodSpec;
import com.enjoy.zero.javapoet.TypeSpec;

import java.io.IOException;

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

    public static void main(String[] args) {

//        testHelloWorld();
        testControlFlow();
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
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addCode(""
                        + "int total = 0;\n"
                        + "for (int i = 0; i < 10; i++){\n"
                        + " total += i;\n"
                        + "}\n")
                .build();

        MethodSpec main1 = MethodSpec.methodBuilder("main1")
                .addStatement("int total = 0;")
                .beginControlFlow("for (int i = 0; i < 10; i++)")
                .addStatement("total += i")
                .endControlFlow()
                .build();

        MethodSpec display = MethodSpec.methodBuilder("display")
                .addStatement("Human human = new Human();",Human.class)
                .addStatement("human.name = \"fanzhang\"")
                .beginControlFlow("for (int i = 0; i < name.length; i++)")
                .addStatement("$T.out.println($S)", System.class, "name.charAt(i)")
                .endControlFlow()
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .addMethod(main1)
                .addMethod(display)
                .build();
        JavaFile javaFile = JavaFile.builder("com.zero.controlflow", typeSpec).build();
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MethodSpec computeRange(String name, int from, int to, String op) {
        return MethodSpec.methodBuilder(name)
                .returns(int.class)
                .addStatement("int result = 1;")
                .beginControlFlow("for (int i = " + from + "; i < " + to + "; i++" + ")")
                .addStatement("result = result" + op + "i")
                .endControlFlow()
                .addStatement("return result")
                .build();
    }


}
