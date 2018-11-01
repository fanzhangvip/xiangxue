package com.kotlin.syntx

import java.util.*

fun demo(source: List<Int>) {
    val list = ArrayList<Int>()

    for (item in source) {
        list.add(item)
    }
    for (i in 0..source.size - 1) {
        list[i] = source[i]
    }
}

fun calendarDemo() {
    val calendar = Calendar.getInstance()

    if (calendar.firstDayOfWeek == Calendar.SUNDAY) {
        calendar.firstDayOfWeek = Calendar.MONDAY
    }
}

fun voidDemo() {
    val v = KotlinJava.vid()
    println("v = $v")
}
