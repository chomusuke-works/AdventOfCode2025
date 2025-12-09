import java.io.File

const val LOOP_DIAL_VALUE: Int = 100
const val DIAL_START_VALUE: Int = 50

var dialValue = DIAL_START_VALUE
var zeroCount = 0

fun main(args: Array<String>) {
    if (args.size != 2)
            throw RuntimeException(
                    "Please give an input file path and the challenge part as inputs"
            )
    val inputFile: String = args[0]
    val challengePart: Int = args[1].toInt()
    val challengeFunction: (String) -> Unit
    if (challengePart == 1) challengeFunction = ::countZero else challengeFunction = ::rotateDial
    File(inputFile).forEachLine(Charsets.UTF_8, challengeFunction)
    println("The password is $zeroCount.")
}

// part 1
fun countZero(line: String) {
    val direction = Direction.of(line)
    val clicks = line.substring(1, line.length).toInt()
    dialValue = positiveModulus(dialValue + direction.sign * clicks, LOOP_DIAL_VALUE)
    if (dialValue == 0) ++zeroCount
}

// part 2
fun rotateDial(line: String) {
    var clicks = getClicks(line)
    val direction = Direction.of(line)

    if (direction == Direction.LEFT) {
        clicks -= dialValue

        if (clicks >= 0) {
            if (dialValue != 0) ++zeroCount
            dialValue = (LOOP_DIAL_VALUE - clicks % LOOP_DIAL_VALUE) % LOOP_DIAL_VALUE
            zeroCount += clicks / LOOP_DIAL_VALUE
        } else {
            dialValue = Math.abs(clicks)
        }
    } else {
        dialValue = dialValue + clicks
        zeroCount += dialValue / LOOP_DIAL_VALUE
        dialValue = dialValue % LOOP_DIAL_VALUE
    }
}

fun positiveModulus(number: Int, mod: Int): Int {
    var result: Int = number % mod
    if (result < 0) result += mod

    return result
}

// Parses a string to return the corresponding number of clicks
fun getClicks(line: String): Int {
    val value = line.substring(1, line.length).toInt()

    return value
}

fun getSignedClicks(line: String): Int {
    var clicks = getClicks(line)

    return if (Direction.of(line) == Direction.LEFT) -clicks else clicks
}

enum class Direction(val sign: Int) {
    LEFT(-1),
    RIGHT(1);

    companion object {
        fun of(character: Char): Direction {
            if (character == 'R') return RIGHT
            if (character == 'L') return LEFT

            throw IllegalArgumentException("Unexpected character $character")
        }

        fun of(line: String): Direction {
            return Direction.of(line[0])
        }
    }
}
