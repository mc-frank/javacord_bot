 import de.btobastian.javacord.*
 import de.btobastian.javacord.entities.Channel
 import de.btobastian.javacord.entities.Server
 import de.btobastian.javacord.entities.User
 import de.btobastian.javacord.entities.message.Message
 import de.btobastian.javacord.entities.message.MessageBuilder
 import de.btobastian.javacord.listener.channel.ChannelChangeNameListener
import de.btobastian.javacord.listener.channel.ChannelChangeTopicListener
import de.btobastian.javacord.listener.message.MessageCreateListener
import de.btobastian.javacord.listener.message.MessageDeleteListener
import de.btobastian.javacord.listener.message.MessageEditListener
import de.btobastian.javacord.listener.message.TypingStartListener
import de.btobastian.javacord.listener.server.ServerJoinListener
import de.btobastian.javacord.listener.server.ServerMemberAddListener
import de.btobastian.javacord.listener.server.ServerMemberRemoveListener
import de.btobastian.javacord.listener.user.UserChangeNameListener
 import org.apache.http.concurrent.FutureCallback
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
        MessageDeleteListener, UserChangeNameListener, ServerJoinListener, ChannelChangeNameListener, ChannelChangeTopicListener
{

    protected val admins: Array<String> = arrayOf("vind", "mongzords", "Lucentconor", "MCFrank", "Trikzbowii")

    private val filefunc: file_functions = file_functions()
    public var _functions: Array<String> = filefunc.functions
    public var _actions: Array<String> = filefunc.actions


    override fun onMessageCreate(api: DiscordAPI, message: Message) {
        filefunc.getFunctions()
        var log: log = log()
        log.setNewFileName(log._FILENAME)

        var msg: String = message.content
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
        // TODO: not currently in the update of the API wrapper
        //if(user.equals(api.yourself.name)) {
        //    return
        //}

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
            if(msg.equals("#bot")) {
                postCommands(message)
            }
            else if(msg.contains("#bot-sys")) {
                var runtime: Runtime = Runtime.getRuntime()
                var rb: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()
                var uptime = rb.uptime / 1000
                var details: String = "CPU(s) -- " + runtime.availableProcessors() + "\nOS -- " + System.getProperty("os.name") + "\nFree memory to JVM -- " + runtime.freeMemory() / (1024*1024) + " MB \nUptime -- " + (uptime/60) + " minutes"
                message.reply(details)
            }

            else if(msg.startsWith("#filter")) {
                var log: log = log()

                var word: String = msg.substring(8, msg.length)
                word.trim()

                var count = log.filterText(word)
                message.reply("$word has been mentioned in $count messages")
            }
            else if(msg.contains("#broadcast")) {
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

            else if(msg.contains("#get-funcs")) {
                thread() {
                    var funcSize: Int = 0
                    for (i in 0..filefunc.max_size - 1) {
                        //println("functions length = ${_functions[i].length}")
                        if (_functions[i].length < 1 && _actions[i].length < 1) {
                            funcSize = i
                            break;
                        }
                    }
                    message.reply("There are $funcSize functions in the file currently:")
                    _functions.forEach { message.reply(it) }
                }
            }
            else if(msg.startsWith("#write-funcs")) {
                var userIsAdmin = false
                admins.forEach {
                    if( (user.equals(it)) ) {
                        userIsAdmin = true
                    }
                }
                if(userIsAdmin == true) {
                    // This entire function assumes the user knows what they're doing D:
                    var newFuncsLong = msg.substring(13, msg.length)
                    var newFuncs = newFuncsLong.split(", ")
                    filefunc.writeFunctions(newFuncs.toTypedArray())
                } else {
                    message.reply("This is still in works and so only admins can use it")
                }
            }

            else if(msg.contains("#status")) {
                var word: String = msg.substring(8, msg.length)
                word.trim()
                api.game = word
            }
            else if(msg.contains("#avatar")) {
                var word: String = msg.substring(8, msg.length).toLowerCase()
                var filename: String = "pics/avatar-$word.jpg"
                var reply: String = ""

                var avatar: ByteArray = ByteArray(0)
                var file: File = File(filename)

                var users: Array<User> = api.users.toTypedArray()

                loop@ for (i in 0..users.size - 1) {
                    var usr = users[i]
                    if (word.contains(usr.name.toLowerCase())) {
                        if (File(filename).exists()) {
                            //
                        } else {
                            var temp = usr.avatarAsByteArray
                            avatar = temp.get()
                            file.appendBytes(avatar)
                        }
                        reply = "$word's avatar:"
                    }
                }

                if (file.length() < 1) {
                    message.reply("Error: no avatar")
                } else if (reply.length == 0) {
                    message.reply("User not found")
                } else {
                    message.reply(reply)
                    message.channelReceiver.sendFile(file)
                }
            }
            }

            else if(msg.contains("#daisy")) {
                var web = web()
                thread() {
                    var link = web.getDaisyLink()
                    message.reply(link)
                }
            }
            else if(msg.startsWith("#/r/")){
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

            else if(msg.contains("#chuck") || msg.contains("#norris")) {
                var web = web()
                message.reply(web.fetchJoke())
            }
            else if(msg.contains("#stop")){
                if(user.equals("MCFrank")) {
                    // Stop the bot
                    System.exit(-1)
                }
            }

            // Reply with to a function in the file with the corresponding action
            check@ for(i in 0..filefunc.max_size-1) {
                //println("function = ${_functions[i]}")
                if(_functions[i].length < 1){
                    break@check
                }
                else {
                    if(msg.contains(_functions[i])) {
                        message.reply(_actions[i])
                    }
                }
            }
        }
    }

    override fun onMessageEdit(api: DiscordAPI, message: Message, string: String) {

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
        var log: log = log()
        log.setNewFileName(log._FILENAME)
    }

    override fun onServerJoin(api: DiscordAPI, server: Server) {

    }

    override fun onChannelChangeName(api: DiscordAPI, channel: Channel, oldName: String) {

    }

    override fun onChannelChangeTopic(api: DiscordAPI, channel: Channel, oldTopic: String) {

    }


    //

    fun postCommands(message: Message) {
        var channel = message.channelReceiver
        channel.sendMessage(MessageBuilder()
                .append("My functions are (everyone):\n")
                .appendCode("", "filter <user/word> - filters and shows the number of times a word has been mentioned in the Discord bot-log.txt")
                .appendCode("", "get-funcs - Retrieves list of dynamic functions in the file currently")
                .appendCode("", "avatar <user> - Posts the image of the user specified, currently must be the exact name of the user\n")
                .appendCode("", "/r/<subreddit> - Posts a random link from <subreddit>")
                .appendCode("", "Daisy - Posts a random link from /r/DaisyRidley")
                .appendCode("", "Chuck or Norris - Posts a random Chuck Norris joke")
                .append("Admin/Moderator only:\n")
                .appendCode("", "write-funcs new-function1 : new-action1 - Rewrites all the dynamic functions and actions in the file with new ones")
                .appendCode("", "status <status> - Updates the status of the bot to the argument")
                .appendCode("", "stop - Stops the bot, I can only be slain by the dank MCFrank")

                .build())
    }

}