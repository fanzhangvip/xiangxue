package com.kotlin.syntx

import java.util.concurrent.*
import kotlin.coroutines.experimental.*


class FilePath(val path: String) : AbstractCoroutineContextElement(FilePath) {
    companion object Key : CoroutineContext.Key<FilePath>
}

fun calcMd5(path: String): String {
    println("Calc md5 for $path")
    Thread.sleep(1000)
    return System.currentTimeMillis().toString()
}

fun asyncCalcMd5(path: String, block: suspend () -> Unit) {
    val continuation = object : Continuation<Unit> {
        override val context: CoroutineContext
            get() = FilePath(path) //To change initializer of created properties use File | Settings | File Templates.

        override fun resume(value: Unit) {
            println("resume: $value") //To change body of created functions use File | Settings | File Templates.
        }

        override fun resumeWithException(exception: Throwable) {
            println(exception.toString()) //To change body of created functions use File | Settings | File Templates.
        }

    }
    block.startCoroutine(continuation)
}

fun coroutine01() {
    println("before coroutine")
    //启动我们的协程
    asyncCalcMd5("test.zip") {
        println("in coroutine. Before suspend.")
        //暂停我们的线程,并开始执行一段耗时操作
        val result: String = suspendCoroutine { continuation ->
            println("in suspend block.")
            continuation.resume(calcMd5(continuation.context[FilePath]!!.path))
            println("after resume.")
        }
        println("in coroutine. After suspend. result = $result")
    }
    println("after coroutine")
}

private val executor = Executors.newSingleThreadScheduledExecutor {
    Thread(it, "scheduler")
}

fun coroutine02() {
    asyncCalcMd5("test.zip1") {
        println("in coroutine. Before suspend.")
        //暂停我们的线程，并开始执行一段耗时操作
        val result: String = suspendCoroutine { continuation ->
            println("in suspend block.")
            executor.submit {
                continuation.resume(calcMd5(continuation.context[FilePath]!!.path))
                println("after resume.")
            }
        }
        println("in coroutine. After suspend. result = $result")
        executor.shutdown()
    }
}

public interface ContinuationInterceptor : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<ContinuationInterceptor>

    public fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T>
}

open class Pool(val pool: ForkJoinPool)
    : AbstractCoroutineContextElement(ContinuationInterceptor),
        ContinuationInterceptor {

    override fun <T> interceptContinuation(continuation: Continuation<T>)
            : Continuation<T> =
            PoolContinuation(pool,
                    //下面这段代码是要查找其他拦截器，并保证能调用它们的拦截方法
                    continuation.context.fold(continuation, { cont, element ->
                        if (element != this@Pool && element is ContinuationInterceptor)
                            element.interceptContinuation(cont) else cont
                    }))
}

private class PoolContinuation<T>(
        val pool: ForkJoinPool,
        val continuation: Continuation<T>
) : Continuation<T> by continuation {
    override fun resume(value: T) {
        if (isPoolThread()) continuation.resume(value)
        else pool.execute { continuation.resume(value) }
    }

    override fun resumeWithException(exception: Throwable) {
        if (isPoolThread()) continuation.resumeWithException(exception)
        else pool.execute { continuation.resumeWithException(exception) }
    }

    fun isPoolThread(): Boolean = (Thread.currentThread() as? ForkJoinWorkerThread)?.pool == pool
}

object CommonPool : Pool(ForkJoinPool.commonPool())

fun asyncCalcMd51(path: String, block: suspend () -> String) {

    val continuation = object : Continuation<String>{
        override val context: CoroutineContext
            get() = FilePath(path) + CommonPool //To change initializer of created properties use File | Settings | File Templates.

        override fun resume(value: String) {
            println("resume: $value") //To change body of created functions use File | Settings | File Templates.
        }

        override fun resumeWithException(exception: Throwable) {
            println(exception.toString())  //To change body of created functions use File | Settings | File Templates.
        }

    }

    block.startCoroutine(continuation)
}

class StandaloneCoroutine(override val context: CoroutineContext): Continuation<Unit> {
    override fun resume(value: Unit) {}

    override fun resumeWithException(exception: Throwable) {
        //处理异常
        val currentThread = Thread.currentThread()
        currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, exception)
    }
}

fun launch(context: CoroutineContext, block: suspend () -> Unit) =
        block.startCoroutine(StandaloneCoroutine(context))

fun coroutine03() {
    println("before coroutine")
    //启动我们的协程
    asyncCalcMd5("test.zip") {
    }
    println("after coroutine")
    //加这句的原因是防止程序在协程运行完之前停止
    CommonPool.pool.awaitTermination(10000, TimeUnit.MILLISECONDS)
}

fun coroutine04() {
    println("before coroutine")
    //启动我们的协程
    launch(FilePath("test.zip") + CommonPool) {
        println("in coroutine. Before suspend.")
        //暂停我们的线程，并开始执行一段耗时操作
        val result: String = suspendCoroutine {
            continuation ->
            println("in suspend block.")
            continuation.resume(calcMd5(continuation.context[FilePath]!!.path))
            println("after resume.")
        }
        println("in coroutine. After suspend. result = $result")
    }
    println("after coroutine")
    CommonPool.pool.awaitTermination(10000, TimeUnit.MILLISECONDS)
}

suspend fun <T> CompletableFuture<T>.await(): T {
    return suspendCoroutine {
        continuation ->
        whenComplete { result, e ->
            if (e == null) continuation.resume(result)
            else continuation.resumeWithException(e)
        }
    }
}

fun calcMd51(path: String): CompletableFuture<String> = CompletableFuture.supplyAsync {
    println("calc md5 for $path.")
    //暂时用这个模拟耗时
    Thread.sleep(1000)
    //假设这就是我们计算得到的 MD5 值
    System.currentTimeMillis().toString()
}

fun coroutine05() {
    println("before coroutine")
    //启动我们的协程
    val coroutineContext = FilePath("test.zip") + CommonPool
    launch(coroutineContext) {
        println("in coroutine. Before suspend.")
        //暂停我们的线程，并开始执行一段耗时操作
        val result: String = calcMd51(coroutineContext[FilePath]!!.path).await()
        println("in coroutine. After suspend. result = $result")
    }
    println("after coroutine")
    CommonPool.pool.awaitTermination(10, TimeUnit.SECONDS)
}

public fun <R, T> (suspend R.() -> T).startCoroutine(
        receiver: R,
        completion: Continuation<T>){

}

fun <T> launch(
        receiver: T,
        context: CoroutineContext,
        block: suspend T.() -> Unit)
        = block.startCoroutine(receiver, StandaloneCoroutine(context))

fun launchWithContext(
        context: CoroutineContext,
        block: suspend CoroutineContext.() -> Unit)
        = launch(context, context, block)

val coroutineContext = FilePath("test.zip") + CommonPool

fun  coroutine06(){
    //需要传入 receiver
    launch(coroutineContext, coroutineContext) {

        //注意下面直接用 this 来获取路径
        val result: String = calcMd51(this[FilePath]!!.path).await()

    }
}

fun coroutine07(){
    launchWithContext(FilePath("test.zip") + CommonPool) {
        println("in coroutine. Before suspend.")
        //暂停我们的线程，并开始执行一段耗时操作
        val result: String = calcMd51(this[FilePath]!!.path).await()
        println("in coroutine. After suspend. result = $result")
    }
    println("after coroutine")
    CommonPool.pool.awaitTermination(10, TimeUnit.SECONDS)
}

fun main(args: Array<String>) {


    test {
        println("after coroutine ${Thread.currentThread().name}")
    }
//    coroutine01()
    coroutine07()

}