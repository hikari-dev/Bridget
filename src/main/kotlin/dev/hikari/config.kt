package dev.hikari

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.Serializable
import java.io.File

object BridgetConfig {
    val config by lazy {
        val configStr = File("config.yml").readText()
        Yaml(configuration = YamlConfiguration(strictMode = false)).decodeFromString(Config.serializer(), configStr)
    }
}

@Serializable
data class Config(
    val qqBot: QQBotConfig,
    val telegramBot: TelegramBotConfig,
    val proxy: Proxy
) {

    @Serializable
    data class QQBotConfig(
        val qq: Long,
        val password: String
    )

    @Serializable
    data class TelegramBotConfig(
        val token: String,
        val qqGroup: Long,
        val telegramGroup: Int,
        val receiveInterval: Int
    )

    @Serializable
    data class Proxy(
        val hostname: String?,
        val port: Int?,
        val type: Int?
    )
}
