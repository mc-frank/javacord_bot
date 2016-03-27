package com.unwin.joodbot.commands

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import com.unwin.joodbot.log

/**
 * Created by unwin on 27-Mar-16.
 */
class filter_command : CommandExecutor {

    @Command(aliases = arrayOf("\$filter"), description = "Filters text in the com.unwin.joodbot.log file")
    fun onCommand(command: String, args: Array<String>): String {
        var word = args[0].toLowerCase().trim()

        var count = log().filter_text(word)
        return "$word has been mentioned in $count messages"
    }

}