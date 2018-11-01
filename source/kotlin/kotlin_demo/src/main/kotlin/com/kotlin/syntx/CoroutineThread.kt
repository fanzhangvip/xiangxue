package com.kotlin.syntx

import kotlinx.coroutines.experimental.*

suspend fun doSomething(): String {
    println("doSomethine")
    return "dosome"
}

fun <T> asyncs(block: suspend () -> T) {

}

fun main(args: Array<String>) {
//    async {
//        val result =doSomething()
//        println("result = $result")
//    }
//
//    async(){
//        doSomething()
//    }
//
//    val fibonacciSeq = buildSequence {
//        var a = 0
//        var b = 1
//        yield(1) //惰性生产1
//        while (true) {
//            yield(a + b)//惰性生产斐波那契数列
//            val tmp = a + b
//            a = b
//            b = tmp
//        }
//    }
//    //输出斐波纳契数列(Fibonacci) [1, 1, 2, 3, 5, 8, 13, 21]
//    println(fibonacciSeq.take(80).toList())
//    launch(CommonPool) {
//        repeat(10){
//            println("i = $it")
//            delay(1000L)
//        }
//        for (i in 1..10) {
//            println("i = $i")
//            delay(1000L)
//        }
//    }
//    launch (CommonPool) {
//        delay(2000L)
//        println("Hello")
//    }
//    println("World")
//    Thread.sleep(3000L)
//    runBlocking<Unit> {
//        println("T0")
//        launch(CommonPool)  {
//            println("T1")
//            delay(3000L)
//            println("T1 Hellow")
//        }
//        println("T3 World")
//        delay(5000L)
//        println("T4")
//    }
    runBlocking {
       val job = launch{
            repeat(10) {
                i -> println("job sleeping $i ... CurrentThread: ${Thread.currentThread()}")
                delay(500L)
            }

//           var nextTime = 0L
//           var i = 0
//           var j = 0
//           while (i < 20) {
//               var currentTime = System.currentTimeMillis();
////               println("while ${j++}")
//               if (currentTime >= nextTime) {
//                   println("job sleeping ${i++} ... CurrentThread: ${Thread.currentThread()}")
//                   nextTime = currentTime + 500L
//               }
//           }
        }
//        delay(1900L)
//        println("Job is alive: ${job.isActive}; Job iscompleted: ${job.isCompleted}")
//        val b1 = job.cancel()
//        println("job cancel: $b1")
//        delay(1300L)
//        println("Job is alive: ${job.isActive}; Job iscompleted: ${job.isCompleted}")
//        delay(30000L)
//        val b2 = job.cancel()
//        println("job cancel: $b2")
//        println("Job is alive: ${job.isActive}; Job iscompleted: ${job.isCompleted}")
//        job.join()
        println("job is completed ${job.isCompleted}")
    }

    runBlocking {
        var c1 = launch(CommonPool) {
            delay(1000L)
            println("Coroutine 1")
        }
        var c2 = launch {
            delay(1000L)
            println("Coroutine 2")
        }
        c2.join() // 2
//        c1.join() // 1
        println("the main")
    }

    runBlocking {
        val one = async(coroutineContext) {  job1() }
        val two = async(coroutineContext) { job2() }
        println("sum = ${one.await() + two.await()}")
    }

    runBlocking {
        val jobs = arrayListOf<Job>()
        jobs += async(Unconfined) {  // 1
            println("Unconfined is worked in ${Thread.currentThread()}")
        }
        jobs += async(coroutineContext) { // 2
            println("coroutineContext is worked in ${Thread.currentThread()}")
        }
        jobs += async(CommonPool) {  // 3
            println("CommonPool is worked in ${Thread.currentThread()}")
        }
        jobs += async(newSingleThreadContext("newThread")) { // 3
            println("newThread is worked in ${Thread.currentThread()}")
        }
        jobs.forEach { it.join() }

        run {
            delay(1000L)
            println("run")
        }
    }



}

suspend fun job1(): Int{
    delay(3000L)
    return 1
}

suspend fun job2(): Int{
    delay(5000L)
    return 2
}