package com.kotlin.syntx

class Aa{
    inner class Bb{
        fun p(){
            println("this = $this")
            666.foo()
        }

        fun Int.foo() { //隐式标签 @foo

            //输出A@2ff4acd0, this代指[A类对象]
            println(this@Aa)

            //输出A$B@279f2327, this代指[B类对象]
            println(this@Bb)

            //输出666, this代指[foo函数接收者Int类对象]
            println(this)

            //输出666, this@foo代指[foo函数接收者Int类对象]
            println(this@foo)

            val funLit = fun String.() {
                //this代指[funLit函数接收者String类对象]
                println(this) //输出lit
            }
            "lit".funLit()

            val funLit2 = { s: String ->
                //该函数没有接收者,故this代指[foo函数接收者Int类对象]
                println(this) //输出666
            }
            funLit2("lit2")
        }
    }
}

fun main(args: Array<String>) {
    Aa().Bb().p()
}