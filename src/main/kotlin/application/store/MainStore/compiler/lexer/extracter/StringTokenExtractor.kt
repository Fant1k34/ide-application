package application.store.MainStore.compiler.lexer.extracter

import application.store.MainStore.compiler.Location
import application.store.MainStore.compiler.lexer.StringToken
import application.store.MainStore.compiler.lexer.Token
import java.lang.StringBuilder

object StringTokenExtractor : TokenExtractor {
    private const val STRING_START_SYMBOL = '\"'

    override fun isToken(input: String, currentIndex: Int): Boolean {
        if (currentIndex < input.length && input[currentIndex] != STRING_START_SYMBOL) {
            return false
        }
        var index = currentIndex + 1

        while (index < input.length && input[index] != STRING_START_SYMBOL) {
            index++
        }

        return index < input.length && input[index] == STRING_START_SYMBOL
    }

    override fun buildToken(input: String, currentIndex: Int, loc: Location): Token {
        var index = currentIndex + 1
        val sb = StringBuilder()

        while (index < input.length && input[index] != STRING_START_SYMBOL) {
            sb.append(input[index])
            index++
        }

        return StringToken(sb.toString(), loc)
    }
}