package com.unwin.joodbot.commands

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import com.unwin.joodbot.json_reader

/**
 * Created by unwin on 27-Mar-16.
 */
class editfuncs_command : CommandExecutor {

    @Command(aliases = arrayOf("edit-funcs"), description = "Overwrites a function in the file")
    fun onCommand(command: String, args: Array<String>) {
        var func = args[0]
        var action = ""
        for(i in 1..args.size-1) {
            action += " " + args[i]
        }

        var j_reader = json_reader()
        j_reader.read_json_config()

        j_reader.functions.forEach {
            if(it.length != 0 && func.equals(it)) {
                j_reader.remove_functions(func)
            }
        }

        j_reader.write_functions(func, action)
    }

}