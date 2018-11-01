package com.kotlin.syntx

class Person(name: String, age: Int) {
    var name = name
    var age = age

    fun printInfo() = {
        println("我是$name,我今年$age")
    }
}

class Person1(var name: String = "John Deo", var age: Int = 23) {

    fun printInfo() = {
        println("我是$name,我今年$age")
    }
}

class Person2 {

    var name: String = ""
    var age: Int = 0

    constructor(name:String,age:Int){
        this.name = name;
        this.age = age
    }

    fun printInfo() {
        println("我是$name,我今年$age")
    }
}

class Person3(var name:String = "fanzhang") {//主构造函数

    init {
        println("我是初始化方法")
    }

    var age: Int = 0

    constructor(name:String = "zero",age:Int = 30):this(name){//次构造函数
        // 注意次构函数不能像主构函数那样，通过加上var或者val修饰符，让方法参数变成类的成员属性，次构函数只能接收值
        this.age = age
        println("我是次构造函数")
    }

    fun printInfo() :Unit {
        println("我是$name,我今年${age}岁")
    }
}



fun main(args: Array<String>) {
    run{
        val p3 = Person3("小乌龟",1)
        p3.printInfo()
        0
    }
    val p3 = Person3()
    p3.printInfo()

    val p31 = Person3(age = 30)//命名参数
    p31.printInfo()

}
