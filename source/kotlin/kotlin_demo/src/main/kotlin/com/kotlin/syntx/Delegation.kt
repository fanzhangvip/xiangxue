package com.kotlin.syntx

/**
 * Created by zero on 2018/1/22.
 * Describe: function
 */
interface Base{
    fun p1()
    fun p2()
}

class Impl(): Base{
    override fun p1() {
        println("p1_Imple")
    }

    override fun p2() {
        println("p2_Imple")
    }
}

class Deg(base:Base) : Base by base{
    override fun p2() {
        println("p2_Deg")
    }
}

fun main(args: Array<String>) {
    val deg = Deg(Impl())
    deg.p1()
    deg.p2()
}