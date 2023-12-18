package application.compiler.lexer.extracter

import application.compiler.Location
import application.compiler.lexer.Token

sealed interface TokenExtractor {

    fun isToken(input: String, currentIndex: Int): Boolean
    fun buildToken(input: String, currentIndex: Int, loc: Location): Token
}
