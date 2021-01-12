package dev.hikari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelegramRsp(
    val ok: Boolean,
    val result: List<Update>
) {
    @Serializable
    data class Update(
        //The update's unique identifier
        @SerialName("update_id")
        val updateId: Int,
        //Optional. New incoming message of any kind — text, photo, sticker, etc.
        val message: Message? = null,
        @SerialName("edited_message")
        //Optional. New version of a message that is known to the bot and was edited
        val editedMessage: Message? = null,
        @SerialName("channel_post")
        //Optional. New incoming channel post of any kind — text, photo, sticker, etc.
        val channelPost: Message? = null,
        @SerialName("edited_channel_post")
        //Optional. New version of a channel post that is known to the bot and was edited
        val editedChannelPost: Message? = null
    )

    @Serializable
    data class Message(
        @SerialName("message_id")
        //Unique message identifier inside this chat
        val messageId: Int,
        val date: Int,
        //Optional. Sender, empty for messages sent to channels
        val from: From? = null,
        //Optional. Sender of the message, sent on behalf of a chat.
        val chat: Chat? = null,
        val text: String = ""
    )

    @Serializable
    data class From(
        //Unique identifier for this user or bot
        val id: Int,
        @SerialName("is_bot")
        //True, if this user is a bot
        val isBot: Boolean = false,
        @SerialName("first_name")
        val firstName: String = "",
        @SerialName("last_name")
        val lastName: String = "",
        val username: String = ""
    )

    @Serializable
    data class Chat(
        //Unique identifier for this chat
        val id: Int,
        //Type of chat, can be either “private”, “group”, “supergroup” or “channel”
        val type: String = "",
        //Optional. Title, for supergroups, channels and group chats
        val title: String = "",
        //Optional. Username, for private chats, supergroups and channels if available
        val username: String = "",
        val firstName: String = "",
        val lastName: String = ""
    )
}


