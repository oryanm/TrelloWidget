package com.github.oryanmat.trellowidget.util

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

import java.lang.reflect.Type

object Json {
    val gson: Gson = Gson()

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