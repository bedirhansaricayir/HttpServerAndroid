package com.bilmsoft.httpserver.util.extension

import com.sun.net.httpserver.HttpExchange
import java.io.OutputStream

fun HttpExchange.sendResponse(responseText: String?) {
    responseText ?: return
    sendResponseHeaders(200, responseText.length.toLong())
    val outputStream: OutputStream = responseBody
    outputStream.write(responseText.toByteArray())
    outputStream.close()
}