package com.unwin.joodbot.commands

import de.btobastian.javacord.entities.message.Message
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import com.unwin.joodbot.json_reader

/**
 * Created by unwin on 27-Mar-16.
 */
class addfuncs_command : CommandExecutor {

    @Command(aliases = arrayOf("\$add-funcs"), description = "Adds a function to the file")
    fun onCommand(command: String, args: Array<String>, message: Message) {
        var new_func = args[0]
        var new_action = args[1]

        var j_reader = json_reader()
        j_reader.read_json_config()

        j_reader.write_functions(new_func, new_action)

    }

}