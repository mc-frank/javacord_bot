package com.unwin.joodbot.commands

import com.unwin.joodbot.json_reader
import de.btobastian.javacord.DiscordAPI
import de.btobastian.javacord.entities.Channel
import de.btobastian.javacord.entities.Server
import de.btobastian.javacord.entities.User
import de.btobastian.javacord.entities.message.Message
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.CtNewMethod

/**
 * Created by unwin on 27-Mar-16.
 */
class execute_command : CommandExecutor {

    @Command(aliases = arrayOf("execute"), description = "Executes inputted Java code")
    fun onCommand(command: String, args: Array<String>, message: Message, api: DiscordAPI): String {

        var return_string = "Not allowed to execute!"

        var j_reader = json_reader()
        j_reader.read_json_config()

        var user_id = message.author.id
        println(user_id)

        if( j_reader.get_member_can_exec(user_id) == true ) {
            if(message.content.contains("api.getToken()")) {
                return "Pls don't try to get the token :D"
            }

            var code = ""
            args.forEach { code += " $it" }
            code = code.replace("return (.+);".toRegex(), "return String.valueOf($1);")
            val pool = ClassPool.getDefault()
            pool.insertClassPath(ClassClassPath(api.javaClass))
            pool.importPackage("java.io.File")
            pool.importPackage("")
            pool.importPackage("de.btobastian.javacord.entities")
            pool.importPackage("de.btobastian.javacord.entities.message")
            pool.importPackage("de.btobastian.javacord.entities.invite")
            pool.importPackage("de.btobastian.javacord.entities.permissions")
            pool.importPackage("de.btobastian.javacord.exceptions")
            pool.importPackage("de.btobastian.javacord.utils")
            pool.importPackage("de.btobastian.javacord")
            val executeClass = pool.makeClass("ExecuteCode" + System.currentTimeMillis())
            executeClass.addMethod(CtNewMethod.make("public java.lang.Object executeCode (DiscordAPI api, Message message, Channel channel, Server server, User user) { $code }", executeClass))
            val clazz = executeClass.toClass(api.javaClass.classLoader, api.javaClass.protectionDomain)
            val obj = clazz.newInstance()
            val meth = clazz.getDeclaredMethod("executeCode", DiscordAPI::class.java, Message::class.java, Channel::class.java, Server::class.java, User::class.java)
            val server = if (message.channelReceiver != null) message.channelReceiver.server else null
            return_string = "```" + meth.invoke(obj, api, message, message.channelReceiver, server, message.author).toString() + "```"

            if(return_string.contains(api.token)) {
                return_string = "Pls don't try to get the token :D"
            }
        }

        return return_string

    }

}