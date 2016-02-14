import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.split

/**
 * Created by unwin on 10/01/2016.
 */
class log {
    public val _FILENAME: String = "bot-log.txt"
    public val _STATE_FILENAME: String = "status-log.txt"
    var fileName: String = ""
    var text: String = ""

    /* Get the number of times filterText has been mentioned in the file */
    fun filterText(filterText: String): Int{
        var count: Int = -1

        try{
            var fstream: FileInputStream = FileInputStream(_FILENAME)
            var din: DataInputStream = DataInputStream(fstream)
            var br: BufferedReader = BufferedReader(InputStreamReader(din))
            var strLine: String = ""

            // Check for the filter word in each line and if so, increment the counter
            br.forEachLine {
                strLine = it
                var words = strLine.split(" ")
                for(s: String in words) {
                    if(s.equals(filterText)) {
                        ++count
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
    fun setNewFileText(newText: String) {
        text = newText
    }

    /* Set the file name */
    fun setNewFileName(newFileName: String) {
        fileName = newFileName
    }

    /* Write the file */
    fun writeFile() {
        try{
            var fWrite: FileWriter = FileWriter(fileName, true)
            var pWrite: PrintWriter = PrintWriter(fWrite)

            var dateFormat: DateFormat = SimpleDateFormat("dd/MM/yy HH:mm:ss")
            var date: Date = Date()

            pWrite.printf("%s --- " + dateFormat.format(date) + "%n",  text)

            pWrite.close()
        } catch(ex: Exception) {
            println("Some exception in writeFile fam, check it out --- ${ex.message}")
        }
    }

}