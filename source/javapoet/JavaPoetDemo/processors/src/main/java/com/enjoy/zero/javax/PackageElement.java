package com.enjoy.zero.javax;

import javax.lang.model.element.Name;

/**
 * 表示一个包程序元素.
 */
public interface PackageElement {
 
    /**
     * 返回此包的完全限定名称。该名称也是包的规范名称
     */
    Name getQualifiedName();
 
    /**
     * 如果此包是一个未命名的包，则返回 true，否则返回 false
     */
    boolean isUnnamed();

}