package com.enjoy.zero.javax;

/**
 * 表示 Java 编程语言中的类型
 */
public interface TypeMirror {
    /**
     * 返回此类型的种类，一个 TypeKind 枚举值：
     */
    TypeKind getKind();
}