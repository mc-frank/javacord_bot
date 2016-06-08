package com.unwin.joodbot

import kotlin.text.split
import kotlin.text.startsWith
import de.btobastian.javacord.*
import kotlin.text.*
import com.google.common.util.concurrent.FutureCallback
import de.btobastian.sdcf4j.handler.JavacordHandler
import net.dean.jraw.RedditClient

import com.unwin.joodbot.commands.*

/**
 * Created by unwin on 10/01/2016.
 */

fun main(args: Array<String>) {

    val api = Javacord.getApi()

    var j_reader = json_reader()
    j_reader.read_json_config()
    var token = j_reader.token

    api.setToken(token, false)

    api.connect(object: FutureCallback<DiscordAPI> {
            override fun onSuccess(api: DiscordAPI?) {
                println("Connect to Discord as ${api?.yourself?.name}/(@${api?.yourself?.id})")
                setupAPI(api, j_reader)
            }

            override fun onFailure(t: Throwable) {
                println("Unable to connect to Discord Servers :( -- ${t.message}")
            }
        })
}

// Run when the api gets a connection to the server
fun setupAPI(n_api: DiscordAPI?, j_reader: json_reader) {

    var api = n_api as DiscordAPI

    var prefix = j_reader.prefix

    api.game = j_reader.status
    api.isIdle = true
    api.registerListener(main_listener())

    //Register commands using sdcf4j.
    var command_handler = JavacordHandler(api)
    command_handler.defaultPrefix = prefix

    command_handler.registerCommand(bot_command())
    command_handler.registerCommand(chuck_command())
    command_handler.registerCommand(reconnect_command())
    command_handler.registerCommand(daisy_command())
    command_handler.registerCommand(avatar_command())
    command_handler.registerCommand(info_command())
    command_handler.registerCommand(filter_command())
    command_handler.registerCommand(getfuncs_command())
    command_handler.registerCommand(addfuncs_command())
    command_handler.registerCommand(editfuncs_command())
    command_handler.registerCommand(execute_command())
    command_handler.registerCommand(status_command())
    command_handler.registerCommand(stop_command())
    command_handler.registerCommand(dump_command())
    command_handler.registerCommand(mark_command())
    //

    api.setAutoReconnect(true)

    // Do console based commands
    while (true) {

        println("Commands ready: ")
        var input = readLine()

        if (input!!.startsWith("${prefix}msg", true)) {
            var channels = api.getServerById("90542226181988352")?.channels
            var generalElement = 0
            for(a in 0..(channels?.size)!!.minus(1)) {
                if(channels?.elementAt(a)?.name.equals("developer")) {
                    generalElement = a
                }
            }
            var generalChannel = channels?.elementAt(generalElement)
            println("channel = ${generalChannel?.name}")
            var msg = input.split("${prefix}msg ")
            generalChannel?.sendMessage("${msg[1]}")
        }
        else if (input.startsWith("${prefix}status")){
            var word: String = input.substring(8, input.length)
            word.trim()
            api.game = word
        }
        else if (input.startsWith("${prefix}stop", true)) {
            System.exit(-1)
        }
        else if (input.startsWith("${prefix}join-server")) {
            var invite = input.substring(13, input.length)
            if(invite.length == 0) {
                println("No invite specified.")
            }
            api.acceptInvite(invite)
        }
        else if (input.startsWith("${prefix}reconnect")) {
            api.setAutoReconnect(false)
            api.reconnectBlocking()
            api.setAutoReconnect(true)
        }


    }
}