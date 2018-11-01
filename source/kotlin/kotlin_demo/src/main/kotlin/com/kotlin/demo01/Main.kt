package com.kotlin.demo01

import java.util.*

fun sum(a: Int, b: Int): Int {
    return a + b
}

fun sum1(a: Int, b: Int) = a + b

fun printSum(a: Int, b: Int): Unit {
    println("sum of $a and $b is ${a + b}")
}

fun printSum1(a: Int, b: Int) {
    println("sum of $a and $b is ${a + b}")
}

fun maxOf(a: Int, b: Int): Int {
    if (a > b) {
        return a
    } else {
        return b
    }
}

fun maxOf1(a: Int, b: Int) = if (a > b) a else b

fun parseInt(str: String): Int? {
    //当空值可能出现时应该明确指出该引用可空
    return str.toIntOrNull()
}

fun printProduct(arg1: String, arg2: String) {
    val x = parseInt(arg1)
    var y = parseInt(arg2)

    if (x != null && y != null) {
        println("'$x' * '$y' = ${x * y}")
    } else {
        println("either '$arg1' or '$arg2' is not a number")
    }
}

fun printProduct1(arg1: String, arg2: String) {
    val x = parseInt(arg1)
    val y = parseInt(arg2)

    if (x == null) {
        println("wrong number format in arg1: '${arg1}'")
        return
    }

    if (y == null) {
        println("wrong number format in arg1: '${arg2}'")
        return
    }

    println("'$x' * '$y' = ${x * y}")
}

//使用值检查并自动转换 使用 is 操作符检查一个表达式是否是某个类型的实例。如果对不可变的局部变量或属性进行过了类型检查，就没有必要明确转换：
fun getStringLength(obj: Any): Int? {
    if (obj is String) {
        return obj.length
    }
    return null
}

fun getStringLength1(obj: Any): Int? {
    if (obj !is String) return null
    return obj.length
}

fun getStringLength2(obj: Any): Int? {
    if (obj is String && obj.length > 0) {
        return obj.length
    }
    return null
}

//使用 when 表达式
fun describe(obj: Any): String =
        when (obj) {
            1 -> "One"
            "Hello" -> "Greeting"
            is Long -> "Long"
            !is String -> "Not a string"
            else -> "Unkown"
        }


fun main(args: Array<String>) {
    print("sum of 3 and 5 is ")
    println(sum(3, 5))
    println()

    println("sum of 19 and 23 is ${sum1(19, 23)}")
    printSum(-1, 8)
    printSum1(3, 6)
    println()

    val a: Int = 1
    val b = 2
    val c: Int
    c = 3
    println("a = $a, b = $b, c = $c")
    println()

    var x = 5
    x += 1
    println("x = $x")
    println()
    var sa = 1
    //使用变量名作为模版:
    var s1 = "sa is $sa"
    sa = 2
    //使用表达式作为模版
    var s2 = "${s1.replace("is", "was")}, but now is $sa"
    println(s2)
    println()

    println("max of 0 and 42 is ${maxOf(0, 42)}")
    println("max of 2 and 5 is ${maxOf(2, 5)}")
    println()

    printProduct("6", "7")
    printProduct("a", "7")
    printProduct("a", "b")
    println()

    printProduct1("6", "7")
    printProduct1("a", "7")
    printProduct1("a", "b")
    println()

    fun printLength(obj: Any) {
        println("'$obj' string length is ${getStringLength(obj) ?: "... err, not a string"}")
    }
    printLength("fasdlkfjsdlkfjweoirwejrwes dfwer")
    printLength(1000)
    printLength(listOf(Any()))
    println()

    //使用循环
    val items = listOf("apple", "banana", "kiwi")
    for (item in items) {
        println("item = $item")
    }
    println()

    for (index in items.indices) {
        println("item at $index is ${items[index]}")
    }
    println()

    //使用 while 循环
    var index = 0
    while (index < items.size) {
        println("item at $index is ${items[index]}")
        index++
    }
    println()

    println(describe(1))
    println(describe("Hello"))
    println(describe(1000L))
    println(describe(2))
    println(describe("other"))
    println()

    //检查 in 操作符检查数值是否在某个范围内：
    val rx = 10
    val ry = 9
    if (rx in 1..ry + 1) {
        println("$rx fits in range")
    }
    println()

    //检查数值是否在范围外：
    val list = listOf("a", "b", "c")
    if (-1 !in 0..list.lastIndex) {
        println("-1 is out of range")
    }
    if (list.size !in list.indices) {
        println("list size is out of valid list indices range too")
    }
    println()

    //在范围内迭代：
    for (x in 1..5) {
        println("x is $x")
    }
    println()

    //或者使用步进：
    for (x in 1..10 step 2) {
        println("x step is $x")
    }
    println()

    for (x in 9 downTo 0 step 3) {
        println("x downTo is $x")
    }
    println()

    //使用 in 操作符检查集合中是否包含某个对象
    val stItems = setOf("apple", "banana", "kiwi")
    when {
        "orange" in stItems -> println("juicy")
        "apple" in stItems -> println("apple is fine too")
    }
    println()

    //使用lambda表达式过滤和映射集合
    val fruits = listOf("banana", "avocado", "apple", "kiwi")
    fruits
            .filter { it.startsWith("a") }
            .sortedBy { it }
            .map { it.toUpperCase() }
            .forEach { println("it is $it") }
    println()
}

data class Book(val name:String = "")

data class Foo(val f:String="f")
data class Bar(var b:String="b")