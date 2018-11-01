package com.kotlin.demo04


fun main(args: Array<String>) {

    val invoice = Invoice()
    val empty = Empty()
    val person = Persondata()
    val person1 = Persondata("romg")
    println("$invoice, $empty, $person, $person1")
    val customer = Customer("fanzhang")
    println("$customer")

    val c = C()
    println("c is ${c.f()}")

}


class Invoice() {}

class Empty

data class Persondata constructor(val firstName: String = "firstName") {}

class Customer(name: String) {
    init {
        println("Customer initialized with value ${name}")
//		val logger = KotlinLogging.logger("Zero");
//		logger.info{"Customer initialized with value ${name}"}
    }
}

class Person(val firstName: String, val lastName: String, var age: Int) {}

class Cus public constructor(name: String) {}

class Persons {
    constructor(parent: Persons) {
    }
}

open class Outer {
    private val a = 1
    protected open val b = 2
    internal val c = 3
    val d = 4

    protected class Nested {
        public val e: Int = 5
    }
}

open class Base(p: Int)

class Derived(p: Int) : Base(p)

class Context
class Attr

open class View(ctx: Context)

class MyView : View {
    constructor(ctx: Context) : super(ctx) {

    }

    constructor(ctx: Context, attrs: Attr) : super(ctx) {

    }
}


open class Base1 {
    open fun v() {}
    fun nv() {}
}

class Der() : Base1() {
    override fun v() {}
}

open class AnotherDer() : Base1() {
    final override fun v() {}
}

open class Foo {
    open val x: Int = 0
}

open class A {
    open fun f() {
        println("A")
    }

    fun a() {
        println("a")
    }
}

interface B {
    fun f() {
        println("B")
    }

    fun b() {
        println("b")
    }
}

class C() : A(), B {
    override fun f() {
        super<A>.f()
        super<B>.f()
    }
}

open class Base2 {
    open fun f() {}
}

abstract class Der2 : Base2() {
    override abstract fun f()
}

class MyClass {
    companion object Factory {
        fun create(): MyClass = MyClass()
    }

    val instance = MyClass.create()
}

fun test() {
    val myclass = MyClass.create()
}

sealed class Expr {
    class Const(val number: Double) : Expr()
    class Sum(val e1: Expr, val e2: Expr) : Expr()
    object NotANumber : Expr()

    fun eval(expr: Expr): Double = when (expr) {
        is Const -> expr.number
        is Sum -> eval(expr.e1) + eval(expr.e2)
        NotANumber -> Double.NaN
    // the `else` clause is not required because we've covered all the cases
    }
}


interface MyInterface {
    val pro: Int
    val proWithInit: String
        get() = "foo"

    fun foo() {
        println(pro)
    }
}

class Child : MyInterface {
    override val pro: Int = 90
}

class Baz

fun Baz.goo() {
    println("Baz.goo()")
}


























