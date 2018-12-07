package com.enjoy.zero.javax;

import java.util.List;

import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;

/**
 * 表示一个类或接口程序元素
 */
public interface TypeElement {
 
    /**
     * 返回此类型元素的嵌套种类
     * 某一类型元素的嵌套种类 (nesting kind)。类型元素的种类有四种：top-level（顶层）、member（成员）、local（局部）和 anonymous（匿名）
     */
    NestingKind getNestingKind();
 
    /**
     * 返回此类型元素的完全限定名称。更准确地说，返回规范 名称。对于没有规范名称的局部类和匿名类，返回一个空名称.
     * 一般类型的名称不包括对其形式类型参数的任何引用。例如，接口 java.util.Set<E> 的完全限定名称是 "java.util.Set"
     */
    Name getQualifiedName();
 
    /**
     * 返回此类型元素的直接超类。如果此类型元素表示一个接口或者类 java.lang.Object，则返回一个种类为 NONE 的 NoType
     */
    TypeMirror getSuperclass();
 
    /**
     * 返回直接由此类实现或直接由此接口扩展的接口类型
     */
    List<? extends TypeMirror> getInterfaces();
 
    /**
     * 按照声明顺序返回此类型元素的形式类型参数
     */
    List<? extends TypeParameterElement> getTypeParameters();

}