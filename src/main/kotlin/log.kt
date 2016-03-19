import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.split

/**
 * Created by unwin on 10/01/2016.
 */
class log() {

    val _LOG_FILENAME: String = "bot-log.txt"
    val _STATE_FILENAME: String = "status-log.txt"
    private var fileName: String = ""
    private var text: String = ""


    /* Get the number of times filterText has been mentioned in the file */
    fun filter_text(filterText: String): Int{
        var count: Int = -1

        try{
            var fstream: FileInputStream = FileInputStream(_LOG_FILENAME)
            var din: DataInputStream = DataInputStream(fstream)
            var br: BufferedReader = BufferedReader(InputStreamReader(din))
            var strLine: String = ""

            // Check for the filter word in each line and if so, increment the counter
            br.forEachLine {
                strLine = it
                var words = strLine.split(" ")
                if(strLine.contains("JoodBot >")) {
                    // Ignore counting the line if it's from JoodBot
                } else {
                    for(s: String in words) {
                        if(s.toLowerCase().contains(filterText)) {
                            ++count
                        }
                    }
                }
            }

        }
        catch(ex: Exception) {
            println("Error in filterText: $ex")
        }


        return count
    }

    /* Set the text of the file */
    fun set_file_text(newText: String) {
        text = newText
    }

    /* Set the file name */
    fun set_file_name(newFileName: String) {
        fileName = newFileName
    }

    /* Write the file */
    fun write_file() {
        try{
            var file = File(fileName)

            var dateFormat: DateFormat = SimpleDateFormat("dd/MM/yy HH:mm:ss")
            var date: Date = Date()

            var fileText = "$text --- ${dateFormat.format(date)} \r\n"
            file.appendText(fileText)
        } catch(ex: Exception) {
            println("Some exception in writeFile --- ${ex.message}")
        }
    }

}