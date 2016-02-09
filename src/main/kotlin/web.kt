import net.dean.jraw.RedditClient
import net.dean.jraw.http.UserAgent
import net.dean.jraw.http.oauth.Credentials

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

        try {


        } catch(ex: Exception) {
            println("Error in fetchJoke - ${ex.message}")
        }


        return joke;
    }

    fun getDaisyLink(): String {
        var link = "link-null"

        var userAgent = UserAgent.of("discord-bot", "com.unwin.discord-bot", "v2.0", "fcumbadass")
        var redditClient = RedditClient(userAgent)

        var creds: List<String> = listOf()
        try {
            creds = File("pass.txt").readLines()
        } catch (ex: Exception) {
            // File not found or something.
        }

        var credentials = Credentials.script(creds[2], creds[3], creds[4], creds[5])
        var authData = redditClient.oAuthHelper.easyAuth(credentials)
        redditClient.authenticate(authData)

        var post = redditClient.getRandomSubmission("DaisyRidley")
        var postTitle = post.title
        var postLink = post.url

        link = postTitle + " - " + postLink

        return link
    }

}