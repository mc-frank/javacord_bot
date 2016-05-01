package com.unwin.joodbot.commands

import de.btobastian.javacord.DiscordAPI
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor

/**
 * Created by unwin on 27-Mar-16.
 */
class reconnect_command : CommandExecutor {

    @Command(aliases = arrayOf("reconnect"), description = "Reconnects the bot to the server")
    fun onCommand(command: String, args: Array<String>, api: DiscordAPI): String {

        api.setAutoReconnect(false)
        api.reconnectBlocking()
        api.setAutoReconnect(true)

        return "```Reconnected```"
    }

}