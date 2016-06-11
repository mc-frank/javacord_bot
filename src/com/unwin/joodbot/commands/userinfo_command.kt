package com.unwin.joodbot.commands

import de.btobastian.javacord.DiscordAPI
import de.btobastian.javacord.entities.message.Message
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor

/**
 * Created by frank on 11/06/16.
 */
class userinfo_command : CommandExecutor {

        @Command(aliases = arrayOf("userinfo", "user-info", "avatar"), description = "Returns information of user")
        fun onCommand(command: String, args: Array<String>, message: Message, api: DiscordAPI): String {

                println("called")

                var username: String = ""
                var id: String = ""
                var descriminator: String = ""
                var avatar: String = ""

                var mentions = message.mentions

                if(mentions.size == 0) {
                        return ""
                }

                mentions.forEach {
                        username = it.name
                        id = it.id
                        descriminator = it.discriminator
                        avatar = it.avatarUrl.toString()
                }

                return "$username#$descriminator has an ID of @$id and their avatar is:\n$avatar"
        }

}
