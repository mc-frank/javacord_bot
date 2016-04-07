package com.unwin.joodbot.commands

import de.btobastian.javacord.entities.message.Message
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import java.io.File
import com.unwin.joodbot.log

/**
 * Created by unwin on 27-Mar-16.
 */
class dump_command : CommandExecutor {

    @Command(aliases = arrayOf("\$dump"), description = "Dumps the com.unwin.joodbot.log file")
    fun onCommand(command: String, args: Array<String>, message: Message) {
        var file = File(log()._LOG_FILENAME + "-" + message.channelReceiver.id + ".txt")
        message.replyFile(file)
    }

}