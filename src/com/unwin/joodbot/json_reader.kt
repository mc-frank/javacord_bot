package com.unwin.joodbot

import com.google.gson.GsonBuilder
import de.btobastian.javacord.entities.Channel
import de.btobastian.javacord.entities.User
import java.io.File
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

/**
 * Created by unwin on 01-Mar-16.
 */
class json_reader {

    var maxsize: Int = 1000

    val file_name = "config.json"
    var config_file = File(file_name)

    var token: String = ""
    var owner_id: String = ""
    var status: String = ""
    var prefix: String = ""

    var functions = Array(maxsize, {i -> ""})
    var actions = Array(maxsize, {i -> ""})

    var usernames = Array(maxsize, {i -> ""})
    var ids = Array(maxsize, {i -> ""})

    var r_username: String = ""
    var r_password: String = ""
    var r_client_id: String = ""
    var r_client_secret: String = ""

    var channel_ids = Array(maxsize, {i -> ""})
    var channel_marks = Array(maxsize, {i -> ""})


    //
    //

    fun read_json_config(){

        try {
            var text = config_file.readText()

            var jsonObject = JSONParser().parse(text.toString()) as JSONObject

            var a_obj = jsonObject.get("api") as JSONObject
            var f_obj = jsonObject.get("functions") as JSONObject
            var u_obj = jsonObject.get("users") as JSONObject
            var r_obj = jsonObject.get("reddit") as JSONObject
            var c_obj = jsonObject.get("channels") as JSONObject

            // Credentials objects
            status = a_obj.get("status") as String
            token = a_obj.get("token") as String
            owner_id = a_obj.get("owner_id") as String
            prefix = a_obj.get("prefix") as String
            //

            // Functions and actions objects
            var f_keys = f_obj.keys
            var f_values = f_obj.values
            var count = 0

            f_keys.forEach {
                functions[count++] = it as String
            }
            count = 0

            f_values.forEach {
                actions[count++] = it as String
            }
            count = 0
            //

            // Users and ids objects
            var u_temp = u_obj.entries
            for(a in 0..u_temp.indices.last) {
                var t_temp = u_temp.elementAt(a).toString().split("=")
                usernames[a] = t_temp[0]
                ids[a] = t_temp[1]
            }
            //

            // Create RedditCreds object and fill it with stuff from file
            r_username = r_obj.get("username") as String
            r_password = r_obj.get("password") as String
            r_client_id = r_obj.get("client_id") as String
            r_client_secret = r_obj.get("client_secret") as String
            //

            // Channels and their marks
            var c_keys = c_obj.keys
            var c_values = c_obj.values

            c_keys.forEach {
                channel_ids[count++] = it as String
            }
            count = 0

            c_values.forEach {
                channel_marks[count++] = it as String
            }
            count = 0

        } catch (ex: Exception) {
            println("Error in read_json_from_config -- ${ex.message}")
        }

    }

    fun get_user_id(username: String): String {
        var r_id = "r_id null"

        try {
            for(a in 0..usernames.size-1) {
                if(username.equals(usernames[a])) {
                    r_id = ids[a]
                }
            }
        } catch (ex: Exception) {
            println("Error in getUserIDFromConfig -- ${ex.message}")
        }

        return r_id
    }

    fun write_channel_mark(channel: Channel, nsfw_mark: String) {

        try {

            var text = config_file.readText()
            var jsonObject = JSONParser().parse(text.toString()) as JSONObject
            var c_obj = jsonObject.get("channels") as JSONObject

            c_obj.remove(channel.id)
            c_obj.put(channel.id, nsfw_mark)

            jsonObject.replace("channels", c_obj)
            var gson = GsonBuilder().setPrettyPrinting().create()
            var pretty_json = gson.toJson(jsonObject)

            config_file.writeText(pretty_json)

        } catch(ex: Exception) {
            println("Error in write_channel_mark -- ${ex.message}")
        }

    }

    fun get_channel_mark(channel: Channel): String {

        try {

            for(a in 0..channel_ids.size) {
                if(channel_ids.elementAt(a).length == 0) {
                    //
                } else {
                    if(channel_ids.elementAt(a).equals(channel.id)) {
                        return channel_marks.elementAt(a)
                    }
                }
            }

        } catch(ex: Exception) {
            println("Error in get_channel_mark -- ${ex.message}")
        }

        return "null"

    }

    fun write_users(m_users: Collection<User>?) {

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

    fun write_functions(new_func: String, new_action: String) {

        try {

            var function = new_func
            var action = new_action

            var text = config_file.readText()
            var jsonObject = JSONParser().parse(text) as JSONObject
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
        
        read_json_config()

    }

    fun remove_functions(func: String) {

        var text = config_file.readText()
        var jsonObject = JSONParser().parse(text) as JSONObject

        functions.forEach {
            if(it.length != 0 && func.equals(it)) {
                jsonObject.remove(func)
            }
        }

    }
}