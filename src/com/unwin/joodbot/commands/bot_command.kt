package com.unwin.joodbot.commands

import de.btobastian.javacord.entities.message.Message
import de.btobastian.javacord.entities.message.MessageBuilder
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor

/**
 * Created by unwin on 27-Mar-16.
 */
class bot_command : CommandExecutor {

    @Command(aliases = arrayOf("bot"), description = "Posts the bots commands")
    fun onCommand(command: String, args: Array<String>, message: Message) {
        var channel = message.channelReceiver
        channel.sendMessage(MessageBuilder()
                .append("My functions are (everyone):\n")
                .appendCode("", "filter <user/word> - filters and shows the number of times a word has been mentioned in the Discord bot-com.unwin.joodbot.log.txt")
                .appendCode("", "get-funcs - Retrieves list of dynamic functions in the file currently")
                .appendCode("", "add-funcs new-function1  new-action1 - Adds a function to the file")
                .appendCode("", "edit-funcs function1  new-action1 - Edits a function in the file")
                .appendCode("", "avatar <user> - Posts the image of the user specified, currently must be the exact name of the user\n")
                .appendCode("", "/r/ <subreddit> - Posts a random link from <subreddit>")
                .appendCode("", "Daisy - Posts a random link from /r/DaisyRidley")
                .appendCode("", "Chuck or Norris - Posts a random Chuck Norris joke")
                .appendCode("", "status <status> - Updates the status of the bot to the argument")
                .append("Admin/Moderator only:\n")
                .appendCode("", "mark <sfw/nsfw> - Marks a channel as SFW or NSFW.")
                .appendCode("", "execute <code> - Executes specified Java code. Must have a return statement.")
                .appendCode("", "stop - Stops the bot, I can only be slain by the dank MCFrank")

                .build())
    }

}