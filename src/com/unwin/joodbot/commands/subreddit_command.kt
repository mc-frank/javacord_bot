package com.unwin.joodbot.commands

import net.dean.jraw.models.Submission
import de.btobastian.sdcf4j.CommandExecutor
import de.btobastian.sdcf4j.Command
import de.btobastian.javacord.entities.Channel
import de.btobastian.javacord.entities.message.Message
import net.dean.jraw.RedditClient
import com.unwin.joodbot.json_reader
import net.dean.jraw.http.UserAgent
import net.dean.jraw.http.oauth.Credentials


/**
 * Created by unwin on 27-Mar-16.
 */
class subreddit_command : CommandExecutor {

    @Command(aliases = arrayOf("/r/"), description = "Returns random image from a subreddit", async = true)
    fun onCommand(command: String, args: Array<String>, channel: Channel, message: Message): String {
        var subreddit_name = args[0]
        return get_post(subreddit_name, channel, message)
    }

    fun get_post(subreddit_name: String, channel: Channel, message: Message): String {

        var j_reader = json_reader()
        j_reader.read_json_config()
        if(j_reader.r_enabled == false) {
            return "Reddit functions aren't enabled :("
        }

        message.receiver.type()

        val userAgent = UserAgent.of("discord-bot", "com.unwin.discordbot", "v3.0", "joodbot")
        val redditClient = RedditClient(userAgent)
        val credentials = Credentials.script(j_reader.r_username, j_reader.r_password, j_reader.r_client_id, j_reader.r_client_secret)
        val auth_data = redditClient.oAuthHelper.easyAuth(credentials)
        redditClient.authenticate(auth_data)

        println("subreddit name = $subreddit_name")
        var link = "link-null"

        try {

            var post: Submission
            var subreddit = redditClient.getSubreddit(subreddit_name)

            if(subreddit.isNsfw){
                j_reader.read_json_config()
                var channel_mark = j_reader.get_channel_mark(channel)
                if(channel_mark.equals("sfw")) {
                    return "This subreddit is NSFW and not allowed in this SFW marked channel."
                }
            } else if(subreddit.isQuarantined) {
                return "This subreddit is Quarantined and so cannot be accessed by this bot."
            }

            post = redditClient.getRandomSubmission(subreddit_name)

            link = post.title + " - " + post.url
        } catch(ex: Exception) {
            println("Error in randomSubredditPost - ${ex.message}")
            link = "Error in randomSubredditPost - ${ex.message}"
        }

        return link
    }

}