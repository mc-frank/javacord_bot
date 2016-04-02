package com.unwin.joodbot.commands

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dean.jraw.RedditClient
import net.dean.jraw.http.UserAgent
import net.dean.jraw.http.oauth.Credentials
import com.unwin.joodbot.json_reader
import net.dean.jraw.models.Submission

/**
 * Created by unwin on 27-Mar-16.
 */
class subreddit_command : CommandExecutor {

    @Command(aliases = arrayOf("\$/r/"), description = "Returns random image from a subreddit")
    fun onCommand(command: String, args: Array<String>): String {
        var subreddit_name = args[0]
        return get_post(subreddit_name)
    }

    fun get_post(subreddit_name: String): String {
        println("subreddit name = $subreddit_name")
        var link = "link-null"

        try {
            var redditClient = getRedditClient()
            var post: Submission
            var subreddit = redditClient.getSubreddit(subreddit_name)
            // If my pull request is successful this will change to .isQuarantined
            if(subreddit.isNsfw){
                //
            }

            post = redditClient.getRandomSubmission(subreddit_name)

            link = post.title + " - " + post.url
        } catch(ex: Exception) {
            println("Error in randomSubredditPost - ${ex.message}")
            link = "Error in randomSubredditPost - ${ex.message}"
        }

        return link
    }


    //JRAW Reddit Stuff
    fun getRedditClient(): RedditClient {
        var userAgent = UserAgent.of("discord-bot", "com.unwin.discord-bot", "v3.0", "fcumbadass")
        var reddit_client = RedditClient(userAgent)

        var jReader = json_reader()
        jReader.read_json_config()

        var credentials = Credentials.script(jReader.r_username, jReader.r_password, jReader.r_client_id, jReader.r_client_secret)
        var authData = reddit_client.oAuthHelper.easyAuth(credentials)
        reddit_client.authenticate(authData)

        return reddit_client
    }

}