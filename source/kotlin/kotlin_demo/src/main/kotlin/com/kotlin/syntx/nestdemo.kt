package com.kotlin.syntx



class MyOuter{
    val outerVal = 10
    class MyInner{
        fun innerFun()={
            //不能访问外部类的成员属性
//            println("my Inner innerFun ${outerVal}")
            println("my Inner")
        }
    }
}

class MyOuter1{
    val outerVal1 = 10
    inner class MyInner1{
        fun innerFun()={
            //可以访问外部类的成员属性
            println("my Inner innerFun ${outerVal1}")
            println("my Inner")
        }
    }
}

data class Fooo(val bars: MutableList<String>)

data class Persons(val name:String,val age: Int)


fun main(args: Array<String>) {

    //创建对象不需要外部类对象
    var inner = MyOuter.MyInner()
    inner.innerFun()

    val bars = mutableListOf("foobar", "wombar")
    val foo0 = Fooo(bars)
    val foo1 = foo0.copy()
    bars.add("oops")
    //bar 只是复制了引用
    println(foo1.bars.joinToString())

    val p1 = Persons("fanzhang",10)
    val p2 = Persons("zero",13)
    println("-----封装好了toString-------")
    println("p1: $p1")
    println("-----封装好了hashCode-------")
    println("p1 == p2: ${p1 == p2}")
    println("p1 === p2: ${p1 === p2}")
    println("-----封装好了copy()-------")
    val p3 = p1.copy()
    println("p3: $p3")
    println("-----封装好了copy(修改值)-------")
    val p4 = p1.copy("樊章")
    println("p4: $p4")
    println("-----封装好了componentx-------")
    val (name,age) = p4
    println("name:$name,age:$age")
}