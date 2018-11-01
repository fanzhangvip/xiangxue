package com.kotlin.demo07

fun main(args: Array<String>) {
	val p = Person()
	println("p = $p")
	val mulList = listOf(1, 2, 3)
	val obj = Obj()
	obj.age = -1
	println("obj=$obj,name =${obj.name},table = ${obj.table}")

	val child = Child1()
	println("child = ${child.foo()}")

	val list = mutableListOf(1,2,3)
	list.swap(0,2)


}

class Obj() {
	var age: Int = 0
		get() {
			return field
		}
		set(value) {
			if (value >= 0)
				field = value
		}

	private var _name: String = ""

	public val name: String
		get() {
			if (_name == null) {
				_name = ""
			}
			return _name
		}


	private var _table: Person? = null
	public val table: Person
		get() {
			if (_table == null) {
				_table = Person()
			}
			return _table ?: throw AssertionError("Set to null by another thread")
		}

	override fun toString(): String {
		return "Obj(name=$_name,age=$age)"
	}


}

class Child1() : MyInterface {
	override val prop: Int = 29
	override val property: String = "11Child foo"

//	override fun foo() {
//		println("child")
//	}
}

interface MyInterface {
	val prop: Int
	val property: String
		get() = "foo"

	fun foo() {
		println(property)
	}
}

fun <T> MutableList<T>.swap(index1: Int,index2: Int){
	val tmp = this[index1]
	this[index1] = this[index2]
	this[index2] = tmp
}

