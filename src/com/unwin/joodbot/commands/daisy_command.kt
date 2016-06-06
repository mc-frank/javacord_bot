package com.unwin.joodbot.commands

import com.unwin.joodbot._reddit
import com.unwin.joodbot._reddit_client
import com.unwin.joodbot.json_reader
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dean.jraw.RedditClient

/**
 * Created by unwin on 27-Mar-16.
 */
class daisy_command : CommandExecutor {

    @Command(aliases = arrayOf("daisy", "Daisy"), description = "Returns a random link from /r/daisyridley")
    fun onCommand(command: String, args: Array<String>): String {

        var j_reader = json_reader()
        j_reader.read_json_config()
        if(j_reader.r_enabled == false) {
            return "Reddit functions aren't enabled :("
        }

        var link = "link-null"

        var redditClient = _reddit_client as RedditClient
        if( !(redditClient.isAuthenticated) ) {
            _reddit.authenticate()
            redditClient = _reddit_client as RedditClient
        }

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