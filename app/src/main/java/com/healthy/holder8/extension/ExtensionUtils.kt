package com.healthy.holder8.extension

fun<K,V> MutableMap<K,V>.toVarargArray(): Array<Pair<K,V>> =
    //mutablemap转化为pair
    map {
        Pair(it.key, it.value)
    }.toTypedArray()

