package com.unwin.joodbot.commands

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by unwin on 27-Mar-16.
 */
class chuck_command : CommandExecutor {

    @Command(aliases = arrayOf("chuck", "norris"), description = "Tells a Chuck Norris joke")
    fun onCommand(command: String, args: Array<String>): String {
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

}