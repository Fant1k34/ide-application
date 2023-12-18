package application.compiler.lexer.extracter

import application.compiler.Location
import application.compiler.lexer.NumberToken
import application.compiler.lexer.Token


object NumberExtractor : TokenExtractor {

    override fun isToken(input: String, currentIndex: Int): Boolean =
        (currentIndex < input.length) && input[currentIndex].isDigit()

    override fun buildToken(input: String, currentIndex: Int, loc: Location): Token {
        var index = currentIndex

        val sb = StringBuilder()
        while (index < input.length && Character.isDigit(input[index])) {
            sb.append(input[index++])
        }

        return NumberToken(sb.toString(), loc)
    }
}