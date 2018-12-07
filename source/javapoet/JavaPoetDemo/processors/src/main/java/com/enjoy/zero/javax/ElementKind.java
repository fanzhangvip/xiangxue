package com.enjoy.zero.javax;

//元素的种类 和元素的类型TypeMirror很容易混掉。
//public class Sample         // TypeElement
//        <T extends List> {  // TypeParameterElement
//
//    private int num;        // VariableElement
//    String name;            // VariableElement
//
//    public Sample() {}      // ExecuteableElement
//
//    public void setName(    // ExecuteableElement
//                            String name     // VariableElement
//    ) {}
//}
//TypeMirror表示的是 Java 编程语言中的类型，比如上面例子中的字段String name，
//它的元素种类为FIELD，
//而它的元素类型为DECLARED表示一个类类型，这里对应Java 编程语言中的类型为java.lang.String。
//Element代表的是源代码上的元素，
//TypeMirror代表的是Element对应Java 编程语言中的类型
public enum ElementKind {
 
    /** A package. */
    PACKAGE,
    /** An enum type. */
    ENUM,
    /** A class not described by a more specific kind (like {@code ENUM}). */
    CLASS,
    /** An annotation type. */
    ANNOTATION_TYPE,
    /** An interface not described by a more specific kind */
    INTERFACE,
 
    // Variables
    /** An enum constant. */
    ENUM_CONSTANT,
    /** A field not described by a more specific kind */
    FIELD,
    /** A parameter of a method or constructor. */
    PARAMETER,
    /** A local variable. */
    LOCAL_VARIABLE,
    /** A parameter of an exception handler. */
    EXCEPTION_PARAMETER,
 
    // Executables
    /** A method. */
    METHOD,
    /** A constructor. */
    CONSTRUCTOR,
    /** A static initializer. */
    STATIC_INIT,
    /** An instance initializer. */
    INSTANCE_INIT,
    /** A type parameter. */
    TYPE_PARAMETER,
 
    /** An implementation-reserved element. This is not the element you are looking for. */
    OTHER,
    /**
     * A resource variable.
     * @since 1.7
     */
    RESOURCE_VARIABLE;
}