import com.google.gson.GsonBuilder
import de.btobastian.javacord.entities.User
import java.io.File
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

/**
 * Created by unwin on 01-Mar-16.
 */
class jsonReader {

    val file_name = "config.json"
    var config_file = File(file_name)

    var email: String = ""
    var password: String = ""

    var maxsize: Int = 50

    var functions = Array(maxsize, {i -> ""})
    var actions = Array(maxsize, {i -> ""})

    var usernames = Array(100, {i -> ""})
    var ids = Array(100, {i -> ""})

    var r_username = ""
    var r_password = ""
    var r_client_id = ""
    var r_client_secret = ""

    //
    //

    //expr
    var raspi_addr = ""

    fun readJsonConfig(){

        try {
            var text = config_file.readText()

            var jsonObject = JSONParser().parse(text.toString()) as JSONObject

            var a_obj = jsonObject.get("api") as JSONObject
            var f_obj = jsonObject.get("functions") as JSONObject
            var u_obj = jsonObject.get("users") as JSONObject
            var r_obj = jsonObject.get("reddit") as JSONObject
            var exp_obj = jsonObject.get("exp") as JSONObject

            // Credentials objects
            email = a_obj.get("email") as String
            password = a_obj.get("password") as String

            // Functions and actions objects
            var f_temp = f_obj.entries
            for(a in 0..f_temp.indices.last) {
                var t_temp = f_temp.elementAt(a).toString().split("=")

                functions[a] = t_temp[0]
                actions[a] = t_temp[1]
            }

            // Users and ids objects
            var u_temp = u_obj.entries
            for(a in 0..u_temp.indices.last) {
                var t_temp = u_temp.elementAt(a).toString().split("=")
                usernames[a] = t_temp[0]
                ids[a] = t_temp[1]
            }

            // Create RedditCreds object and fill it with stuff from file
            r_username = r_obj.get("username") as String
            r_password = r_obj.get("password") as String
            r_client_id = r_obj.get("client_id") as String
            r_client_secret = r_obj.get("client_secret") as String

            // Experimental stuff
            raspi_addr = exp_obj.get("raspi-addr") as String

        } catch (ex: Exception) {
            println("Error in readJson -- ${ex.message}")
        }
    }

    fun getEmailFromConfig(): String {
        return email
    }

    fun getPasswordFromConfig(): String {
        return password
    }

    //data class RedditCreds(var username: String, var password: String, var client_id: String, var client_secret: String)

    fun writeUsersToConfig(m_users: Collection<User>?) {

        try {

            var text = config_file.readText()
            var jsonObject = JSONParser().parse(text.toString()) as JSONObject
            var u_obj = jsonObject.get("users") as JSONObject

            var users = m_users as Collection<User>
            if(users.count() == 0){
                return
            }
            for(a in 0..users.size-1) {
                var user_name = users.elementAt(a).name
                var user_id = users.elementAt(a).id

                u_obj.put(user_name, user_id)
            }

            jsonObject.replace("users", u_obj)
            var gson = GsonBuilder().setPrettyPrinting().create()
            var pretty_json = gson.toJson(jsonObject)

            config_file.writeText(pretty_json)

        } catch(ex: Exception) {
            println("Error in writeUsersToConfig -- ${ex.message}")
        }

    }

    fun writeFunctionsToConfig(newVar: String) {

        try {

            var newFuncAct = newVar.split(':')
            var function = newFuncAct[0].trim()
            var action = newFuncAct[1].trim()

            var text = config_file.readText()
            var jsonObject = JSONParser().parse(text.toString()) as JSONObject
            var f_obj = jsonObject.get("functions") as JSONObject

            var functionsInConfig = f_obj.toString()

            for(a in 0..functionsInConfig.count()) {
                // function conflict
            }

            f_obj.put(function, action)
            jsonObject.replace("functions", f_obj)
            var gson = GsonBuilder().setPrettyPrinting().create()
            var pretty_json = gson.toJson(jsonObject)

            config_file.writeText(pretty_json)

        } catch (ex: Exception) {
            println("Error in writeFunctionsToConfig -- ${ex.message}")
        }

    }
}