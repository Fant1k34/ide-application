package application.store.MainStore.compiler.lexer.extracter

import application.store.MainStore.compiler.Location
import application.store.MainStore.compiler.lexer.Token

sealed interface TokenExtractor {

    fun isToken(input: String, currentIndex: Int): Boolean
    fun buildToken(input: String, currentIndex: Int, loc: Location): Token
}
