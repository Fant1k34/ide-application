package application.store.MainStore.compiler.lexer.extracter

import application.store.MainStore.compiler.LanguageLabel
import application.store.MainStore.compiler.Location
import application.store.MainStore.compiler.lexer.Token
import application.store.MainStore.compiler.lexer.TokenCreator

sealed class SyntaxSignExtractor(private val sign: LanguageLabel) : TokenExtractor {
    override fun isToken(input: String, currentIndex: Int): Boolean {
        sign.label.forEachIndexed { index, c ->
            val inputIndex = currentIndex + index
            if (inputIndex >= input.length || input[inputIndex] != c) {
                return false
            }
        }
        return true
    }

    override fun buildToken(input: String, currentIndex: Int, loc: Location): Token = TokenCreator.create(sign, loc)
}

object SemicolonTokenExtractor : SyntaxSignExtractor(LanguageLabel.SEMICOLON)
object OpeningBracketTokenExtractor : SyntaxSignExtractor(LanguageLabel.OPENING_BRACKET)
object ClosingBracketTokenExtractor : SyntaxSignExtractor(LanguageLabel.CLOSING_BRACKET)
object OpeningCurlyBracketTokenExtractor : SyntaxSignExtractor(LanguageLabel.OPENING_CURLY_BRACKET)
object ClosingCurlyBracketTokenExtractor : SyntaxSignExtractor(LanguageLabel.CLOSING_CURLY_BRACKET)
object CommaTokenExtractor : SyntaxSignExtractor(LanguageLabel.COMMA)