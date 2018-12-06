package com.enjoy.zero.processors;



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

        testHelloWorld();
    }

    public static void testHelloWorld() {
        //第一步 生成main函数
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class,"args")
                .addStatement("$T.out.println($S)",System.class,"Hello, JavaPoet!")
                .build();
        //第二步 生成类
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                .addMethod(main)
                .build();
        //第三步 生成java文件对象
        JavaFile javaFile = JavaFile.builder("com.zero.helloworld",helloWorld).build();
        //第四步 输出到控制台
        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
