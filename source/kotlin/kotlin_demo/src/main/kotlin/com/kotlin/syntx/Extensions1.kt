package com.kotlin.syntx

/**
 * Created by zero on 2018/1/17.
 * Describe: function
 */

class D1 {
    private val name: String = "D1"
    val prop: String = "public D1"
    fun f(): Unit {
        println("D1 f")
    }
}

class C3 {
    open fun f(): Unit {
        println("C3 f")
    }

    fun D1.foo() {
        println("C3 D1.foo")
        println(prop)
        this@C3.f()
        f()
        this.f()
    }

    fun call(d: D1) {
        println("C3 call")
        d.foo()
    }
}

open class OD {}

class OD1 : OD() {}

open class OC {
    open fun OD.foo() {
        println("OD.foo in OC")
    }

    open fun OD1.foo() {
        println("OD1.foo in OC")
    }

    fun call(od: OD) {
        od.foo()
    }
}

class OC1 : OC() {
    override fun OD.foo() {
        println("OD.foo in OC1")
    }

    override fun OD1.foo() {
        println("OD1.foo in OC1")
    }
}


fun main(args: Array<String>) {
//    val c = C3()
//    c.call(D1())

    OC().call(OD())
    OC().call(OD1())
    OC1().call(OD())
    OC1().call(OD1())

}