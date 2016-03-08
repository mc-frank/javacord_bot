import net.dean.jraw.RedditClient
import net.dean.jraw.http.UserAgent
import net.dean.jraw.http.oauth.Credentials
import net.dean.jraw.models.Submission

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by unwin on 06-Feb-16.
 */
class web {

    fun fetchJoke(): String {
        var joke = "joke-null"

        try {
            var result: StringBuilder = StringBuilder()
            var url = URL("http://api.icndb.com/jokes/random")
            var conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            var rd = BufferedReader(InputStreamReader(conn.inputStream))

            // how come I've never seen this fantastic shit before?
            rd.forEachLine { result.append(it) }

            var jsonObject = JSONParser().parse(result.toString()) as JSONObject
            var valueObj = jsonObject.get("value") as JSONObject
            var value = valueObj.get("joke")
            joke = value as String

        } catch(ex: Exception) {
            println("Error in fetchJoke - ${ex.message}")
        }

        return joke;
    }

    fun getDaisyLink(): String {
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

    fun randomSubredditPost(subredditName: String): String {
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
        jReader.readJsonConfig()

        var credentials = Credentials.script(jReader.r_username, jReader.r_password, jReader.r_client_id, jReader.r_client_secret)
        var authData = redditClient.oAuthHelper.easyAuth(credentials)
        redditClient.authenticate(authData)

        return redditClient
    }

}