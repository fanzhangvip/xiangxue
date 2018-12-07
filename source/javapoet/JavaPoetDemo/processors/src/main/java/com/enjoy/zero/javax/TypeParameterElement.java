package com.enjoy.zero.javax;

import java.util.List;

/**
 * 表示一般类、接口、方法或构造方法元素的泛型参数.
 */
public interface TypeParameterElement {
 
    /**
     * 返回由此类型参数参数化的一般类、接口、方法或构造方法
     */
    Element getGenericElement();
 
    /**
     * 返回此类型参数的边界。它们是用来声明此类型参数的 extends 子句所指定的类型。
     * 如果没有使用显式的 extends 子句，则认为 java.lang.Object 是唯一的边界
     */
    List<? extends TypeMirror> getBounds();
}