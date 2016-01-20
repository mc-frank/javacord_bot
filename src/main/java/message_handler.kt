import de.btobastian.javacord.*
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
import de.btobastian.javacord.listener.voice.VoiceChannelChangeNameListener
import de.btobastian.javacord.message.Message
import java.io.File
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean
import kotlin.collections.forEach
import kotlin.collections.toTypedArray
import kotlin.text.*

/**
 * Created by unwin on 10/01/2016.
 */
class mainListener: MessageCreateListener, MessageEditListener, TypingStartListener,
        MessageDeleteListener, UserChangeNameListener, ServerMemberAddListener, ServerMemberRemoveListener,
        ServerJoinListener, ChannelChangeNameListener, ChannelChangeTopicListener, VoiceChannelChangeNameListener
{

    protected val admins: Array<String> = arrayOf("vind", "mongzords", "Lucentconor", "MCFrank")

    private val filefunc: fileFunctions = fileFunctions()
    public var _functions: Array<String> = filefunc.functions
    public var _actions: Array<String> = filefunc.actions


    override fun onMessageCreate(api: DiscordAPI, message: Message) {
        filefunc.getFunctions()

        var msg: String = message.content
        var user: String = message.author.name
        var channel: String? = message.channelReceiver.name

        if(message.isPrivateMessage) {
            println("$user sent a private message - $msg")
            return
        }

        println("[$channel] $user > $msg")
        var log: log = log()
        log.setNewFileName(log._FILENAME)
        log.setNewFileText( ("[$channel] $user > $msg") )
        log.writeFile()

        // Ignore if message comes from bot
        if(user.equals(api.yourself.name)) {
            return
        }

        // Respond to BotBT's penis functions
        if(user.equals("BotBT")) {
            if(msg.contains("8") && msg.contains("=3")) {
                message.reply("( ͡° ͜ʖ ͡°)")
            }
        }

        /*
        *
        * Start of cluster fuck
        * */

        if(msg.equals("#bot")) {
            // TODO: post commands
            message.reply("Bot is being re-written shitlord")
        }
        else if(msg.contains("#bot-sys")) {
            var runtime: Runtime = Runtime.getRuntime()
            var rb: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()
            var uptime: Long = rb.uptime / 1000
            var details: String = "CPU(s) -- " + runtime.availableProcessors() + ", OS -- " + System.getProperty("os.name") + ", Total memory to JVM -- " + runtime.totalMemory() + "KB, Uptime -- " + uptime
            message.reply(details)
        }
        else if(msg.contains("#filter")) {
            var log: log = log()

            var word: String = msg.substring(8, msg.length)
            word.trim()

            var count = log.filterText(word)
            message.reply("$word has been mentioned in $count messages")
        }
        else if(msg.contains("#kotlin")){
            message.reply("All of this bot is now in Kotlin ya dumb-dumb :)")
        }

        else if(msg.contains("#get-funcs")) {
            //filefunc.getFunctions()

            var funcSize: Int = 0
            for (i in 0..filefunc.max_size - 1) {
                println("functions length = ${_functions[i].length}")
                if (_functions[i].length < 1 && _actions[i].length < 1) {
                    funcSize = i - 1
                    break;
                }
            }
            message.reply("There are $funcSize functions in the file currently:")
            _functions.forEach { message.reply(it) }
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

            loop@ for(i in 0..users.size-1) {
                var usr = users[i]
                if(word.contains(usr.name.toLowerCase())) {
                    if(File(filename).exists()) {
                        //
                    }
                    else {
                        avatar = usr.avatarAsBytearray
                        file.appendBytes(avatar)
                    }
                    reply = "$word's avatar:"
                }
            }

            if(reply.length == 0) {
                message.reply("User not found")
            }
            else {
                message.reply(reply)
                message.channelReceiver.sendFile(file)
            }
        }
        else if(msg.contains("#paste")) {
            var paste: String? = msg.substring(7, msg.length)

            // dis bit a secret ;)
            message.reply(pasteText(paste))
        }

        else if(msg.contains("#stop")){
            if(user.equals("MCFrank")) {
                // Stop the bot
                System.exit(-1)
            }
        }

        // Reply with to a function in the file with the corresponding action
        loop@ for(i in 0..filefunc.max_size-1) {
            if(_functions[i].length < 1){
                break@loop
            }
            else {
                if(msg.contains(_functions[i])) {
                    message.reply(_actions[i])
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
        log.setNewFileText( ("[$channel] $user deleted message: $msg") )
        log.writeFile()
    }

    override fun onUserChangeName(api: DiscordAPI, user: User, oldName: String) {
        var log: log = log()
        log.setNewFileName(log._FILENAME)
    }

    override fun onServerMemberAdd(api: DiscordAPI, server: Server, user: User) {

    }

    override fun onServerMemberRemove(api: DiscordAPI, server: Server, user: User) {

    }

    override fun onServerJoin(api: DiscordAPI, server: Server) {

    }

    override fun onChannelChangeName(api: DiscordAPI, channel: Channel, oldName: String) {

    }

    override fun onChannelChangeTopic(api: DiscordAPI, channel: Channel, oldTopic: String) {

    }

    override fun onVoiceChannelChangeName(api: DiscordAPI, channel: VoiceChannel, oldName: String) {

    }

}