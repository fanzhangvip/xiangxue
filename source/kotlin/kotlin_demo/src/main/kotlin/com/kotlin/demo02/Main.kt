package com.kotlin.demo02

import kotlin.*
import kotlin.io.*
import kotlin.concurrent.*
import java.io.*
import com.kotlin.demo02.Costomer
import com.google.gson.*


fun foo(a: Int = 0, b: String = "") {
    println("a= $a, b=$b")
}

fun String.spcaceToCamelCase(): String {
    return "Convert this to camelcase ${this}"
}

object Resource {
    val name = "Name"
}

inline fun <reified T : Any> Gson.fromJson(json: String): T = this.fromJson(json, T::class.java)

fun main(args: Array<String>) {
    val costomer = Costomer("fanzhang", "fan_0723@qq.com", 1)
    println("costomer is $costomer")
    val (name, email, id) = costomer
    println("name= $name, email= $email, id- $id")
    val costomer1 = Costomer()
    println("costomer1 is $costomer1")
    val costomer2 = Costomer("fanlin", "2145@163.com")
    println("costomer2 is $costomer2")
    costomer2.id = 3
    println("costomer2 changed id is $costomer2")
    val human = Human()
    human.sex = "��"
    println("human is $human")
    println()

    foo()
    foo(35, "35")
    println()

    val listfilter = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -2, -3, -4)
    val positivers = listfilter.filter { x -> x > 3 }
    println("positiver=$positivers")
    val positivers1 = listfilter.filter { it > 0 }
    println("positivers1=$positivers1")
    println()

    val names = "fanzhang"
    println("names $names")
    println()

    class Foo

    class Bar

    var x: Any
    x = Foo()
    when (x) {
        is Foo -> println("is Foo")
        is Bar -> println("is Bar")
        else -> println("Unkown")
    }
    println()

    val map = mapOf<String, String>("key" to "value", "fanzhang" to "wanghuajun")
    val bookA = Book("A", 20, "Jone")
    val bookB = Book("A", 21, "Green")
    val bookC = Book("C", 20, "Mark")
    val bookD = Book("D", 22, "Node")
    val bookE = Book("A", 20, "Blue")
    val mapb1 = mapOf(1 to bookA
            , 2 to bookB
            , 3 to bookC
            , 4 to bookD
            , 5 to bookE)

    var mapMutable = mutableMapOf<Int, Book>(1 to bookA
            , 2 to bookB
            , 3 to bookC
            , 4 to bookD
            , 5 to bookE)
    val mapPair = mapOf(Pair(1, bookA), Pair(2, bookB))
    val mapEmpty = emptyMap<Int, Book>()
    val hashMap = hashMapOf(1 to bookA, 2 to bookB)
    println("mapb1 = $mapb1")
    println("mapEmpty = $mapEmpty")
    val size = mapb1.size
    println("size is $size")

    val setKey = mapb1.keys
    setKey.forEach(::println)

    val setValues = mapb1.values
    setValues.forEach(::println)

    val setEntry = mapb1.entries
    setEntry.forEach {
        println("key: ${it.key},value: ${it.value}")
    }

    for ((k, v) in mapb1) {
        println("$k -> $v")
    }
    println()

    for (i in 0..10) {
        println("$i -> [$i]")
    }

    for (i in 0 until 10) {
        println("$i -> [$i]")
    }

    for (i in 0..10 step 2) {
        println("$i -> [$i]")
    }

    for (i in 10 downTo 0) {
        println("$i -> [$i]")
    }

    var list = listOf("a", 1, "b", 2, "c", 3)
    println("list=$list")
    println(mapb1[1])

    val p: String by lazy { "fanzhang" }
    val p1: String by lazy("fen") { "fanzhang" }


    println("fanzhang is ${"str".spcaceToCamelCase()}")


    val res = Resource.name
    println("Resource is $res")

    val files = File("d:/doc").listFiles()
    println("files size is ${files?.size ?: "empty"}")

    var data = mapOf("fanzhang" to "we", "emails" to "fanzhang@qq.com")
    val emails = data["emails"] ?: throw IllegalStateException("Email is missing!")
    println("emails = $emails")
    data = data.filter { false }
    data?.let {
        for ((k, v) in data) {
            println("$k -> $v")
        }
    }

    fun count(): Int {
        return 3
    }

    fun test() {
        val result = try {
            count()
        } catch (e: Exception) {
            throw Exception(e)
        }
        println("result = $result")
    }
    test()

    fun transform(color: String): Int {
        return when (color) {
            "Red" -> 0
            "Green" -> 1
            "Blue" -> 2
            else -> throw IllegalArgumentException("Invalid color param value")
        }
    }
    println("transform is ${transform("Red")}")

    fun foo(param: Int): String {
        val result = if (param == 1) {
            "one"
        } else if (param == 2) {
            "two"
        } else {
            "three"
        }
        return result
    }
    println("foo is ${foo(2)}")

    fun arrOfMinusOnes(size: Int): IntArray {
        return IntArray(size).apply { fill(-1) }
    }

    println("arrOfMinusOnes(3) = ${arrOfMinusOnes(3)}")

    fun theAnswer() = 43;
    fun theAnswers(): Int {
        return 32
    }


    class Turtle(var name: String = "Turtle") {
        fun penDown() = "----"
        fun penUp() = "|"
        fun turn(degrees: Double) {
            println(degrees)
        }

        fun forward(pixels: Double) {
            println(pixels)
        }

        var type = 0
    }

    val mytur = Turtle()
    with(mytur) {
        val a = penDown()
        val b = penUp()
        for (i in 1..4) {
            forward(100.0)
            turn(90.0)
        }
    }


    val b: Boolean? = false
    if (b == true) {

    } else {

    }


}


