import java.io.File
import kotlin.collections.listOf
import kotlin.text.split
import kotlin.text.startsWith
import de.btobastian.javacord.*
import kotlin.collections.forEach
import kotlin.text.*
import com.google.common.util.concurrent.FutureCallback

/**
 * Created by unwin on 10/01/2016.
 */
fun main(args: Array<String>) {
    val api = Javacord.getApi()

    var creds: List<String> = listOf()
    try {
        creds = File("pass.txt").readLines()
    } catch (ex: Exception) {
        // File not found or something.
    }

    api.setEmail(creds[0])
    api.setPassword(creds[1])

    api.connect(object: FutureCallback<DiscordAPI> {
        override fun onSuccess(api: DiscordAPI?) {
            println("Connect to Discord as ${api?.yourself?.name}/(@${api?.yourself?.id})")
            setupAPI(api)
        }

        override fun onFailure(t: Throwable) {
            println("Unable to connect to Discord Servers :(")
        }
    })
}

// Run when the api gets a connection to the server
fun setupAPI(api: DiscordAPI?) {
    api?.game = "SPYING ON YOU >:)"

    var filefunctions: file_functions = file_functions()
    filefunctions.getFunctions()

    api?.registerListener(mainListener())
    api?.setAutoReconnect(true)

    // Do console based commands
    var mainListener = mainListener()
    var prefix = mainListener.prefix
    while (true) {
        println("Commands ready: ")
        // Gonna make some command line arguments available to be used with the bot
        var input = readLine()
        if (input!!.startsWith("${prefix}msg", true)) {
            var channels = api?.getServerById("90542226181988352")?.channels
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
            api?.game = word
        }
        else if (input.startsWith("${prefix}id")) {

            var file = File("ids.txt")
            if(file.exists()) {
                file.delete()
            }
            var users = api?.users
            users?.forEach {
                println("${it.name} - (${it.id})")
                file.appendText("${it.name} - (${it.id})\r\n")
            }

        }
        else if (input.startsWith("${prefix}stop", true)) {
            System.exit(-1)
        }
        else if (input.startsWith("${prefix}join-server")) {
            var invite = input.substring(13, input.length)
            if(invite.length == 0) {
                println("No invite specified.")
            }
            api?.acceptInvite(invite)
        }
    }
}