package com.kotlin.syntx

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

open class A(x: Int) {
    public open val y: Int = x
}

interface B {

}

class CC {
    private fun foo() = object {
        val x: String = "x"
    }

    fun pFoo() = object {
        val x: String = "X"
    }

    fun bar() {
        val x1 = foo().x
        val x2 = pFoo()
    }
}

class MyClass1 {
    companion object Obj {
        val a = 1
        fun crt(): MyClass1 = MyClass1()
    }

    val b = 2
}

interface Factory<T> {
    fun create(): T
}

class MyCls {
    companion object : Factory<MyClass1> {
        override fun create(): MyClass1 = MyClass1()
    }
}

open class TypeLiteral<T> {
    val type: Type
        get() = (javaClass.getGenericSuperclass() as ParameterizedType).getActualTypeArguments()[0]
}

inline fun <reified T> typeLiteral(): TypeLiteral<T> = object : TypeLiteral<T>() {}

fun main(args: Array<String>) {
    val ab: A = object : A(1), B {
        override val y = 15
    }

    println(ab.y)

    val obj = object {
        var x: Int = 1
        var y: Int = 2
    }

    println(obj.x + obj.y)
    MyCls.create()

    println("${typeLiteral<String>().type}")
}