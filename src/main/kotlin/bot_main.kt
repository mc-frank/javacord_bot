import java.io.File
import kotlin.collections.listOf
import kotlin.text.split
import kotlin.text.startsWith
import de.btobastian.javacord.*
import de.btobastian.javacord.entities.Channel
import kotlin.collections.forEach
import kotlin.text.*

/**
 * Created by unwin on 10/01/2016.
 */
fun main(args: Array<String>) {
    val api = Javacord.getApi()

    var line: String
    var creds: List<String> = listOf()
    try {
        creds = File("pass.txt").readLines()
    } catch (ex: Exception) {
        // File not found or something.
    }

    api.setEmail(creds[0])
    api.setPassword(creds[1])

    /*
    api.connect(FutureCallback<DiscordAPI>() {
        override fun onSuccess(api: DiscordAPI) {
            println("Connected to server with name: ${api.yourself.name} and id: ${api.yourself.id}")
            setupAPI(api)
        }

        override fun onFailure(t: Throwable) {
            println("Connection failed to server - ${t.message}")
        }
    })
    */

    api.connectBlocking()
    setupAPI(api)


    // Do console based commands
    while (true) {
        println("Commands ready: ")
        // Gonna make some command line arguments available to be used with the bot
        var input = readLine()
        if (input!!.startsWith("#msg", true)) {
            var generalChannel: Channel = api.getServerById("90542226181988352").channels.elementAt(0)    // 3 for developer
            //generalChannel = api.getServerById("90542226181988352").channels.get(3)
            //println("channel = ${generalChannel.name}")
            var msg = input.split("#msg ")
            generalChannel.sendMessage("${msg[1]}")
        }
        else if(input.startsWith("#status")){
            var word: String = input.substring(8, input.length)
            word.trim()
            api.game = word
        }
        else if(input.startsWith("#id")) {
            var word: String = input.substring(4, input.length)
            word.trim()

            var users = api.users
            users.forEach { println(it) }


        }
        else if (input.startsWith("#stop", true)) {
            System.exit(-1)
        }
        else if (input.startsWith("#join-server")) {
            var invite = input.substring(13, input.length)
            if(invite.length == 0) {
                println("No invite specified.")
            }
            api.acceptInvite(invite)
        }
    }
}

// Run when the api gets a connection to the server
fun setupAPI(api: DiscordAPI) {
    api.game = "SPYING ON YOU >:)"

    var filefunctions: file_functions = file_functions()
    filefunctions.getFunctions()

    api.registerListener(mainListener())
}

