import java.io.FileReader
import java.util.*
import kotlin.text.split
import kotlin.text.trim

/**
 * Created by unwin on 10/01/2016.
 */
class fileFunctions{
    val _FUN_FILENAME: String = "functions.txt"
    val max_size: Int = 20
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
                mainL._functions[i] = functions[i]
                actions[i] = ssTemp[1].trim()
                mainL._actions[i] = actions[i]

            }
        }



    }

    /* TODO: Write function(s) to the file */
    public fun writeFunctions(newFunctions: Array<String>) {
        getFunctions()

        var nFunctions: Array<String> = Array<String> (max_size, {i -> ""} )
        var nActions: Array<String> = Array<String> (max_size, {i -> ""} )
        var nTemp: List<String>

        for(n in 0..newFunctions.size-1) {
            nTemp = newFunctions[n].split(" : ")

            nFunctions[n] = nTemp[0]
            println("new function = ${nFunctions[n]}")
            nActions[n] = nTemp[1]
            println("new action = ${nActions[n]}")

        }


    }

    /* TODO: check this works - editFunctions */
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

    /* TODO: Figure out wtf I was doing with this */
    public fun listFunctions() {

        for(i in 0..max_size) {
            if(functions[i].length > 0 && actions[i].length > 0) {
                var temp: Array<String> = Array<String>(max_size, {i -> ""})
            }
        }

    }

}