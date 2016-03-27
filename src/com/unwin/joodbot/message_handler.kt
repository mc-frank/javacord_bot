package com.unwin.joodbot

import de.btobastian.javacord.*
import de.btobastian.javacord.entities.Channel
import de.btobastian.javacord.entities.Server
import de.btobastian.javacord.entities.User
import de.btobastian.javacord.entities.permissions.Role
import de.btobastian.javacord.entities.message.Message
import de.btobastian.javacord.listener.channel.ChannelChangeNameListener
import de.btobastian.javacord.listener.channel.ChannelChangeTopicListener
import de.btobastian.javacord.listener.channel.ChannelCreateListener
import de.btobastian.javacord.listener.message.MessageCreateListener
import de.btobastian.javacord.listener.message.MessageDeleteListener
import de.btobastian.javacord.listener.message.MessageEditListener
import de.btobastian.javacord.listener.message.TypingStartListener
import de.btobastian.javacord.listener.role.RoleCreateListener
import de.btobastian.javacord.listener.server.ServerChangeNameListener
import de.btobastian.javacord.listener.server.ServerJoinListener
import de.btobastian.javacord.listener.user.UserChangeNameListener
import kotlin.collections.forEach
import kotlin.concurrent.thread
import kotlin.text.*

/**
 * Created by unwin on 10/01/2016.
 */
class main_listener: MessageCreateListener, MessageEditListener, TypingStartListener,
        MessageDeleteListener, UserChangeNameListener, ServerJoinListener, ChannelChangeNameListener,
        ChannelChangeTopicListener, ServerChangeNameListener, RoleCreateListener,
        ChannelCreateListener
{

    val prefix = "$"

    override fun onMessageCreate(api: DiscordAPI, message: Message) {

        //Read config information from the .json file
        var jReader = json_reader()
        jReader.read_json_config()

        //Create an instance of a com.unwin.joodbot.log file
        var log: log = log()
        log.set_file_name(log._LOG_FILENAME)
        var logText = "[${message.channelReceiver.name}] ${message.author.name} > ${message.content}"

        if(message.isPrivateMessage) {
            return
        }

        //Only print to file and to console if message isn't a PM
        log.set_file_text( logText )
        log.write_file()
        println(logText)


        // Ignore if message comes from bot
        if(message.author.equals(api.yourself.name)) {
            return
        }


        // Respond to BotBT's penis functions
        if(message.author.equals("BotBT")) {
            if(message.content.contains("8") && message.content.contains("=") && message.content.contains("D")) {
                message.reply("( ͡° ͜ʖ ͡°)")
            }
        }

        thread() {
            // Reply with to a function in the file with the corresponding action
            var a = 0
            jReader.functions.forEach {
                if (message.content.equals("$prefix$it")) {
                    message.reply(jReader.actions[a])
                }
                ++a
            }


        }
    }

    override fun onMessageEdit(api: DiscordAPI, message: Message, oldMessage: String) {
        var usr = message.author
        var originalMessage = oldMessage
        var newMessage = message
        var channel = message.channelReceiver

        println("[$channel] $usr > EDITED MESSAGED $originalMessage [to] $newMessage")
        var log = log()
        log.set_file_name(log._LOG_FILENAME)
        log.set_file_text("[$channel] $usr > EDITED MESSAGED $originalMessage [to] $newMessage")
    }

    override fun onTypingStart(api: DiscordAPI, user: User, channel: Channel?) {
        println("${user.name} is typing in ${channel?.name}")
    }

    override fun onMessageDelete(api: DiscordAPI, message: Message) {
        var msg: String = message.content
        var user: String = message.author.name
        var channel: String? = message.channelReceiver.name

        var log: log = log()
        log.set_file_name(log._LOG_FILENAME)
        log.set_file_text( ("[$channel] $user's message was deleted: $msg") )
        log.write_file()

        println("[$channel] $user's message was deleted: $msg")
    }

    override fun onUserChangeName(api: DiscordAPI, user: User, oldName: String) {
        var logText = "[User Changed Name] $oldName changed their name to ${user.name}"
        var log: log = log()
        log.set_file_name(log._LOG_FILENAME)
        log.set_file_text(logText)
        log.write_file()

        println(logText)
    }

    override fun onServerJoin(api: DiscordAPI, server: Server) {

    }

    override fun onChannelChangeName(api: DiscordAPI, channel: Channel, oldName: String) {
        var logText = "[Channel Name Change] $oldName channel's name changed to ${channel.name}"
        var log = log()
        log.set_file_name(log._LOG_FILENAME)
        log.set_file_text(logText)
        log.write_file()
        println(logText)
    }

    override fun onChannelChangeTopic(api: DiscordAPI, channel: Channel, oldTopic: String) {
        var logText = "[Channel Changed Topic] ${channel.name}'s topic changed from $oldTopic to ${channel.topic}"
        var log = log()
        log.set_file_name(log._LOG_FILENAME)
        log.set_file_text(logText)
        log.write_file()

        println(logText)
    }

    override fun onChannelCreate(api: DiscordAPI, channel: Channel) {
        var logText = "${channel.name} created"
        var log = log()
        log.set_file_name(log._LOG_FILENAME)
        log.set_file_text(logText)
        println(logText)
        log.write_file()
    }

    override fun onServerChangeName(api: DiscordAPI, server: Server, name: String) {
        var logText = "[SERVER CHANGE] $name name changed to ${server.name}"
        var log = log()
        log.set_file_name(log._LOG_FILENAME)
        log.set_file_text(logText)
        log.write_file()

        println(logText)
    }

    override fun onRoleCreate(api: DiscordAPI, role: Role) {
        var logText = "${role.name} created"
        var log = log()
        log.set_file_name(log._LOG_FILENAME)
        log.set_file_text(logText)
        log.write_file()

        println(logText)
    }

}