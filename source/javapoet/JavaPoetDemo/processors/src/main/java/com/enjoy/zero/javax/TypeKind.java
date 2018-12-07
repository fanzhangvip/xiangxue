package com.enjoy.zero.javax;

//getKind()方法来获取具体的类型，方法返回一个枚举值
public enum TypeKind {
    /** The primitive type {@code boolean}. */
    BOOLEAN,
    /** The primitive type {@code byte}. */
    BYTE,
    /** The primitive type {@code short}. */
    SHORT,
    /** The primitive type {@code int}. */
    INT,
    /** The primitive type {@code long}. */
    LONG,
    /** The primitive type {@code char}. */
    CHAR,
    /** The primitive type {@code float}. */
    FLOAT,
    /** The primitive type {@code double}. */
    DOUBLE,
    /** The pseudo-type corresponding to the keyword {@code void}. */
    VOID,
    /** A pseudo-type used where no actual type is appropriate. */
    NONE,
    /** The null type. */
    NULL,
    /** An array type. */
    ARRAY,
    /** A class or interface type. */
    DECLARED,
    /** A class or interface type that could not be resolved. */
    ERROR,
    /** A type variable. */
    TYPEVAR,
    /** A wildcard type argument. */
    WILDCARD,
    /** A pseudo-type corresponding to a package element. */
    PACKAGE,
    /** A method, constructor, or initializer. */
    EXECUTABLE,
    /** An implementation-reserved type. This is not the type you are looking for. */
    OTHER,
    /** A union type. */
    UNION,
    /** An intersection type. */
    INTERSECTION;
}