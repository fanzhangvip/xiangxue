package com.enjoy.zero.javax;

import java.util.List;

import javax.lang.model.element.AnnotationValue;

/**
 * 表示某个类或接口的方法、构造方法或初始化程序（静态或实例），包括注解类型元素
 */
public interface ExecutableElement {
 
    /**
     * 获取按照声明顺序返回形式类型参数元素
     */
    List<? extends TypeParameterElement> getTypeParameters();
 
    /**
     * 获取返回的类型元素
     */
    TypeMirror getReturnType();
 
    /**
     * 获取形参元素
     */
    List<? extends VariableElement> getParameters();
 
    /**
     * 如果此方法或构造方法接受可变数量的参数，则返回 true，否则返回 false
     */
    boolean isVarArgs();
 
    /**
     * 按声明顺序返回此方法或构造方法的 throws 子句中所列出的异常和其他 throwable
     */
    List<? extends TypeMirror> getThrownTypes();
 
    /**
     * 如果此 executable 是一个注解类型元素，则返回默认值。如果此方法不是注解类型元素，或者它是一个没有默认值的注解类型元素，则返回 null
     */
    AnnotationValue getDefaultValue();
}