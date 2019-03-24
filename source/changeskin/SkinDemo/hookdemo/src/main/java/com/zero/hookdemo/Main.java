package com.zero.hookdemo;

import java.lang.reflect.Field;

public class Main {

    public static void main(String... args) {

        Person wangmeili = new Person("王美丽", "小丽", 18);

        setFiled("com.zero.hookdemo.Main$Person","age",wangmeili,28);

        wangmeili.displayPerson();

    }



    static class Person {
        private String name;
        private String nickName;

        private int age;

        public Person() {
        }

        public Person(String name, String nickName, int age) {
            this.name = name;
            this.nickName = nickName;
            this.age = age;
        }


        public void displayPerson() {
            if (age > 25) {
                System.out.println("别人都叫我：" + name +",我在老家相夫教子");
            } else {
                System.out.println("别人都叫我：" + nickName + ",我在东莞打工挣钱");
            }

        }
    }

    public static boolean setFiled(String className,String filedName,Object target,Object value){
        Field field = getFiled(className,filedName);
        try {
            field.set(target,value);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Field getFiled(String className, String filedName) {
        Object o = null;
        try {
            Class<?> aClass = Class.forName(className);

            Field declaredField = aClass.getDeclaredField(filedName);
            //   if not public,you should call this
            declaredField.setAccessible(true);
            return declaredField;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
