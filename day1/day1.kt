import java.io.File

const val LOOP_DIAL_VALUE: Int = 100
const val DIAL_START_VALUE: Int = 50

var dialValue = DIAL_START_VALUE
var zeroCount = 0

/**
 * This program implements the solutions to parts 1 and 2 of day 1.
 *
 * @param args In order, an input file and the challenge part that the user wants to run (1 or 2)
 */
fun main(args: Array<String>) {
        if (args.size != 2) {
                val error =
                        "Please give an input file path and the challenge part as runtime arguments"
                throw RuntimeException(error)
        }

        val inputFile: String = args[0]
        val challengePart: Int = args[1].toInt()
        // Choose the function depending on the chosen part of the challenge
        val challengeFunction: (String) -> Unit =
                when (challengePart) {
                        1 -> ::countZero
                        2 -> ::rotateDial
                        else -> {
                                val error = "Challenge part $challengePart is not implemented!"
                                throw IllegalArgumentException(error)
                        }
                }
        File(inputFile).forEachLine(Charsets.UTF_8, challengeFunction)
        println("The password is $zeroCount.")
}

// part 1
/**
 * Counts the total number of times the dial ends up on zero
 *
 * @param line The input line to parse
 */
fun countZero(line: String) {
        val direction = Direction.of(line)
        val clicks = line.substring(1, line.length).toInt()
        dialValue = positiveModulus(dialValue + direction.sign * clicks, LOOP_DIAL_VALUE)
        if (dialValue == 0) ++zeroCount
}

/**
 * Computes a positive modulus on the given value.
 *
 * @param number the number to apply the modulus on
 * @param mod the modulus
 *
 * @return a positive modulus
 */
fun positiveModulus(number: Int, mod: Int): Int {
        var result: Int = number % mod
        if (result < 0) result += mod

        return result
}

// part 2
/**
 * Counts the total number of times the dial is set on zero.
 *
 * @param line the input line to parse
 */
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

/**
 * Parses a string to return the corresponding positive number of clicks.
 *
 * @param line the input line
 *
 * @return the positive number of clicks
 */
fun getClicks(line: String): Int {
        val value = line.substring(1, line.length).toInt()

        return value
}

/**
 * Parses a string to return the corresponding number of clicks.
 *
 * @param line the input line
 *
 * @return the number of clicks
 */
fun getSignedClicks(line: String): Int {
        var clicks = getClicks(line)

        return if (Direction.of(line) == Direction.LEFT) -clicks else clicks
}

/**
 * This enum class represents a direction of rotation on the dial.
 *
 * @param sign the sign corresponding to the rotation direction
 */
enum class Direction(val sign: Int) {
        LEFT(-1),
        RIGHT(1);

        companion object {
                /**
                 * Converts a character to a Direction type.
                 *
                 * @param character the character to convert to a Direction
                 *
                 * @return the corresponding direction
                 */
                fun of(character: Char): Direction {
                        if (character == 'R') return RIGHT
                        if (character == 'L') return LEFT

                        throw IllegalArgumentException("Unexpected character $character")
                }

                /**
                 * Retrieves the first character of a line to convert it to a Direction type.
                 *
                 * @param line the line to convert to a Direction
                 *
                 * @return the corresponding direction
                 */
                fun of(line: String): Direction {
                        return Direction.of(line[0])
                }
        }
}
