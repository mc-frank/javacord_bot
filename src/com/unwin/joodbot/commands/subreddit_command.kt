package com.unwin.joodbot.commands

import net.dean.jraw.models.Submission
import de.btobastian.sdcf4j.CommandExecutor
import de.btobastian.sdcf4j.Command
import de.btobastian.javacord.entities.Channel
import de.btobastian.javacord.entities.message.Message
import net.dean.jraw.RedditClient
import com.unwin.joodbot._reddit_client
import com.unwin.joodbot._reddit
import com.unwin.joodbot.json_reader


/**
 * Created by unwin on 27-Mar-16.
 */
class subreddit_command : CommandExecutor {

    @Command(aliases = arrayOf("/r/"), description = "Returns random image from a subreddit", async = true)
    fun onCommand(command: String, args: Array<String>, channel: Channel, message: Message): String {
        var subreddit_name = args[0]
        return get_post(subreddit_name, channel)
    }

    fun get_post(subreddit_name: String, channel: Channel): String {
        println("subreddit name = $subreddit_name")
        var link = "link-null"

        try {
            var redditClient = _reddit_client as RedditClient

            if( !(redditClient.isAuthenticated) ) {
                _reddit.authenticate()
                redditClient = _reddit_client as RedditClient
            }

            var post: Submission
            var subreddit = redditClient.getSubreddit(subreddit_name)

            if(subreddit.isNsfw){
                var j_reader = json_reader()
                j_reader.read_json_config()
                var channel_mark = j_reader.get_channel_mark(channel)
                if(channel_mark.equals("sfw")) {
                    return "This subreddit is NSFW and not allowed in this SFW marked channel."
                }
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