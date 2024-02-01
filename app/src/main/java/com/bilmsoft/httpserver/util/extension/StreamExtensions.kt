package com.bilmsoft.httpserver.util.extension

import java.io.InputStream
import java.util.Scanner

fun InputStream.streamToString(): String {
    val scanner = Scanner(this).useDelimiter("\\A")
    return if (scanner.hasNext()) scanner.next() else ""
}