package com.unwin.joodbot.commands

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dean.jraw.RedditClient
import net.dean.jraw.http.UserAgent
import net.dean.jraw.http.oauth.Credentials
import com.unwin.joodbot.json_reader

/**
 * Created by unwin on 27-Mar-16.
 */
class daisy_command : CommandExecutor {

    @Command(aliases = arrayOf("\$daisy", "\$Daisy"), description = "Returns a random link from /r/daisyridley")
    fun onCommand(command: String, args: Array<String>): String {
        var link = "link-null"

        var redditClient = getRedditClient()

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