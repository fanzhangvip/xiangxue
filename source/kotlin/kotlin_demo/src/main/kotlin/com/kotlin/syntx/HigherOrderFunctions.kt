package com.kotlin.syntx

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

inline fun <T> lock(lock: Lock, body: () -> T): T {
    lock.lock()
    try {
        return body()
    } finally {
        lock.unlock()
    }
}

fun autoAdd(): Int {
    return 1
}

fun <T, R> List<T>.maps(transformer: (T) -> R): List<R> {
    val result = arrayListOf<R>()
    for (item in this) {
        result.add(transformer(item))
    }
    return result
}

fun <T> max(collection: Collection<T>, less: (T, T) -> Boolean): T? {
    //less参数类型是一个函数,该函数有两个参数类型是T,返回类型是Boolean
    var max: T? = null
    for (it in collection)
        if (max == null || less(max, it))
            max = it
    return max
}

inline fun finline( inlined: () -> Unit, noinline notInlined: () -> Unit){

}

fun main(args: Array<String>) {
    val lck = ReentrantLock()

    val result = lock(lck, ::autoAdd)
    val ll = lock(lck) {
        println("lock run ...")
        "2"
    }
    println("result = $result")
    println("ll = $ll")
    val a = ::findFixPoint
    println("a = $a")

    val ints = mapOf<Int, Int>(1 to 1, 2 to 2, 3 to 3)
    ints.forEach { _, value ->
        println("value = $value")
    }

    val mls = listOf(1, 2, 3)
    val mlss = mls.maps { value -> value * 2 }
    println("mlss = $mlss")


    val list = listOf(1,2,3,4,65,1,6,4,7,4,7,45)

    println(max(list,{ a,b -> a>b })) //输出2

    //等价于:
    val compareVal = {a: Int, b: Int -> a<b}
    println(max(list, compareVal)) //输出2

    //等价于:
    fun compareFun(a: Int, b: Int): Boolean = a < b
    println(max(list, ::compareFun)) //输出2

    val sum :(Int,Int) ->  Int = {x,y -> x+y}
    val sum1 = {x: Int , y: Int -> x + y}
    val s = sum(2,3)
    println("s = $s")

    fun(x: Int,y: Int) : Int = x + y
    val slist = listOf(-1,2,3,0,1)
    var sums = 0
    slist.filter {  it > 0 }.forEach {
        sums += it
    }
    println("sums = $sums")


}