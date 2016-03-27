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
        var subredditName = args[0]
        println("subreddit name = $subredditName")
        var link = "link-null"

        try {
            var redditClient = getRedditClient()
            var post: Submission
            var subreddit = redditClient.getSubreddit(subredditName)
            // If my pull request is successful this will change to .isQuarantined
            if(subreddit.isNsfw){
                //
            }

            post = redditClient.getRandomSubmission(subredditName)

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
        var redditClient = RedditClient(userAgent)

        var jReader = json_reader()
        jReader.read_json_config()

        var credentials = Credentials.script(jReader.r_username, jReader.r_password, jReader.r_client_id, jReader.r_client_secret)
        var authData = redditClient.oAuthHelper.easyAuth(credentials)
        redditClient.authenticate(authData)

        return redditClient
    }

}