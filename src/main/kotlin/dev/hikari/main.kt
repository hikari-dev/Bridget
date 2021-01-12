@file:JvmName("Main")

package dev.hikari

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.utils.BotConfiguration

@OptIn(ObsoleteCoroutinesApi::class)
fun main(): Unit = runBlocking {
    val bridget = BotFactory.newBot(BridgetConfig.config.qqBot.qq, BridgetConfig.config.qqBot.password) {
        fileBasedDeviceInfo()
        protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
    }.alsoLogin()

    bridget.eventChannel.subscribeAlways<GroupMessageEvent> { event ->
        if (group.id == BridgetConfig.config.telegramBot.qqGroup) {
            val msg = "${event.sender.nameCardOrNick}：${event.message.contentToString()}"
            Api.sendTelegramMessage(msg)
        }
    }

    val ticker = ticker(BridgetConfig.config.telegramBot.receiveInterval * 1000L, 0, mode = TickerMode.FIXED_DELAY)
    for (item in ticker) {
        val updates = kotlin.runCatching {
            Api.getTelegramMessage()
        }.onFailure {
            bridget.logger.error("sync telegram message error", it)
        }.getOrNull()
        if (updates.isNullOrEmpty()) continue
        for (update in updates) {
            if (update.message != null && update.message.chat?.id == BridgetConfig.config.telegramBot.telegramGroup) {
                bridget.getGroup(BridgetConfig.config.telegramBot.qqGroup)?.sendMessage(buildMessageChain {
                    +"${update.message.from?.firstName}${update.message.from?.lastName}："
                    +update.message.text
                })
            }
        }
    }

}