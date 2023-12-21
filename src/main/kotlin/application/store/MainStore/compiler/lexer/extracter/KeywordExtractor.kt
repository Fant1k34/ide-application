package application.store.MainStore.compiler.lexer.extracter

import application.store.MainStore.compiler.LanguageLabel
import application.store.MainStore.compiler.Location
import application.store.MainStore.compiler.lexer.Token
import application.store.MainStore.compiler.lexer.TokenCreator

abstract class KeywordTokenExtractor(private val keyword: LanguageLabel) : TokenExtractor {
    override fun isToken(input: String, currentIndex: Int): Boolean {
        val token = keyword.label

        token.forEachIndexed { index, c ->
            val inputIndex = currentIndex + index
            if (inputIndex >= input.length || input[inputIndex] != c) {
                return false
            }
        }
        if (currentIndex + token.length < input.length) {
            val nextCharAfterToken = input[currentIndex + token.length]

            if (nextCharAfterToken.isLetterOrDigit()) {
                return false
            }
        }
        return true
    }

    override fun buildToken(input: String, currentIndex: Int, loc: Location): Token = TokenCreator.create(keyword, loc)

}

object VarTokenExtractor : KeywordTokenExtractor(LanguageLabel.VARIABLE)
object TrueTokenExtractor : KeywordTokenExtractor(LanguageLabel.TRUE)
object FalseTokenExtractor : KeywordTokenExtractor(LanguageLabel.FALSE)
object IfTokenExtractor : KeywordTokenExtractor(LanguageLabel.IF)
object ElseTokenExtractor: KeywordTokenExtractor(LanguageLabel.ELSE)
object WhileTokenExtractor : KeywordTokenExtractor(LanguageLabel.WHILE)
object FuncTokenExtractor : KeywordTokenExtractor(LanguageLabel.FUNCTION)
object ReturnTokenExtractor: KeywordTokenExtractor(LanguageLabel.RETURN)
object ProcTokenExtractor : KeywordTokenExtractor(LanguageLabel.PROCEDURE)
object PrintTokenExtractor: KeywordTokenExtractor(LanguageLabel.PRINT)