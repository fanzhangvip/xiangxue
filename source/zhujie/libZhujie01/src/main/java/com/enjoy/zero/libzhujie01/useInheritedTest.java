package com.enjoy.zero.libzhujie01;

import java.lang.reflect.Method;

public class useInheritedTest {

    public static void main(String[] args) {
        classTest();
        methodTest();
    }

    public static void classTest(){
        //获取child类的class对象
        Class<ChildInherited> childInheritedClass = ChildInherited.class;

        if(childInheritedClass.isAnnotationPresent(InheritedTest.class)){//判断InheritedTest类是不是Child的父注解类
            InheritedTest inheritedTest = childInheritedClass.getAnnotation(InheritedTest.class);
            String value = inheritedTest.value();
            System.out.println("classTest value: " + value);
        }
    }

    public static void methodTest(){
        //获取child类的class对象
        Class<ChildInherited> childInheritedClass = ChildInherited.class;
        try {
            Method method = childInheritedClass.getMethod("doSomething",new Class[]{});
            if(method.isAnnotationPresent(InheritedTest.class)){//判断InheritedTest类是不是Child的父注解类
                InheritedTest inheritedTest = method.getAnnotation(InheritedTest.class);
                String value = inheritedTest.value();
                System.out.println("Method value: " + value);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
}
