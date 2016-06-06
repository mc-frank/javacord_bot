package com.unwin.joodbot.commands

import com.unwin.joodbot.json_reader
import de.btobastian.javacord.entities.message.Message
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dean.jraw.RedditClient
import net.dean.jraw.http.UserAgent
import net.dean.jraw.http.oauth.Credentials

/**
 * Created by unwin on 27-Mar-16.
 */
class daisy_command : CommandExecutor {

    @Command(aliases = arrayOf("daisy", "Daisy"), description = "Returns a random link from /r/daisyridley")
    fun onCommand(command: String, args: Array<String>, message: Message): String {

        var j_reader = json_reader()
        j_reader.read_json_config()
        if(j_reader.r_enabled == false) {
            return "Reddit functions aren't enabled :("
        }

        message.getReceiver().type()

        val userAgent = UserAgent.of("discord-bot", "com.unwin.discordbot", "v3.0", "joodbot")
        val redditClient = RedditClient(userAgent)
        val credentials = Credentials.script(j_reader.r_username, j_reader.r_password, j_reader.r_client_id, j_reader.r_client_secret)
        val auth_data = redditClient.oAuthHelper.easyAuth(credentials)
        redditClient.authenticate(auth_data)

        var link = "link-null"

        try {
            var post = redditClient.getRandomSubmission("DaisyRidley")
            var postTitle = post.title
            var postLink = post.url

            link = postTitle + " - " + postLink

        } catch (ex: Exception) {
            link = "Error in getDaisyLink -- ${ex.message}"
        }

        return link
    }
}