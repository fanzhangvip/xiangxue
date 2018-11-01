package com.kotlin.syntx

import kotlin.properties.Delegates


val lazyValue: String by lazy {
    println("lazyValue进行初始化")
    "hello"
}

val normalValue: String = "Kotlin"

var userName: String by Delegates.observable("黑马程序员") { prop, old, new ->
    println("------------属性变化事件的相应器被触发")
    println("属性的名字: ${prop.name}")
    println("属性的旧值: ${old}")
    println("属性的新值: ${new}")
}


var password: String by Delegates.vetoable("zero123456") { prop, old, new ->
    println("------------属性变化事件的相应器被触发")
    println("属性的名字: ${prop.name}")
    println("属性的旧值: ${old}")
    println("属性的新值: ${new}")
    //如果设置的值包含"zero",则返回true,就会赋值成功,否则返回false,不会赋值
    new.contains("zero")
}

class User() {
    init {
        println("初始化方法...")
    }

    constructor(map: MutableMap<String, Any?>) :this(){
        var userName: String by map
        var age: Int by map
    }

    var userName: String by Delegates.notNull<String>()
    var age: Int
        get() = age
        set(value) {
            value
        }

    override fun toString(): String {
        return "{userName:${userName},age:${age}}"
    }
}

fun main(args: Array<String>) {

    println(normalValue)
    println(lazyValue)
    println("=========")
    println(lazyValue)

    println("=========")
    println("userName的初始值: ${userName}")
    userName = "heima"
    userName = "itheima"


    println("=========")
    password = "654321"
    println("password当前的属性值: ${password}")

    password = "654zero321"
    println("password当前的属性值: ${password}")


    var map: MutableMap<String, Any?> = mutableMapOf(
            "userName" to "John Doe",
            "age" to 24
    )
    val user1 = User(map)
    user1.userName = "fanzhang"
    user1.age = 23
    println("user1: $user1")
    println("=========")


    val user = User()
    if (user.userName != null) {//没赋值 使用会报错
//        println("user.userName: ${user.userName}")
    }
}