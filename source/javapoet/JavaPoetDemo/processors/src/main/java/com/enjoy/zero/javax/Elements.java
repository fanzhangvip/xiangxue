package com.enjoy.zero.javax;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Name;

/**
 * 一个用来处理Element的工具
 */
public interface Elements {
 
    /**
     * 返回已给出其完全限定名称的包.
     *
     * @param name  完全限定的包名称；对于未命名的包，该参数为 ""
     * @return 指定的包；如果没有找到这样的包，则返回 null
     */
    PackageElement getPackageElement(CharSequence name);
 
    /**
     * 返回已给出其规范名称的类型元素.
     *
     * @param name  规范名称
     * @return 指定的类型元素；如果没有找到这样的元素，则返回 null
     */
    TypeElement getTypeElement(CharSequence name);
 
    /**
     * 返回注释元素的值，包括默认值.
     * 此值是以映射的形式返回的，该映射将元素与其相应的值关联。只包括那些注释中明确存在其值的元素，不包括那些隐式假定其默认值的元素。
     * 映射的顺序与值出现在注释源中的顺序匹配
     *
     * @see AnnotationMirror#getElementValues()
     * @param a  要检查的注释
     * @return 注释元素的值，包括默认值
     */
    Map<? extends ExecutableElement, ? extends AnnotationValue>
    getElementValuesWithDefaults(AnnotationMirror a);
 
    /**
     * 返回元素的文档（"Javadoc"）注释文本
     *
     * @param e  将被检查的元素
     * @return 元素的文档注释；如果没有，则返回 null
     */
    String getDocComment(Element e);
 
    /**
     * 如果元素已过时，则返回 true，否则返回 false.
     *
     * @param e  将被检查的元素
     * @return 如果元素已过时，则返回 true，否则返回 false
     */
    boolean isDeprecated(Element e);
 
    /**
     * 返回类型元素的二进制名称.
     *
     * @param type  将被检查的类型元素
     * @return 二进制名称
     */
    Name getBinaryName(TypeElement type);
 
    /**
     * 返回元素的包。包的包是它本身.
     *
     * @param type 将被检查的元素
     * @return 元素的包
     */
    PackageElement getPackageOf(Element type);
 
    /**
     * 返回类型元素的所有成员，不管是继承的还是直接声明的。对于一个类，结果还包括其构造方法，但不包括局部类或匿名类.
     *
     * 注意，使用 ElementFilter 中的方法可以隔离某个种类的元素.
     *
     * @param type  将被检查的类型
     * @return 类型的所有成员
     */
    List<? extends Element> getAllMembers(TypeElement type);
 
    /**
     * 返回元素的所有注释，不管是继承的还是直接存在的.
     *
     * @param e  将被检查的元素
     * @return 元素的所有注释
     */
    List<? extends AnnotationMirror> getAllAnnotationMirrors(Element e);
 
    /**
     * 测试一个类型、方法或字段是否隐藏了另一个类型、方法或字段.
     *
     * @param hider   第一个元素
     * @param hidden  第二个元素
     * @return 当且仅当第一个元素隐藏了第二个元素时返回 true
     */
    boolean hides(Element hider, Element hidden);
 
    /**
     * 测试一个方法（作为给定类型的成员）是否重写了另一个方法。当非抽象方法重写抽象方法时，还可以说成是前者实现 了后者.
     *
     * @param overrider  第一个方法，可能是 overrider
     * @param overridden  第二个方法，可能被重写
     * @param type   第一个方法是其成员的类型
     * @return 当且仅当第一个方法重写第二个方法时返回 true
     */
    boolean overrides(ExecutableElement overrider, ExecutableElement overridden,
                      TypeElement type);
 
    /**
     * 返回表示基本值或字符串的常量表达式 文本。返回文本的形式是一种适合于表示源代码中的值的形式.
     *
     * @param value  基本值或字符串
     * @return 常量表达式的文本
     * @throws IllegalArgumentException 如果参数不是基本值或字符串
     *
     * @see VariableElement#getConstantValue()
     */
    String getConstantExpression(Object value);
 
    /**
     * 按指定顺序将元素的表示形式打印到给定 writer。此方法的主要用途是诊断。输出的具体格式没有 指定并且是可以更改的.
     *
     * @param w 输出打印到的 writer
     * @param elements 要打印的元素
     */
    void printElements(java.io.Writer w, Element... elements);
 
    /**
     * 返回与参数具有相同字符序列的名称.
     *
     * @param cs 将以名称形式返回的字符序列
     * @return 返回与参数具有相同字符序列的名称
     */
    Name getName(CharSequence cs);
 
    /**
     * 如果类型是一个泛型接口则返回 true，否则返回 false
     *
     * @param type 将被检查的类型
     * @return 如果类型是一个泛型接口则返回 true，否则返回 false
     * @since 1.8
     */
    boolean isFunctionalInterface(TypeElement type);
}