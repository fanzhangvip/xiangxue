package com.kotlin.syntx

import kotlin.coroutines.experimental.CoroutineContext

fun mask(): () -> Unit {
    var count = 0
    return {
        println("count = ${count++}")
    }
}

fun mask1(): Unit {
    var count1 = 0;
    return println("count1 = ${count1++}")
}

fun mask2(): Unit {
    var count2 = 0
    println("${fun() {
        println("count2 = ${count2++}")
    }}")
//    return pl()
}

fun <T :CoroutineContext.Element> T.test(t: T):T{return t}



val printMsg = {msg: String -> println(msg)}

val log = {str: String , printLog: (String) -> Unit  -> printLog(str)}



fun main(args: Array<String>) {
//    val mask = mask()
//    mask()
//    mask()
//    mask()
//    val mask1 = mask1()
//    mask1()
//    mask1()
//    mask1()
//
//    val mask2 = mask2()
//    mask2()
//    mask2()
//    mask2()
    val test: (Int, Int) -> Unit = { x, y ->
        if (x > y) {
            println("yes x=$x , y=$y")
        } else {
            println("no x=$x,y=$y")
        }
    }

    val a = 5 ushr   3

    val test1 = if (5 > 3) {
        println("yes")
    } else {
        println("no")
    }

    test.invoke(3,6)
    printMsg.invoke("hello world")

    log("fanzhang", printMsg)

}