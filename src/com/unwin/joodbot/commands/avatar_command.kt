package com.unwin.joodbot.commands

import de.btobastian.javacord.DiscordAPI
import de.btobastian.javacord.entities.User
import de.btobastian.javacord.entities.message.Message
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import java.io.File

/**
 * Created by unwin on 27-Mar-16.
 */
class avatar_command : CommandExecutor {

    @Command(aliases = arrayOf("\$avatar"), description = "Returns the avatar of user specified")
    fun onCommand(command: String, args: Array<String>, message: Message, api: DiscordAPI) {

        var pathName = "pics-temp/"
        var path = File(pathName)
        var word: String = message.content.substring(8, message.content.length).toLowerCase().trim()
        var filename: String = "${pathName}avatar-$word.jpg"
        var reply: String = ""
        try {
            if(path.isDirectory) {
                // Do nothing, dir exists
            } else {
                path.mkdir()
            }

            var avatar: ByteArray
            var file: File = File(filename)

            var users: Array<User> = api.users.toTypedArray()

            for (i in 0..users.size-1) {

                 if (word.equals(users[i].name.toLowerCase())) {

                    file.delete()
                    var temp = users[i].avatarAsByteArray
                    avatar = temp.get()
                    file.writeBytes(avatar)

                    reply = "$word's avatar:"
                }
            }

            if (file.length() < 1) {
                message.reply("Error: user has no set avatar")
            }
            else if (reply.length == 0) {
                message.reply("User not found")
            }
            else {
                message.reply(reply)
                var future_msg = message.replyFile(file)
                future_msg.get()
                if(future_msg.isDone) {
                    file.delete()
                }
            }
        } catch (ex: Exception) {
            message.reply("Error in avatar -- ${ex.message}")
        }
    }

}