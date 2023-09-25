package com.github.oryanmat.trellowidget.util.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.oryanmat.trellowidget.util.Json
import java.lang.reflect.Type

class VolleyUtil(appContext: Context) {

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(appContext.applicationContext)
    }

    private fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }

    fun <T> getSynchronously(url: String, type: Type, defaultValue: T): DataStatus<T> {

        val future = RequestFuture.newFuture<String>()
        addToRequestQueue(StringRequest(Request.Method.GET, url, future, future))

        return try {
            val json = future.get()
            val data = Json.tryParseJson(json, type, defaultValue)
            DataStatus.success(data)
        } catch (e: Exception) {
            val msg = "HTTP request to Trello failed: ${e.stackTraceToString()}"
            DataStatus.error(msg)
        }
    }

    fun <T> getAsynchronously(
        url: String,
        type: Type,
        defaultValue: T,
        listener: (DataStatus<T>) -> Unit
    ) {
        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val data = Json.tryParseJson(response, type, defaultValue)
                val dataStatus = DataStatus.success(data)
                listener(dataStatus)
            },
            { error ->
                val dataStatus: DataStatus<T> = DataStatus.error(error.toString())
                listener(dataStatus)
            }
        )
        addToRequestQueue(request)
    }
}