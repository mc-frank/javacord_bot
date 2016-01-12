import com.sun.xml.internal.fastinfoset.util.StringArray
import java.io.FileReader
import java.util.*
import kotlin.collections.setOf
import kotlin.text.single
import kotlin.text.split
import kotlin.text.trim

/**
 * Created by unwin on 10/01/2016.
 */
class fileFunctions {
    val _FUN_FILENAME: String = "functions.txt"
    val max_size: Int = 100
    var functions: Array<String> = Array<String>(max_size, {i -> ""})
    var actions: Array<String> = Array<String>(max_size, {i -> ""})

    /* Get the functions from the file */
    public fun getFunctions() {
        var temp: Array<String> = Array<String>(max_size, {i -> ""})
        var mainL: mainListener = mainListener()

        var scanner: Scanner = Scanner(FileReader(_FUN_FILENAME))
        var str: String?
        var a: Int = 0

        loop@ for(a in 0..max_size-1) {
            if(scanner.hasNext()) {
                str = scanner.nextLine()
                temp[a] = str
            }
        }

        scanner.close()

        for(i in 0..max_size-1) {
            if(temp[i].length > 1) {
                var sTemp = temp[i]

                var ssTemp = sTemp.split(" : ")

                functions[i] = ssTemp[0].trim()
                actions[i] = ssTemp[1].trim()
                //println("function = ${mainL._functions[i]}")
                //println("action = ${mainL._actions[i]}")

            }
        }


    }

    /* Write function(s) to the file */
    public fun writeFunctions() {

    }

    /* editFunctions */
    public fun editFunctions(newFunc: String, newAction: String) {
        getFunctions()
        var tempFunctions: Array<String> = arrayOf<String>()
        var tempActions: Array<String> = arrayOf<String>()

        tempFunctions = functions

        for(i in 0..max_size) {
            if(newFunc.equals(tempFunctions[i])) {
                actions[i] = newAction
            }
        }
    }

    public fun listFunctions() {

        for(i in 0..max_size) {
            if(functions[i].length > 0 && actions[i].length > 0) {
                var temp: Array<String> = Array<String>(max_size, {i -> ""})
            }
        }

    }
}