package com.unwin.joodbot.commands

import de.btobastian.javacord.DiscordAPI
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor

/**
 * Created by unwin on 27-Mar-16.
 */
class stop_command : CommandExecutor {

    @Command(aliases = arrayOf("stop"), description = "Stops the bot")
    fun onCommand(command: String, args: Array<String>, api: DiscordAPI) {
        System.exit(-1)
    }

}