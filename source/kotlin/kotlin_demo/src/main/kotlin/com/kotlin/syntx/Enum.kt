package com.kotlin.syntx


/**
 * Created by zero on 2018/1/22.
 * Describe: function
 */
enum class Direction {
    //每个枚举常量(Enum Constants)都是一个对象,用逗号分隔
    NORTH,
    SOUTH, WEST, EAST;
}

enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF);

    inline fun <reified T : Enum<T>> printAllValues() {
        print(enumValues<T>().joinToString { it.name })
    }
}

enum class ProtocolState {
    WAITING {
        override fun signal() = TALKING
    },
    TALKING {
        override fun signal() = WAITING
    }; //注意:像Java一样,Kotlin枚举常量也需要分号;分隔!

    abstract fun signal(): ProtocolState
}

fun main(args: Array<String>) {
    Color.BLUE.printAllValues<Color>()
    val protocal = ProtocolState.TALKING.signal()
    println("\nprotocal = $protocal")
}