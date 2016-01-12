import de.btobastian.javacord.impl.Javacord
import de.btobastian.javacord.listener.ReadyListener
import java.io.File
import kotlin.collections.forEach
import kotlin.collections.listOf

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
        //
    }

    api.email = creds[0]
    api.password = creds[1]

    api.connect(object : ReadyListener {
        override fun onReady() {
            println("Connected fam")
        }

        override fun onFail() {
            println("Connection failed fam")
        }
    })

    api.game = "ALLAHU FRANKBAR"

    var filefunctions: fileFunctions = fileFunctions()
    filefunctions.getFunctions()

    api.registerListener(mainListener())

}

