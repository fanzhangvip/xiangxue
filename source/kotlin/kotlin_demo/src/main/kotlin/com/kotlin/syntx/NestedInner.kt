package com.kotlin.syntx

/**
 * Created by zero on 2018/1/22.
 * Describe: function
 */
class Outer {
    private val bar: Int = 1

    class Nest {
        fun foo() = 2
    }
}

class Outer1 {
    private var bar: Int = 1

    inner class Inner {
        fun foo() = bar
    }
}

fun main(args: Array<String>) {
    println("Outer:${Outer.Nest().foo()}")
    println("Outer1:${Outer1().Inner().foo()}")
}