import java.io.File
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
    var jReader = json_reader()
    jReader.read_json_config()
    var email = jReader.get_email()
    var pass = jReader.get_password()

    val api = Javacord.getApi()

    api.setEmail(email)
    api.setPassword(pass)

    api.connect(object: FutureCallback<DiscordAPI> {
        override fun onSuccess(api: DiscordAPI?) {
            println("Connect to Discord as ${api?.yourself?.name}/(@${api?.yourself?.id}). Token = ${api?.token}")
            setupAPI(api, jReader)
        }

        override fun onFailure(t: Throwable) {
            println("Unable to connect to Discord Servers :( -- ${t.message}")
        }
    })
}

// Run when the api gets a connection to the server
fun setupAPI(n_api: DiscordAPI?, jReader: json_reader) {
    var api = n_api as DiscordAPI

    api.game = jReader.status
    api.registerListener(mainListener())
    api.setAutoReconnect(true)

    // Do console based commands
    var mainListener = mainListener()
    var prefix = mainListener.prefix

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
        else if (input.startsWith("${prefix}id")) {

            var file = File("ids.txt")
            if(file.exists()) {
                file.delete()
            }
            var users = api.users
            jReader.write_users(users)
            var file_text = ""
            users?.forEach {
                println("${it.name} - (${it.id})")
                file.appendText("${it.name} - (${it.id})\r\n")
                file_text += "${it.name} - ${it.id}\r\n"
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
            api.acceptInvite(invite)
        }
        else if (input.startsWith("${prefix}reconnect")) {
            api.reconnect(object: FutureCallback<DiscordAPI> {
                override fun onSuccess(api: DiscordAPI?) {
                    println("Reconnected")
                    setupAPI(api, jReader)
                }
                override fun onFailure(t: Throwable) {
                    println("Reconnect failed :(")
                }
            })
        }

    }
}