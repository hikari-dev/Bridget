package dev.hikari

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit


object Api {

    private val httpClient = HttpClient(OkHttp) {
        engine {
            config {
                connectTimeout(8, TimeUnit.SECONDS)
                readTimeout(8, TimeUnit.SECONDS)

                val proxyHostName = BridgetConfig.config.proxy.hostname
                val proxyPort = BridgetConfig.config.proxy.port
                val type = BridgetConfig.config.proxy.type
                if (proxyHostName != null && proxyPort != null && type != null) {
                    when (type) {
                        0 -> {
                            proxy(Proxy(Proxy.Type.DIRECT, InetSocketAddress(proxyHostName, proxyPort)))
                        }
                        1 -> {
                            proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHostName, proxyPort)))
                        }
                        2 -> {
                            proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress(proxyHostName, proxyPort)))
                        }
                    }
                }
            }
        }
    }
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    private var offset = 0

    suspend fun getTelegramMessage(): List<TelegramRsp.Update> {
        val rspStr =
            httpClient.get<String>("https://api.telegram.org/bot${BridgetConfig.config.telegramBot.token}/getUpdates?offset=${if (offset > 0) offset else ""}")
        val rsp = json.decodeFromString(TelegramRsp.serializer(), rspStr)
        if (rsp.result.isNullOrEmpty()) return emptyList()
        offset = rsp.result.last().updateId + 1
        return rsp.result
    }

    suspend fun sendTelegramMessage(message: String) {
        httpClient.get<String>("https://api.telegram.org/bot${BridgetConfig.config.telegramBot.token}/sendMessage?chat_id=${BridgetConfig.config.telegramBot.telegramGroup}&text=$message")
    }
}
