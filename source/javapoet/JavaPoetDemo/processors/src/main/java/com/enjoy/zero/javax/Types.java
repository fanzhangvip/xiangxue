package com.enjoy.zero.javax;

import java.util.List;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.WildcardType;

/**
 * 一个用来处理TypeMirror的工具
 */
public interface Types {
    /**
     * 返回对应于类型的元素。该类型可能是 DeclaredType 或 TypeVariable。如果该类型没有对应元素，则返回 null.
     */
    Element asElement(TypeMirror t);
 
    /**
     * 测试两个 TypeMirror 对象是否表示同一类型.
     * 警告：如果此方法两个参数中有一个表示通配符，那么此方法将返回 false
     */
    boolean isSameType(TypeMirror t1, TypeMirror t2);
 
    /**
     * 测试一种类型是否是另一个类型的子类型。任何类型都被认为是其本身的子类型.
     *
     * @return 当且仅当第一种类型是第二种类型的子类型时返回 true
     * @throws IllegalArgumentException 如果给定一个 executable 或 package 类型
     */
    boolean isSubtype(TypeMirror t1, TypeMirror t2);
 
    /**
     * 测试一种类型是否可以指派给另一种类型.
     *
     * @return 当且仅当第一种类型可以指派给第二种类型时返回 true
     * @throws IllegalArgumentException 如果给定一个 executable 或 package 类型
     */
    boolean isAssignable(TypeMirror t1, TypeMirror t2);
 
    /**
     * 测试一个类型参数是否包含 另一个类型参数.
     *
     * @return 当且仅当第一种类型包含第二种类型时返回 true
     * @throws IllegalArgumentException 如果给定一个 executable 或 package 类型
     */
    boolean contains(TypeMirror t1, TypeMirror t2);
 
    /**
     * 测试一个方法的签名是否是另一个方法的子签名.
     *
     * @return 当且仅当第一个签名是第二个签名的子签名时返回 true
     */
    boolean isSubsignature(ExecutableType m1, ExecutableType m2);
 
    /**
     * 返回类型的直接超类型。interface 类型将出现在列表的最后（如果有）.
     *
     * @return 直接超类型；如果没有，则返回一个空列表
     * @throws IllegalArgumentException 如果给定一个 executable 或 package 类型
     */
    List<? extends TypeMirror> directSupertypes(TypeMirror t);
 
    /**
     * 返回删除状态的类型.
     *
     * @return 删除状态的给定类型
     * @throws IllegalArgumentException 如果给定一个 package 类型
     */
    TypeMirror erasure(TypeMirror t);
 
    /**
     * 返回给定基本类型的装箱 (boxed) 值类型的类。即应用 boxing 转换.
     *
     * @param p  要转换的基本类型
     * @return 类型 p 的装箱值类型的类
     */
    TypeElement boxedClass(PrimitiveType p);
 
    /**
     * 返回给定类型的拆箱 (unboxed) 值类型（基本类型）。即应用 unboxing 转换.
     *
     * @param t  要拆箱的类型
     * @return 类型 t 的拆箱值类型
     * @throws IllegalArgumentException 如果给定类型无法进行 unboxing 转换
     */
    PrimitiveType unboxedType(TypeMirror t);
 
    /**
     * 对类型应用 capture 转换.
     *
     * @return 应用 capture 转换的结果
     * @throws IllegalArgumentException 如果给定 executable 或 package 类型
     */
    TypeMirror capture(TypeMirror t);
 
    /**
     * 返回基本类型.
     *
     * @param kind  要返回的基本类型的种类
     * @return 一个基本类型
     * @throws IllegalArgumentException 如果 kind 不是基本种类
     */
    PrimitiveType getPrimitiveType(TypeKind kind);
 
    /**
     * 返回 null 类型。该类型是 null 的类型.
     */
    NullType getNullType();
 
    /**
     * 返回在实际类型不适用的地方所使用的伪类型。
     * 要返回的类型的种类可以是 VOID 或 NONE。对于包，可以使用 Elements.getPackageElement(CharSequence).asType() 替代.
     *
     * @param kind  要返回的类型的种类
     * @return 种类 VOID 或 NONE 的伪类型
     * @throws IllegalArgumentException 如果 kind 无效
     */
    NoType getNoType(TypeKind kind);
 
    /**
     * 返回具有指定组件类型的数组类型.
     *
     * @throws IllegalArgumentException 如果组件类型对于数组无效
     */
    ArrayType getArrayType(TypeMirror componentType);
 
    /**
     * 返回新的通配符类型参数。可以指定通配符边界中的一个，也可以都不指定，但不能都指定.
     *
     * @param extendsBound  扩展（上）边界；如果没有，则该参数为 null
     * @param superBound    超（下）边界；如果没有，则该参数为 null
     * @return 新的通配符
     * @throws IllegalArgumentException 如果边界无效
     */
    WildcardType getWildcardType(TypeMirror extendsBound,
                                 TypeMirror superBound);
 
    /**
     * 返回对应于类型元素和实际类型参数的类型。例如，如果给定 Set 的类型元素和 String 的类型镜像，那么可以使用此方法获取参数化类型 Set<String>.
     *
     * 类型参数的数量必须等于类型元素的形式类型参数的数量，或者等于 0。如果等于 0，并且类型元素是泛型，则返回该类型元素的原始类型.
     *
     * 如果返回一个参数化类型，则其类型元素不得包含在一般外部类中。
     * 例如，首先使用此方法获取类型 Outer<String>，然后调用 getDeclaredType(DeclaredType, TypeElement, TypeMirror...)，
     * 可以构造参数化类型 Outer<String>.Inner<Number>.
     *
     * @param typeElem  类型元素
     * @param typeArgs  实际类型参数
     * @return 对应于类型元素和实际类型参数的类型
     * @throws IllegalArgumentException 如果给定的类型参数太多或太少，或者提供不合适的类型参数或类型元素
     */
    DeclaredType getDeclaredType(TypeElement typeElem, TypeMirror... typeArgs);
 
    /**
     * 根据给定的包含类型，返回对应于类型元素和实际类型参数的类型（它是给定包含类型的成员）.例子如上
     * 如果包含类型是一个参数化类型，则类型参数的数量必须等于 typeElem 的形式类型参数的数量。
     * 如果包含类型不是参数化的，或者为 null，则此方法等效于 getDeclaredType(typeElem, typeArgs）.
     *
     * @param containing  包含类型；如果没有，则该参数为 null
     * @param typeElem  类型元素
     * @param typeArgs  实际类型参数
     * @return 对应于类型元素和实际类型参数的类型，该类型包含在给定类型中
     * @throws IllegalArgumentException 如果给定的类型参数太多或太少，或者提供了不合适的类型参数、类型元素或包含类型
     */
    DeclaredType getDeclaredType(DeclaredType containing,
                                 TypeElement typeElem, TypeMirror... typeArgs);
 
    /**
     * 当元素被视为给定类型的成员或者直接由给定类型包含时，返回该元素的类型。
     * 例如，被视为参数化类型 Set<String> 的成员时，Set.add 方法是参数类型为 String 的 ExecutableType.
     *
     * @param containing  包含类型
     * @param element     元素
     * @return 从包含类型来看的元素的类型
     * @throws IllegalArgumentException 如果元素对于给定类型无效
     */
    TypeMirror asMemberOf(DeclaredType containing, Element element);
}