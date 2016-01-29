import de.btobastian.javacord.Channel
import de.btobastian.javacord.impl.Javacord
import de.btobastian.javacord.listener.ReadyListener
import java.io.File
import kotlin.collections.listOf
import kotlin.text.split
import kotlin.text.startsWith

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

    api.email = creds[0]
    api.password = creds[1]

    api.connect(object : ReadyListener {
        override fun onReady() {
            println("Connected to server with name: ${api.yourself.name}")
        }

        override fun onFail() {
            println("Connection failed to server")
        }
    })

    api.game = "SPYING ON YOU >:)"

    var filefunctions: fileFunctions = fileFunctions()
    filefunctions.getFunctions()

    api.registerListener(mainListener())

    // Do console based commands
    while (true) {
        println("Commands ready: ")
        // Gonna make some command line arguments available to be used with the bot
        var input = readLine()
        if (input!!.startsWith("#msg", true)) {
            var generalChannel: Channel = api.getServerById("90542226181988352").channels.get(0)    // 3 for developer
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
        else if (input.startsWith("#stop", true)) {
            System.exit(-1)
        }
    }
}

