package com.unwin.joodbot.commands

import de.btobastian.javacord.DiscordAPI
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean

/**
 * Created by unwin on 27-Mar-16.
 */
class info_command : CommandExecutor {

    @Command(aliases = arrayOf("info"), description = "Returns information about the bot")
    fun onCommand(command: String, args: Array<String>, api: DiscordAPI): String {
        var runtime: Runtime = Runtime.getRuntime()
        var rb: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()
        var uptime = rb.uptime / 1000
        var details: String = "CPU(s) -- " + runtime.availableProcessors() +
                "\nOS -- " + System.getProperty("os.name") +
                "\nFree memory to JVM -- " + runtime.freeMemory() / (1024*1024) + " MB " +
                "\nUptime -- " + (uptime/60) + " minutes (" + uptime/60/60 + " hours)" +
                "\nReconnect enabled -- " + api.isAutoReconnectEnabled
        //message.reply(details)
        return details
    }

}