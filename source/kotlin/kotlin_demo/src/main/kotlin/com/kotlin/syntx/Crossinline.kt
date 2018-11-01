package com.kotlin.syntx

fun outterFun() {

    innerFun {
        //return报错 不支持直接返回return
        return@innerFun
    }

    var f = fun() {
        return
    }

}

fun outterFun1() {
    innerFun1 {
        return  //支持直接返回outterFun
    }
}

fun outterFun2() {
    innerFun2 {
        return@innerFun2
    }
}

fun innerFun(a: () -> Unit) {}
inline fun innerFun1(a: () -> Unit) {}
inline fun innerFun2(crossinline a: () -> Unit) {}


inline fun foo(inlined: () -> Unit, noinline notInlined: () -> Unit) {}

interface TestInterface{
    fun test(a:Int):Int
}

inline fun testInterface(crossinline t: (Int) -> Int): TestInterface = object : TestInterface {
    override fun test(a: Int): Int = t.invoke(a)
}

inline public fun thread(start: Boolean = true, isDaemon: Boolean = false, contextClassLoader: ClassLoader? = null, name: String? = null, priority: Int = -1,crossinline block: () -> Unit): Thread {
    val thread = object : Thread() {
        public override fun run() {
            block()
        }
    }
    if (isDaemon)
        thread.isDaemon = true
    if (priority > 0)
        thread.priority = priority
    if (name != null)
        thread.name = name
    if (contextClassLoader != null)
        thread.contextClassLoader = contextClassLoader
    if (start)
        thread.start()
    return thread
}

fun test(f: () -> Unit) {
    thread(block = f)
}

inline fun test1(crossinline f: () -> Unit) {
    thread(block = f)
}

inline fun test2(noinline  f: () -> Unit){
    thread { f() }
}

fun main(args: Array<String>) {
    testInterface {
        1
    }

    innerFun2{
        2
    }
}


