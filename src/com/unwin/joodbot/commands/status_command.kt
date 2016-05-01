package com.unwin.joodbot.commands

import de.btobastian.javacord.DiscordAPI
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor

/**
 * Created by unwin on 27-Mar-16.
 */
class status_command : CommandExecutor {

    @Command(aliases = arrayOf("status"), description = "Changes the game the bot is playing")
    fun onCommand(command: String, args: Array<String>, api: DiscordAPI) {
        var new_status = ""
        args.forEach { new_status += " $it" }
        api.game = new_status
    }

}