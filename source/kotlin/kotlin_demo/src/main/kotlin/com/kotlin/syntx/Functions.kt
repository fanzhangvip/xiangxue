package com.kotlin.syntx

/**
 * Created by zero on 2018/1/22.
 * Describe: function
 */
fun demo(): Unit {

}

fun demo1() {}

fun double(x: Int): Int = x * 2

fun double1(x: Int) = x * 2

infix fun Int.MyAdd(i: Int): Int {
    return this + i + 1
}

class MyInt(var a: Int) {
    infix fun aad(i: Int): Int {
        return a + i
    }
}

fun NamedArguments(a: String = "fanzhang"
                   , b: Boolean = true
                   , c: Int = 1
                   , d: Char = 'w') {
    println("a = $a,b=$b,c = $c,d = $d")
}

fun <T : Comparable<T>> VarargFun(vararg ts: T) {
    for (t in ts) {
        print("$t ")
    }
    println()
}

tailrec fun findFixPoint(x: Double = 1.0): Double {
    return if (x == Math.cos(x)) x else findFixPoint(Math.cos(x))
}

fun main(args: Array<String>) {
    println("2 MyAdd 3 = ${2 MyAdd 3}")
    println("2.MyAdd(3) = ${2.MyAdd(3)}")

    val i = MyInt(2)
    println("i MyAdd 3 = ${i aad 3}")
    println("i.MyAdd(3) = ${i.aad(3)}")

    NamedArguments()
    NamedArguments(c = 2)
    NamedArguments(a = "wanghuanj", c = 23)

    VarargFun(1, 2, 4, 65)
    val a = arrayOf(1, 2, 4)
    VarargFun(0, *a, 6)

    println("findFixPoint = ${findFixPoint(22.0)}")

}