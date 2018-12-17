package com.enjoy.zero.javax;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;

/**
 * 表示一个程序元素，比如包、类或者方法，有如下几种子接口：
 * ExecutableElement：表示某个类或接口的方法、构造方法或初始化程序（静态或实例），包括注解类型元素 ；
 * PackageElement：表示一个包程序元素；
 * TypeElement：表示一个类或接口程序元素；
 * TypeParameterElement：表示一般类、接口、方法或构造方法元素的形式类型参数；
 * VariableElement：表示一个字段、enum 常量、方法或构造方法参数、局部变量或异常参数
 */
//Element 代表程序的元素，在注解处理过程中，编译器会扫描所有的Java源文件，并将源码中的每一个部分都看作特定类型的 Element。它可以代表包、类、接口、方法、字段等多种元素种类，
// 具体看getKind()方法中所指代的种类，每个Element 代表一个静态的、语言级别的构件。

//Element 有五个直接子接口，它们分别代表一种特定类型的元素，如下：


//PackageElement	表示一个包程序元素
//TypeElement	表示一个类或接口程序元素
//VariableElement	表示一个字段、enum 常量、方法或构造方法参数、局部变量或异常参数
//ExecutableElement	表示某个类或接口的方法、构造方法或初始化程序（静态或实例），包括注解类型元素
//TypeParameterElement	表示一般类、接口、方法或构造方法元素的泛型参数

public interface Element extends AnnotatedConstruct {
    /**
     * 返回此元素定义的类型
     * 例如，对于一般类元素 C<N extends Number>，返回参数化类型 C<N>
     */
    TypeMirror asType();
 
    /**
     * 返回此元素的种类：包、类、接口、方法、字段...,如下枚举值
     * PACKAGE, ENUM, CLASS, ANNOTATION_TYPE, INTERFACE, ENUM_CONSTANT, FIELD, PARAMETER, LOCAL_VARIABLE, EXCEPTION_PARAMETER,
     * METHOD, CONSTRUCTOR, STATIC_INIT, INSTANCE_INIT, TYPE_PARAMETER, OTHER, RESOURCE_VARIABLE;
     */
    ElementKind getKind();
 
    /**
     * 返回此元素的修饰符,如下枚举值
     * PUBLIC, PROTECTED, PRIVATE, ABSTRACT, DEFAULT, STATIC, FINAL,
     * TRANSIENT, VOLATILE, SYNCHRONIZED, NATIVE, STRICTFP;
     */
    Set<Modifier> getModifiers();
 
    /**
     * 返回此元素的简单名称,例如
     * 类型元素 java.util.Set<E> 的简单名称是 "Set"；
     * 如果此元素表示一个未指定的包，则返回一个空名称；
     * 如果它表示一个构造方法，则返回名称 "<init>"；
     * 如果它表示一个静态初始化程序，则返回名称 "<clinit>"；
     * 如果它表示一个匿名类或者实例初始化程序，则返回一个空名称
     */
    Name getSimpleName();
 
    /**
     * 返回封装此元素的最里层元素。
     * 如果此元素的声明在词法上直接封装在另一个元素的声明中，则返回那个封装元素；
     * 如果此元素是顶层类型，则返回它的包；
     * 如果此元素是一个包，则返回 null；
     * 如果此元素是一个泛型参数，则返回 null.
     */
    Element getEnclosingElement();
 
    /**
     * 返回此元素直接封装的子元素
     */
    List<? extends Element> getEnclosedElements();
 
    /**
     * 返回直接存在于此元素上的注解
     * 要获得继承的注解，可使用 getAllAnnotationMirrors
     */
    @Override
    List<? extends AnnotationMirror> getAnnotationMirrors();
 
    /**
     * 返回此元素针对指定类型的注解（如果存在这样的注解），否则返回 null。注解可以是继承的，也可以是直接存在于此元素上的
     */
    @Override
    <A extends Annotation> A getAnnotation(Class<A> annotationType);

    /**
     * Applies a visitor to this element.
     *
     * @param <R> the return type of the visitor's methods
     * @param <P> the type of the additional parameter to the visitor's methods
     * @param v   the visitor operating on this element
     * @param p   additional parameter to the visitor
     * @return a visitor-specified result
     */
    <R, P> R accept(ElementVisitor<R, P> v, P p);
}