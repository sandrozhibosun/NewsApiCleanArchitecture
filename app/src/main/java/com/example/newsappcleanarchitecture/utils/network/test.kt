package com.example.newsappcleanarchitecture.utils.network

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

fun main(): Unit = runBlocking {
    async {
        repeat(5) { i ->
            // Print from the first coroutine
            println("Coroutine 1: $i")
            yield() // Give other coroutines a chance to run
        }
        val char  = "123"
        for (c in char){
            c.isUpperCase()
        }

    }

    async {
        repeat(5) { i ->
            // Print from the second coroutine
            println("Coroutine 2: $i")
            yield() // Give other coroutines a chance to run
        }
    }
}



fun removeCap(data: String): String{
    data.replace("[A,E,I,O,U]".toRegex(),"")
    return data
}