package com.kotlin.syntx

import com.kotlin.demo07.Baz

/**
 * Created by zero on 2018/1/16.
 * Describe: function
 */
open class C

class D : C()

fun C.foo() = "c"
fun D.foo() = "d"

fun printFoo(c: C) {
    println(c.foo())//扩展函数是静态解析的,不是虚函数(即没有多态)
}

class C1 {
    fun foo() {
        println("member")
    }
}

fun C1.foo() {
    println("extension")
}

class C2 {
    fun foo() {
        println("member")
    }
}

fun C2.foo(i: Int) {
    println("extension")
}

fun Any?.toString(): String {
    if (this == null) return "null"
    return toString()
}

val <T> List<T>.lastIndex: Int
    get() = size - 1

class Foo(val bar: String = "Foo") {
    val ff: String = "ff"
}

val Foo.ff: String
    get() = "0"

//val Foo.bar = 1 //// 错误：扩展属性不能有初始化器
//    get() = 1

class MyClass {
    companion object {
        val name: String? = "fanzhang"

        fun test(): Unit {
            println("companion object test")
        }
    }
}

fun MyClass.Companion.foo() {
    println("MyClass.Companion.foo")
}

fun Baz.goo(){
    println("Baz.goo")
}

fun Baz.test1(): Unit {
    println("Baz.test1 extension")
}

fun Baz.test1(i: Int): Unit {
    println("Baz.test1 extension $i")
}

fun main(args: Array<String>) {
    printFoo(D())//输出"c",扩展函数调用只取决于参数c的声明类型

    val c1 = C1()
    println(c1.foo())//输出“member”
    val c2 = C2()
    println(c2.foo(2))//输出"extension"
    var nullObj = null
    println(c2.toString())
    println(nullObj.toString())

    val f = Foo()
    println("f = ${f.bar}")

    MyClass.foo()

    val baz = Baz()

    baz.goo()
    baz.test1()
    baz.test1(2)
}
