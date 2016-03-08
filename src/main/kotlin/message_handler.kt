import com.google.common.util.concurrent.FutureCallback
import de.btobastian.javacord.*
import de.btobastian.javacord.entities.Channel
import de.btobastian.javacord.entities.Server
import de.btobastian.javacord.entities.User
import de.btobastian.javacord.entities.permissions.Role
import de.btobastian.javacord.entities.message.Message
import de.btobastian.javacord.entities.message.MessageBuilder
import de.btobastian.javacord.listener.channel.ChannelChangeNameListener
import de.btobastian.javacord.listener.channel.ChannelChangeTopicListener
import de.btobastian.javacord.listener.message.MessageCreateListener
import de.btobastian.javacord.listener.message.MessageDeleteListener
import de.btobastian.javacord.listener.message.MessageEditListener
import de.btobastian.javacord.listener.message.TypingStartListener
import de.btobastian.javacord.listener.role.RoleCreateListener
import de.btobastian.javacord.listener.server.ServerChangeNameListener
import de.btobastian.javacord.listener.server.ServerJoinListener
import de.btobastian.javacord.listener.user.UserChangeNameListener
import java.io.File
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean
import kotlin.collections.forEach
import kotlin.collections.toTypedArray
import kotlin.concurrent.thread
import kotlin.text.*

/**
 * Created by unwin on 10/01/2016.
 */
class mainListener: MessageCreateListener, MessageEditListener, TypingStartListener,
        MessageDeleteListener, UserChangeNameListener, ServerJoinListener, ChannelChangeNameListener,
        ChannelChangeTopicListener, ServerChangeNameListener, RoleCreateListener
{

    private val admins: Array<String> = arrayOf("vind", "mongzords", "Lucentconor", "MCFrank", "Trikzbowii")

    val prefix = "$"


    override fun onMessageCreate(api: DiscordAPI, message: Message) {
        var jReader = json_reader()
        jReader.readJsonConfig()

        var log: log = log()
        log.setNewFileName(log._FILENAME)

        var msg: String = message.content.toLowerCase()
        var user: String = message.author.name
        var channel: String? = message.channelReceiver.name

        // Ignore if the message is a private message
        if(message.isPrivateMessage) {
            println("$user sent a private message - $msg")
            log.setNewFileText("[PRIVATE MESSAGE] $user > $msg")
            log.writeFile()
            return
        }

        println("[$channel] $user > $msg")
        log.setNewFileText( ("[$channel] $user > $msg") )
        log.writeFile()


        // Ignore if message comes from bot
        if(user.equals(api.yourself.name)) {
            return
        }

        // Respond to BotBT's penis functions
        if(user.equals("BotBT")) {
            if(msg.contains("8") && msg.contains("=")) {
                message.reply("( ͡° ͜ʖ ͡°)")
            }
        }

        /*
        *
        * Start of cluster fuck
        *
        * */
        thread() {
            if(msg.equals("${prefix}bot")) {
                postCommands(message)
            }
            else if(msg.contains("${prefix}bot-sys") || msg.contains("${prefix}botsys") || msg.contains("${prefix}info")) {
                var runtime: Runtime = Runtime.getRuntime()
                var rb: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()
                var uptime = rb.uptime / 1000
                var details: String = "CPU(s) -- " + runtime.availableProcessors() +
                        "\nOS -- " + System.getProperty("os.name") +
                        "\nFree memory to JVM -- " + runtime.freeMemory() / (1024*1024) + " MB " +
                        "\nUptime -- " + (uptime/60) + " minutes (" + uptime/60/60 + " hours)" +
                        "\nReconnect enabled -- " + api.isAutoReconnectEnabled
                message.reply(details)
            }

            else if(msg.startsWith("${prefix}filter")) {
                var log: log = log()

                var word: String = msg.substring(8, msg.length)
                word.toLowerCase().trim()

                var count = log.filterText(word)
                message.reply("$word has been mentioned in $count messages")
            }
            else if(msg.contains("${prefix}broadcast")) {
                // TODO: This will allow a user to broadcast a message to all the channels they have permission to
                var serverID: Server
                serverID = api.getServerById("JJG")


                var channels = serverID.channels
                channels.forEach {
                    //if() {
                    //
                    //}
                }
            }

            else if(msg.contains("${prefix}get-funcs")) {
                var function_count = 0
                var temp = ""

                for(a in 0..jReader.functions.size-1) {
                    if(jReader.functions[a].length < 1) {

                    } else {
                        temp += jReader.functions[a] + "\n"
                        ++function_count
                    }
                }
                if(temp.length < 1) {
                    message.reply("There was an error getting the functions")
                } else {
                    message.reply("There are $function_count functions in the file: \n$temp")
                }

                /*
                var function_count = 0
                for(a in 0..filefunc.max_size-1) {
                    if(_functions[a].length != 0 && _actions[a].length != 0) {
                        ++function_count
                    }
                }
                var temp = Array(function_count, {i -> ""})
                for(a in 0..temp.size-1) {
                    temp[a] = _functions[a]
                }
                var reply_string = "There are $function_count functions in the file currently:\n"
                temp.forEach {
                    reply_string += "$it\n"
                }
                message.reply(reply_string)
                */
            }
            else if(msg.startsWith("${prefix}add-func")) {
                var newFunc = msg.substring(10, msg.length)
                jReader.writeFunctionsToConfig(newFunc)
            }

            else if(msg.contains("${prefix}status")) {
                // Get the argument after the command and trim it rid of whitespaces
                var word: String = msg.substring(8, msg.length).trim()

                // Set the api's game to the argument
                api.game = word
            }
            else if(msg.contains("${prefix}avatar")) {
                var pathName = "pics-temp/"
                var path = File(pathName)
                var word: String = msg.substring(8, msg.length).toLowerCase().trim()
                var filename: String = "${pathName}avatar-$word.jpg"
                var reply: String = ""
                try {

                    // Check if the path exists or not - if not, create it
                    if(path.isDirectory) {
                        // Do nothing, dir exists
                    } else {
                        path.mkdir()
                    }

                    // Crate some variables for the avatar (bytes), and the file (file)
                    var avatar: ByteArray
                    var file: File = File(filename)

                    // Create an array of users in the server to loop through
                    var users: Array<User> = api.users.toTypedArray()

                    // Loop through the users and check if their name matches one provided by the user
                    for (i in 0..users.size-1) {

                        // Checks if the current user in the loop is equal to the one provided by the user
                        if (word.equals(users[i].name.toLowerCase())) {

                            file.delete()
                            var temp = users[i].avatarAsByteArray
                            avatar = temp.get()
                            file.writeBytes(avatar)

                            // Add some text to be replied
                            reply = "$word's avatar:"
                        }
                    }

                    // Checks if the length of the file to be replied with it less than 1 byte (doesn't exist)
                    if (file.length() < 1) {
                        message.reply("Error: user has no set avatar")
                    }
                    // Checks if the reply text is equal to 0
                    else if (reply.length == 0) {
                        message.reply("User not found")
                    }
                    // If everything works fine and the file is good
                    else {
                        message.reply(reply)
                        var future_msg = message.channelReceiver.sendFile(file)
                        future_msg.get()
                        if(future_msg.isDone) {
                            file.delete()
                        }
                    }
                } catch (ex: Exception) {
                    message.reply("Error in avatar -- ${ex.message}")
                }
            }

            else if(msg.contains("${prefix}temp")) {
                if(!message.author.id.equals("90548142981808128")) {
                    message.reply("MCFrank only pls :3")
                } else {
                    var temp = raspi_stuff().fetchData("temp")
                    message.reply(temp)
                }
            }
            else if(msg.contains("${prefix}hum")) {
                if(!message.author.id.equals("90548142981808128")) {
                    message.reply("MCFrank only pls :3")
                } else {
                    var temp = raspi_stuff().fetchData("hum")
                    message.reply(temp)
                }
            }

            else if(msg.contains("${prefix}daisy")) {
                var web = web()
                thread() {
                    var link = web.getDaisyLink()
                    message.reply(link)
                }
            }
            else if(msg.startsWith("${prefix}/r/")){
                var subredditName = msg.substring(4, msg.trim().length)
                if(subredditName.contains(" ")) {
                    message.reply("Subreddit cannot have a space in it")
                } else {
                    var web = web()
                    var link: String
                    link = web.randomSubredditPost(subredditName)
                    message.reply(link)

                }
            }

            else if(msg.contains("${prefix}dump")) {
                var file = File("bot-log.txt")
                message.channelReceiver.sendFile(file)
                /*
                message.reply("This is gonna take a while - please wait")
                message.reply("Please also note this file may not be organised, I am only getting the messages from Discord as they give them to me")
                var size = 10//Integer.MAX_VALUE
                var channel_history = message.channelReceiver.getMessageHistory(size)
                var channel_msg_history = channel_history.get()
                var msgHistoryFile = File("msgHistoryFile.txt")
                var file_text = ""
                channel_msg_history.iterator().forEach {
                    file_text += it.content + "\r\n"
                }
                msgHistoryFile.writeText(file_text)
                message.channelReceiver.sendFile(msgHistoryFile)
                msgHistoryFile.deleteOnExit()
                */
            }
            else if(msg.contains("${prefix}chuck") || msg.contains("${prefix}norris")) {
                var web = web()
                message.reply(web.fetchJoke())
            }

            else if(msg.contains("${prefix}reconnect")) {
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
            else if(msg.contains("${prefix}stop")){
                admins.forEach {
                    if(user.equals(it)) {
                        System.exit(-1)
                    }
                }
            }

            // Reply with to a function in the file with the corresponding action
            var a = 0
            jReader.functions.forEach {
                if(msg.equals("$prefix$it")) {
                    message.reply(jReader.actions[a])
                }
                ++a
            }
        }
    }

    override fun onMessageEdit(api: DiscordAPI, message: Message, oldMessage: String) {
        var usr = message.author
        var originalMessage = oldMessage
        var newMessage = message
        var channel = message.channelReceiver

        println("[$channel] $usr > EDITED MESSAGED $originalMessage [to] $newMessage")
        var log = log()
        log.setNewFileName(log._FILENAME)
        log.setNewFileText("[$channel] $usr > EDITED MESSAGED $originalMessage [to] $newMessage")
    }

    override fun onTypingStart(api: DiscordAPI, user: User, channel: Channel?) {
        println("${user.name} is typing in ${channel?.name}")
    }

    override fun onMessageDelete(api: DiscordAPI, message: Message) {
        var msg: String = message.content
        var user: String = message.author.name
        var channel: String? = message.channelReceiver.name

        var log: log = log()
        log.setNewFileName(log._FILENAME)
        log.setNewFileText( ("[$channel] $user's message was deleted: $msg") )
        println("[$channel] $user's message was deleted: $msg")
        log.writeFile()
    }

    override fun onUserChangeName(api: DiscordAPI, user: User, oldName: String) {
        var logText = "[User Changed Name] $oldName changed their name to ${user.name}"
        println(logText)
        var log: log = log()
        log.setNewFileName(log._FILENAME)
        log.setNewFileText(logText)
        log.writeFile()
    }

    override fun onServerJoin(api: DiscordAPI, server: Server) {

    }

    override fun onChannelChangeName(api: DiscordAPI, channel: Channel, oldName: String) {
        var logText = "[Channel Name Change] $oldName channel's name changed to ${channel.name}"
        println(logText)
        var log = log()
        log.setNewFileName(log._FILENAME)
        log.setNewFileText(logText)
    }

    override fun onChannelChangeTopic(api: DiscordAPI, channel: Channel, oldTopic: String) {
        var logText = "[Channel Changed Topic] ${channel.name}'s topic changed from $oldTopic to ${channel.topic}"
        println(logText)
        var log = log()
        log.setNewFileName(log._FILENAME)
        log.setNewFileText(logText)
        log.writeFile()
    }

    override fun onServerChangeName(api: DiscordAPI, server: Server, name: String) {

    }

    override fun onRoleCreate(api: DiscordAPI, role: Role) {
        var logText = "${role.name} created"
        var log = log()
        log.setNewFileName(log._FILENAME)
        log.setNewFileText(logText)
        log.writeFile()
    }

    //

    fun postCommands(message: Message) {
        var channel = message.channelReceiver
        channel.sendMessage(MessageBuilder()
                .append("My functions are (everyone):\n")
                .appendCode("", "filter <user/word> - filters and shows the number of times a word has been mentioned in the Discord bot-log.txt")
                .appendCode("", "get-funcs - Retrieves list of dynamic functions in the file currently")
                .appendCode("", "add-func new-function1 : new-action1 - Adds a function to the functions file")
                .appendCode("", "avatar <user> - Posts the image of the user specified, currently must be the exact name of the user\n")
                .appendCode("", "/r/<subreddit> - Posts a random link from <subreddit>")
                .appendCode("", "Daisy - Posts a random link from /r/DaisyRidley")
                .appendCode("", "Chuck or Norris - Posts a random Chuck Norris joke")
                .appendCode("", "status <status> - Updates the status of the bot to the argument")
                .append("Admin/Moderator only:\n")
                .appendCode("", "stop - Stops the bot, I can only be slain by the dank MCFrank")

                .build())
    }

}