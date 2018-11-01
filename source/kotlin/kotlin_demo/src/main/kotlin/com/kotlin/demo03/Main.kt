package com.kotlin.demo03

import com.kotlin.demo01.Bar as Bar1
import com.kotlin.demo01.Foo as Foo1

import com.kotlin.demo02.Bar
import com.kotlin.demo02.Foo


fun main(args: Array<String>) {

    val a: Int = 1000
    println(a === a)
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a
    println(boxedA === anotherBoxedA)

    val a1: Int? = 1
    val b1: Long? = a1?.toLong();

    val l = 1.toLong() + 1

    val x = (1 shl 2) and 3

    fun decimalDigitValue(c: Char): Int {
        if (c !in '0'..'9') {
            println("$c is not a number")
        }
        return c.toInt() - '0'.toInt()
    }

    val d = decimalDigitValue('5')
    println("d is $d")

    val asc = Array(5, { i -> (i * i - 3).toString() })

    println("asc = $asc")


    val s = "Hello World!\n"
    println(s)
    val text = """
 for (c in "foo") //�����ַ���
	 	print(c)
	"""
    println(text)

    val bar1 = Bar1()
    val bar = Bar()
    println("bar = $bar, bar1 = $bar1")

    loop1@ for (i in 1..60) {
        if (i == 45) {
            println("i == $i")
            break@loop1
        }
    }

    fun foo(ints: List<String>) {
        ints.forEach {
            if (it == "a") return@forEach
            println(it)
        }
    }

    fun foo1(ints: List<String>) {
        ints.forEach lit@{
            if (it == "a") return@lit
            println(it)
        }
    }

    fun foo2(ints: List<String>) {
        ints.forEach lit@{
            if (it == "a") return
            println(it)
        }
    }

    fun foo3(ints: List<String>) {
        lo@ ints.forEach {
            if (it == "a") return@foo3
            println(it)
        }
    }

    var list = listOf("aadf", "a", "wer")
    println("return@forEach")
    foo(list)
    println("return@lit")
    foo1(list)
    println("return")
    foo2(list)
    println("return@foo3")
    foo3(list)
    println()

    loo@ for (i in 1..100) {
        ll@ for (j in i..100) {
            if (i * j == i * 9) {
                break@ll
            }
            println("$i * $j = ${i * j}")
        }
    }

}

