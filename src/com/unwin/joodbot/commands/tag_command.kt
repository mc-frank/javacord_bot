package com.unwin.joodbot.commands

import com.unwin.joodbot.json_reader
import de.btobastian.javacord.DiscordAPI
import de.btobastian.javacord.entities.message.Message
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor

/**
 * Created by unwin on 06-Jul-16.
 */
class tag_command : CommandExecutor {

    @Command(aliases = arrayOf("tag"), description = "Posts a tag")
    fun onCommand(command: String, args: Array<String>, api: DiscordAPI, message: Message): String {
        var jreader = json_reader()
        jreader.read_json_config()

        var argument = ""
        args.forEach { argument += it }
        argument = argument.removePrefix("${jreader.prefix}tag")

        println(argument)

        var a = 0
        jreader.functions.forEach {
            if(argument.equals("$it")) {
                return(jreader.actions[a])
            }
            ++a
        }
        return("No tag found")
    }

}
