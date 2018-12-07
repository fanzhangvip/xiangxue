package com.enjoy.zero.javax;

/**
 * 表示一个字段、enum 常量、方法或构造方法参数、局部变量或异常参数
 */
public interface VariableElement {

    /**
     * 如果此变量是一个被初始化为编译时常量的 static final 字段，则返回此变量的值。否则返回 null。
     * 该值为基本类型或 String，如果该值为基本类型，则它被包装在适当的包装类中（比如 Integer）。
     * 注意，并非所有的 static final 字段都将具有常量值。特别是，enum 常量不 被认为是编译时常量。要获得一个常量值，字段的类型必须是基本类型或 String
     */
    Object getConstantValue();
}