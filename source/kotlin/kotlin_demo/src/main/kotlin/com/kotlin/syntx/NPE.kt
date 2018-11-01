package com.kotlin.syntx

/**
 * Created by zero on 2018/1/16.
 * Describe: function
 */
fun testNull1() {
    var a: String = "abc"
//    a = null//编译报错
}

fun testNull2() {
    var b: String? = "abc"
    b = null //OK
}

fun testNull3(a: String, b: String?) {
    println(a)
    println(b)
    println("a.length = ${a.length}")
    println("b?.length = ${b?.length}")//安全调用操作符 ?.
    println("b!!.length = ${b!!.length}")//!! 操作符 要么返回b的长度length，要么就一言不合抛出NPE
    val a_len: Int = if (a != null) a.length else -1
    println("a_len = $a_len")
    //or
    val b_len: Int = b?.length ?: -1
    println("b_len = $b_len")
}

fun testNull4(a: String?): String? {//直接把return当作表达式放右侧
    return a ?: return null
}

fun testNull5(a: String?): String {////直接把throw当作表达式放右侧
    return a ?: throw IllegalAccessException("a is null")
}

fun <T> testNull6(a: T, b: T) {
    val aInt: Int? = a as Int
    val bInt: Int? = b as? Int
    println("aInt = $aInt")
    println("bInt = $bInt")
}

fun main(args: Array<String>) {
    testNull1()
    testNull2()
    testNull3("a", "b")
//    testNull3("a",null)
    testNull4("a")
    testNull4(null)
    testNull5("a")
//    testNull5(null)
    testNull6(1, "a")

    var a: String
    var b: String?
//    a = null
    b = null
    // ? 和?. 新增的null机制，有效避免空指针
    // !! 和?: Kotlin代码中不会出现大量的非空判断
}
