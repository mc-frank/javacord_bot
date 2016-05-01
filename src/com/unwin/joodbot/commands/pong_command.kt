import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor

/**
 * Created by unwin on 26-Mar-16.
 */
class pong_command : CommandExecutor {

    @Command(aliases = arrayOf("ping"), description = "A ping-pong test")
    fun onCommand(command: String, args: Array<String>): String {
        return "PONG!"
    }

}
