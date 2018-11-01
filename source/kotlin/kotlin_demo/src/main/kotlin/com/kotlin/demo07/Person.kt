package com.kotlin.demo07

class Person(val name: String = "fanzhang", val gender: Gender = Gender.FEMALE, age: Int = 20) {
	override fun toString(): String {
		return "Person(name=$name,gender=$gender,age=$age)"
	}

	val age = age
        get() = when (gender) {
            Gender.FEMALE -> (field * 0.8).toInt()
            Gender.MALE -> field
        }
	
	
}

enum class Gender {
    MALE, FEMALE
}