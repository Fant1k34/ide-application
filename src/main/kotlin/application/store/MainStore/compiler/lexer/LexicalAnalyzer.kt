package application.store.MainStore.compiler.lexer

import application.store.MainStore.compiler.Location
import application.store.MainStore.compiler.lexer.extracter.AndTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.AssignTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.ClosingBracketTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.ClosingCurlyBracketTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.CommaTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.DivideTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.ElseTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.EqualTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.FalseTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.FuncTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.GreaterOrEqualThanTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.GreaterThanTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.IdentifierExtractor
import application.store.MainStore.compiler.lexer.extracter.IfTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.LessOrEqualThanTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.LessThanTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.MinusTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.NotEqualTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.NotTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.NumberExtractor
import application.store.MainStore.compiler.lexer.extracter.OpeningBracketTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.OpeningCurlyBracketTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.OrTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.PlusTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.PrintTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.ProcTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.ReturnTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.SemicolonTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.StringTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.TimesTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.TrueTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.VarTokenExtractor
import application.store.MainStore.compiler.lexer.extracter.WhileTokenExtractor

val KEYWORD_EXTRACTOR_CHAIN = buildList {
    add(VarTokenExtractor)
    add(TrueTokenExtractor)
    add(FalseTokenExtractor)
    add(IfTokenExtractor)
    add(ElseTokenExtractor)
    add(WhileTokenExtractor)
    add(FuncTokenExtractor)
    add(ReturnTokenExtractor)
    add(ProcTokenExtractor)
    add(PrintTokenExtractor)
}

val SYNTAX_SIGN_EXTRACTOR_CHAIN = buildList {
    add(SemicolonTokenExtractor)
    add(OpeningBracketTokenExtractor)
    add(ClosingBracketTokenExtractor)
    add(OpeningCurlyBracketTokenExtractor)
    add(ClosingCurlyBracketTokenExtractor)
    add(CommaTokenExtractor)
}

val OPERATOR_EXTRACTOR_CHAIN = buildList {
    add(PlusTokenExtractor)
    add(MinusTokenExtractor)
    add(TimesTokenExtractor)
    add(DivideTokenExtractor)
    add(AndTokenExtractor)
    add(OrTokenExtractor)
    add(NotEqualTokenExtractor)
    add(EqualTokenExtractor)
    add(GreaterOrEqualThanTokenExtractor)
    add(GreaterThanTokenExtractor)
    add(LessOrEqualThanTokenExtractor)
    add(LessThanTokenExtractor)
    add(AssignTokenExtractor)
    add(NotTokenExtractor)
}

val TOKEN_EXTRACTOR_CHAIN = buildList {
    addAll(KEYWORD_EXTRACTOR_CHAIN)
    addAll(SYNTAX_SIGN_EXTRACTOR_CHAIN)
    addAll(OPERATOR_EXTRACTOR_CHAIN)
    add(NumberExtractor)
    add(StringTokenExtractor)
    add(IdentifierExtractor)
}

class LexicalAnalyzer(private val input: String) {
    private var currentIndex = 0
    private var lineNumber = 1
    private var columnNumber = 1

    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()
        skipWhitespace()
        while (currentIndex < input.length) {

            val tokenWasFound = TOKEN_EXTRACTOR_CHAIN.any {
                if (it.isToken(input, currentIndex)) {
                    val token = it.buildToken(input, currentIndex, Location(lineNumber, columnNumber))
                    tokens.add(token)
                    currentIndex += token.length
                    columnNumber += token.length
                    true
                }
                else {
                    false
                }
            }

            if (!tokenWasFound) {
                throw IllegalArgumentException("Unexpected character '${input[currentIndex]}' at position $currentIndex.")
            }

            skipWhitespace()
        }
        return tokens
    }

    private fun peek(): Char? = if (currentIndex < input.length) input[currentIndex] else null

    private fun skipWhitespace() {
        while (currentIndex < input.length && (peek()?.isWhitespace() == true)) {
            if (peek() == ' ') columnNumber++
            else if (peek() == '\n'){
                lineNumber++
                columnNumber = 1
            }
            currentIndex++
        }
    }

}