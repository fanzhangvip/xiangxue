package com.kotlin.demo06

import kotlin.reflect.KProperty
import kotlin.properties.Delegates
import kotlin.properties.*
import kotlin.reflect.*
import kotlin.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.Annotation
import kotlin.concurrent.*
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl
import com.git.kotlin.demo05.Person

fun main(args: Array<String>) {
	
	val demo01 = Outer1.Nested().foo()
	
	val red = Color.valueOf("RED")
    val colors = Color.values()  
	
	
	println("red = $red")
	println("colors = $colors")
	
	val ab = object: A(1),B{
		override val y = 14
	}
	
	val adHoc = object{
		var x: Int = 0
		var y: Int = 0
	}
	
	val f = MyClass.create()
	
	val b = BaseImpl(10);//ʵ��ʵ����
	Derived(b).p(b)//Derivedί��b�����˼̳���Base(:Base by b)�������Derived�Լ�������print()����Ϊ
	
	
	println("name = $name")
	name = "fanzhang"
	println("name = $name")
	
	var stu: Student = Student()

    stu.no = 20
    stu.name = "wang"

    println(stu.toString()) // ��ӡ��no: 20 | name: wang

    stu.no = 30
    stu.name = "li"
    println(stu.toString()) // ��ӡ��no: 20 | name: li
	
	val user  = User(mapOf("name" to "fanzhang","age" to 29))
	println("user = $user")
	
	 try{
        var ui = MyUI()
        println(ui.image)
//        println(ui.text)
    }catch(e: Exception) {
        println(e.message)
    }
	
	with(stu){
		
	}
	
	var ints = listOf(1,2,3,4)
	var doubleList = ints.map { it -> it*2 }
	kotlin.lazy {  }
	kotlin.run { println("test") }
	
	val p = Person()
	
	println("p = $p")
	println("p = ${p.name}")
}



 
fun <T, R> List<T>.map(transform: (T) -> R): List<R> {  
  val result = arrayListOf<R>()  
  for (item in this)  
    result.add(transform(item))  
  return result  
}  

class Outer1{
	private val bar: Int = 1;
	class Nested {
		fun foo() = 2
	}
}


class Outer2{
	private val bar: Int = 1;
	
	inner class Inner{
		fun foo() = bar
	}
}

enum class Direction{
	NORTH,SOUTH,WEST
}

enum class Color(valrgb:Int){
	RED(0xFF0000),
	GREEN(0x00FF00),
	BLUE(0x0000FF)
}


enum class ProtocolState {
	WAITING {
		override fun signal() = Taking
	},
	Taking{
		override fun signal() = WAITING
	};//ע�����ö�ٶ������κγ�Ա������Ҫ���� java �������÷ֺ� ; ��ö�ٳ�������ͳ�Ա����ֿ�
	abstract fun signal(): ProtocolState
}


open class A(x: Int){
	public open val y: Int = x
}

interface B{
	
}


interface Factory<T>{
	fun create(): T
}

class MyClass{
	companion object: Factory<MyClass>{
		override fun create(): MyClass = MyClass()
	}
}

interface Base{
	 fun<T> p(t: T)
}

class BaseImpl(val x: Int) :Base{
	override fun<Int> p(x: Int){
		println("x = $x")
	}
}

class Derived(b: Base) : Base by b

val no: Int by lazy {
    200
}

public interface ReadWriteProperty1<in R, T> {

    public operator fun getValue(thisRef: R, property: KProperty<*>): T


    public operator fun setValue(thisRef: R, property: KProperty<*>, value: T)
}

public interface ReadOnlyProperty1<in R, out T> {


    public operator fun getValue(thisRef: R, property: KProperty<*>): T
}

class SingleValueVar<T> : ReadWriteProperty<Any?, T> {

    private var value: T? = null

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (null != value && null == this.value) {
            this.value = value
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value!!
    }

}

object DelegatesExt{
	fun <T> singleValueVar():ReadWriteProperty<Any?,T> = SingleValueVar<T>()
}


class Student{
	var no: Int by DelegatesExt.singleValueVar()
	
	var name: String = "wang"
	
	override fun toString(): String{
		return "no: $no | name: $name"
	}
}


var name: String by Delegates.observable("wang", {
    kProperty, oldName, newName ->
    println("kProperty��${kProperty.name} | oldName:$oldName | newName:$newName")
})

var name1: String by Delegates.vetoable("wang", {
    kProperty, oldValue, newValue ->
    println("oldValue��$oldValue | newValue:$newValue")
    newValue.contains("wang")
})

class Delegate {
	operator fun getValue(thisRef: Any?, prop: KProperty<*>): String {
		return "$thisRef, thank you for delegating '${prop.name}' to me !"
	}
	
	operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String) {
		println("$value has been assigned to '${prop.name} in $thisRef.'")
	}
}

class Example{
	var p: String by Delegate()
}

class User(val map: Map<String, Any?>){
	override fun equals(other: Any?): Boolean {
		return super.equals(other)
	}

	override fun hashCode(): Int {
		return super.hashCode()
	}

	override fun toString(): String {
		return "(name=$name,age=$age)"
	}

	val name: String by map
	val age: Int by map
	
}

class ResourceID() {
    val image_id: String = "101"
    val text_id: String = "102"
}

class ResourceLoader(id: ResourceID) {
    val d: ResourceID = id
    operator fun provideDelegate( thisRef: MyUI, prop: KProperty<*> ): ReadOnlyProperty<MyUI,String> {
		println("2")
        if(checkProperty(thisRef, prop.name)){
            return DellImpl(d)
        }else{
            throw Exception("Error ${prop.name}")
        }
    }

    private fun checkProperty(thisRef: MyUI, name: String):Boolean {
		println("3")
        if(name.equals("image") || name.equals("text")){
            return true
        }else{
            return false
        }
    }
}

class DellImpl(d: ResourceID) : ReadOnlyProperty<MyUI,String> {
	override fun getValue(thisRef: MyUI, property: KProperty<*>): String {
		println("4")
		if(property.name.equals("image"))
            return property.name+"  "+id.image_id
        else
            return property.name+"  "+id.text_id
	}

	val id: ResourceID = d
}

fun  bindResource(id: ResourceID) : ResourceLoader{
	println("1")
	val res = ResourceLoader(id)
	println("5")
	return res
} 

class MyUI {
    val image by bindResource(ResourceID())
//    val text by bindResource(ResourceID())
    //val webview by bindResource(ResourceID())
}









































































































