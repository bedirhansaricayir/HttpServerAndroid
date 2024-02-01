package com.bilmsoft.httpserver.ui.main

import android.os.Bundle
import com.bilmsoft.httpserver.R
import com.bilmsoft.httpserver.data.model.HttpRequestType
import com.bilmsoft.httpserver.databinding.ActivityMainBinding
import com.bilmsoft.httpserver.ui.base.BaseActivity
import com.bilmsoft.httpserver.util.extension.sendResponse
import com.bilmsoft.httpserver.util.extension.streamToString
import com.bilmsoft.httpserver.util.extension.viewBinding
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.concurrent.Executors


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private var serverUp = false
    private var mHttpServer: HttpServer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.serverButton.setOnClickListener {
            serverUp = if (!serverUp) {
                startServer()
                true
            } else {
                stopServer()
                false
            }
        }

        printAllLocalIpAddress()
    }

    private fun startServer(port: Int = 5000) {
        try {
            mHttpServer = HttpServer.create(InetSocketAddress(port), 0).apply {
                executor = Executors.newCachedThreadPool()
                createContext("/", rootHandler)
                createContext("/index", rootHandler)
                // Handle /messages endpoint
                createContext("/messages", messageHandler)
                start() //startServer server
            }
            binding.serverTextView.text = getString(R.string.server_running)
            binding.serverButton.text = getString(R.string.stop_server)
        } catch (e: IOException) {
            e.printStackTrace()
            mHttpServer = null
        }
    }

    private fun stopServer() {
        if (mHttpServer != null) {
            mHttpServer?.stop(0)
            binding.serverTextView.text = getString(R.string.server_down)
            binding.serverButton.text = getString(R.string.start_server)
        }
    }


    // Handler for root endpoint
    private val rootHandler = HttpHandler { exchange ->
        run {
            toLog(exchange.remoteAddress.hostName)
            // Get request method
            when (exchange.requestMethod) {
                HttpRequestType.GET.value -> {
                    exchange.sendResponse("Welcome to my server2")
                }
            }
        }
    }

    private fun printAllLocalIpAddress() {
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val nextInterface = networkInterfaces.nextElement()
                val enumIpAddress = nextInterface.inetAddresses
                while (enumIpAddress.hasMoreElements()) {
                    val address = enumIpAddress.nextElement()
                    if (!address.isLoopbackAddress) {
                        val text = "interface: $nextInterface, " +
                                "address: $address, " +
                                "hostAddress: ${address.hostAddress}"
                        toLog(text)
                    }
                }
            }
        } catch (ex: SocketException) {
            toLog(ex.toString())
            ex.printStackTrace()
        }
    }

    private val messageHandler = HttpHandler { httpExchange ->
        run {
            when (httpExchange!!.requestMethod) {
                HttpRequestType.GET.value -> {
                    // Get all messages
                    httpExchange.sendResponse("Would be all messages stringified json")
                }

                HttpRequestType.POST.value -> {
                    val inputStream: InputStream = httpExchange.requestBody

                    val requestBody = inputStream.streamToString()
                    val jsonBody = JSONObject(requestBody)
                    // save message to database

                    //for testing
                    httpExchange.sendResponse(jsonBody.toString())
                }
            }
        }
    }

    private fun toLog(text: String?) {
        Timber.tag("httpServerLog").d("toLog: $text")
    }
}