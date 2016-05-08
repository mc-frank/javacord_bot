package com.unwin.joodbot.commands

import com.unwin.joodbot.json_reader
import de.btobastian.javacord.entities.Channel
import de.btobastian.javacord.entities.message.Message
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor

/**
 * Created by unwin on 01-May-16.
 */
class mark_command: CommandExecutor {

    @Command(aliases = arrayOf("mark"), description = "Marks a channel as NSFW or SFW")
    fun onCommand(command: String, args: Array<String>, channel: Channel, message: Message) {
        var nsfw_mark = args[0].toLowerCase()
        if( !(nsfw_mark.equals("nsfw")) && !(nsfw_mark.equals("sfw")) ) {
            message.reply("Not a correct channel marking.\n Try using 'sfw' or 'nsfw'")
        }else {
            var j_reader = json_reader()
            j_reader.write_channel_mark(channel, nsfw_mark)
            message.reply("This channel marked $nsfw_mark.")
        }
    }

}