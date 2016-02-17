import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.text.split
import kotlin.text.trim

/**
 * Created by unwin on 10/01/2016.
 */
class file_functions{
    val _FUN_FILENAME: String = "functions.txt"
    val max_size: Int = 20
    var functions: Array<String> = Array(max_size, {i -> ""})
    var actions: Array<String> = Array(max_size, {i -> ""})


    /* Get the functions from the file */
    fun getFunctions() {
        var temp: Array<String> = Array(max_size, {i -> ""})
        var mainL: mainListener = mainListener()

        var file = File(_FUN_FILENAME)
        var a = 0

        file.forEachLine {
            temp[a] = it
            ++a
        }

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

    /* Appends a function to functions.txt */
    fun writeFunction(newVar: String) {
        getFunctions()

        var newFuncAct = newVar.split(':')
        var function = newFuncAct[0].trim()
        var action = newFuncAct[1].trim()

        for(a in 0..max_size-1) {
            if(function == functions[a]) {
                // Dunno what to do about function conflict yet
            }
        }

        var file = File(_FUN_FILENAME)
        file.appendText("$function : $action\n")

        getFunctions()

    }

    /* TODO: rewrite this - editFunctions */
    fun editFunctions(newFunc: String, newAction: String) {
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

}