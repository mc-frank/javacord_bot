package com.unwin.joodbot.commands

import de.btobastian.javacord.entities.message.Message
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import com.unwin.joodbot.json_reader

/**
 * Created by unwin on 27-Mar-16.
 */
class getfuncs_command : CommandExecutor {

    @Command(aliases = arrayOf("get-funcs", "get-tags"), description = "Returns the functions in the file")
    fun onCommand(command: String, args: Array<String>, message: Message) {
        var function_count = 0
        var temp = ""
        var j_reader = json_reader()
        j_reader.read_json_config()

        for(a in 0..j_reader.functions.size-1) {
            if(j_reader.functions[a].length < 1) {

            } else {
                temp += "``" + j_reader.functions[a] + "``, "
                ++function_count
            }
        }
        if(temp.length < 1) {
            message.reply("There was an error getting the functions")
        } else {
            message.reply("There are $function_count functions in the file: \n$temp")
        }
    }

}