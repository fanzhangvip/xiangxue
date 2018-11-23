package com.enjoy.zero.libzhujie01;

import java.util.HashMap;
import java.util.Map;

@TestA(name = "class", gid = UserAnnotation.class)
// 类成员注解
public class UserAnnotation {
    @TestA(name = "param", id = 1, gid = Integer.class)
    // 类成员注解
    private Integer age;

    @TestA(name = "construct", id = 2, gid = Void.class)
    // 构造方法注解
    public UserAnnotation() {

    }

    @TestA(name = "public method", id = 3, gid = HashMap.class)
    // 类方法注解
    public void a() {
        Map<String, String> m = new HashMap<String, String>(0);
    }

    @TestA(name = "protected method", id = 4, gid = Map.class)
    // 类方法注解
    protected void b() {
        Map<String, String> m = new HashMap<String, String>(0);
    }

    @TestA(name = "private method", id = 5, gid = Long.class)
    // 类方法注解
    private void c() {
        Map<String, String> m = new HashMap<String, String>(0);
    }

    public void b(Integer a) {

    }
}
