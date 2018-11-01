package com.git.kotlin.demo05

fun main(args: Array<String>) {

    val l = mutableListOf(1, 2, 3)
    println("l = $l")
    l.swap(1, 2)
    println("l = $l")
    println("l.lastIndex: = ${l.lastIndex}")

    printFoo(C())

    printFoo(D())

    MyClass.foo()
}


fun <T> MutableList<T>.swap(x: Int, y: Int) {
    println("x = $x, y = $y")
    val tmp = this[x]
    this[x] = this[y]
    this[y] = tmp
}

open class C {
    fun foo(): String {
        println("member")
        return "member"
    }
}

class D : C()

fun C.foo() = "c"

fun D.foo() = "d"

fun printFoo(c: C) {
    println("c = ${c.foo()}")
}

fun Any?.toString(): String {
    if (this == null) return "null"
    return toString()
}

//������չ
val <T> List<T>.lastIndex: Int
    get() = size - 1

class MyClass {
    companion object {}
}

fun MyClass.Companion.foo() {
    println("Companion.foo")
}

class Person(val name: String = "fanzhang")





















