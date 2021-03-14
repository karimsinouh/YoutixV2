package com.karimsinouh.youtixv2.data

data class Result<T>(
        val isSuccessful:Boolean,
        val data:T?=null,
        val message:String?=null
)