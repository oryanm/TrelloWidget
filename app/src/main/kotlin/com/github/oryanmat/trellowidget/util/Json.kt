package com.github.oryanmat.trellowidget.util

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

import java.lang.reflect.Type

object Json {
    private val gson = Gson()

    fun toJson(src: Any): String = gson.toJson(src)

    fun <T> fromJson(json: String, c: Class<T>): T = gson.fromJson(json, c)

    fun <T> tryParseJson(json: String, c: Class<T>, defaultValue: T): T = try {
        gson.fromJson(json, c)
    } catch (e: JsonSyntaxException) {
        defaultValue
    }

    fun <T> tryParseJson(json: String, type: Type, defaultValue: T): T = try {
        gson.fromJson<T>(json, type)
    } catch (e: JsonSyntaxException) {
        defaultValue
    }
}