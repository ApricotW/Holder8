package com.healthy.holder8.model

data class Account(val map:MutableMap<String,Any?>){
    val ID by map
    val password by map
    val beat by map
    val bpressure by map
    val btemperature by map
    val boxy by map
}