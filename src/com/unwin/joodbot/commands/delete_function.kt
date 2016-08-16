package com.unwin.joodbot.commands

import com.unwin.joodbot.json_reader
import de.btobastian.javacord.entities.message.Message
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor

/**
 * Created by unwin on 15/08/2016.
 */
class delete_function : CommandExecutor {

    @Command(aliases = arrayOf("delete_function", "delete_funcs", "delet_this"), description = "Deletes a function")
    fun onCommand(command: String, args: Array<String>, message: Message) {
        var function = args[0]

        var jsonReader = json_reader()
        jsonReader.read_json_config()
        jsonReader.delete_function(function)
        message.reply("```Deleted $function```")
    }

}