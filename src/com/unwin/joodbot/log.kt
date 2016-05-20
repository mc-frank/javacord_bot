package com.unwin.joodbot

import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.split

/**
 * Created by unwin on 10/01/2016.
 */
class log() {

    val _LOG_FILENAME: String = "bot-log"
    var fileName: String = ""
    var text: String = ""


    /* Get the number of times filterText has been mentioned in the file */
    fun filter(filter_text: String, server_id: String): Int {
        var count = 0
        var file = File(_LOG_FILENAME + "-" + server_id + ".txt")
        var text = file.readText().toLowerCase()

        var index = text.indexOf(filter_text)
        while(index != -1) {
            count++
            text = text.substring(index + 1)
            index = text.indexOf(filter_text)
        }

        return count
    }

    /* Write the file */
    fun write_file() {
        try{
            var file = File(fileName + ".txt")

            var dateFormat: DateFormat = SimpleDateFormat("dd/MM/yy HH:mm:ss")
            var date: Date = Date()

            var fileText = "$text --- ${dateFormat.format(date)} \r\n"
            file.appendText(fileText)
        } catch(ex: Exception) {
            println("Some exception in writeFile --- ${ex.message}")
        }
    }

}