import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by unwin on 28-Feb-16.
 */


/*
 *
 * This file is just some test stuff
 * that I was playing around with along with
 * a Python webserver on a Raspberry Pi
 * grabbing information
 *
 */
class raspi_stuff {

    fun fetchData(data: String): String {
        var temp = "temp-null"
        var humidity = "humidity-null"
        var jReader = json_reader()
        jReader.readJsonConfig()

        try {
            var result: StringBuilder = StringBuilder()
            var url = URL(jReader.raspi_addr)
            var conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            var rd = BufferedReader(InputStreamReader(conn.inputStream))

            rd.forEachLine { result.append(it) }

            var jsonObject = JSONParser().parse(result.toString()) as JSONObject
            var t_value = jsonObject.get("temperature")
            var h_value = jsonObject.get("humidity")
            temp = t_value.toString() + "°C"
            humidity = h_value.toString() + "%"

        } catch(ex: Exception) {
            println("Error in fetchTemp - ${ex.message}")
            temp = ex.message as String
            humidity = ex.message as String
        }

        if(data.equals("temp")) {
            return temp
        } else if (data.equals("hum")){
            return humidity
        }
        return "Raspberry Pi Connection Error :("
    }

}