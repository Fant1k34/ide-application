package application.compiler.lexer.extracter

import application.compiler.LanguageLabel
import application.compiler.Location
import application.compiler.lexer.Token
import application.compiler.lexer.TokenCreator

sealed class OperatorExtractor(private val operator: LanguageLabel) : TokenExtractor {
    override fun isToken(input: String, currentIndex: Int): Boolean {
        operator.label.forEachIndexed { index, c ->
            val inputIndex = currentIndex + index
            if (inputIndex >= input.length || input[inputIndex] != c) {
                return false
            }
        }
        return true
    }

    override fun buildToken(input: String, currentIndex: Int, loc: Location): Token = TokenCreator.create(operator, loc)
}

object PlusTokenExtractor : OperatorExtractor(LanguageLabel.PLUS)
object MinusTokenExtractor : OperatorExtractor(LanguageLabel.MINUS)
object TimesTokenExtractor: OperatorExtractor(LanguageLabel.TIMES)
object DivideTokenExtractor : OperatorExtractor(LanguageLabel.DIVIDE)
object AndTokenExtractor : OperatorExtractor(LanguageLabel.LOGIC_AND)
object OrTokenExtractor : OperatorExtractor(LanguageLabel.LOGIC_OR)
object NotEqualTokenExtractor : OperatorExtractor(LanguageLabel.NOT_EQUAL)
object EqualTokenExtractor : OperatorExtractor(LanguageLabel.EQUAL)
object GreaterOrEqualThanTokenExtractor : OperatorExtractor(LanguageLabel.GREATER_OR_EQUAL)
object GreaterThanTokenExtractor : OperatorExtractor(LanguageLabel.GREATER)
object LessOrEqualThanTokenExtractor : OperatorExtractor(LanguageLabel.LESS_OR_EQUAL)
object LessThanTokenExtractor : OperatorExtractor(LanguageLabel.LESS)
object AssignTokenExtractor : OperatorExtractor(LanguageLabel.ASSIGN)
object NotTokenExtractor : OperatorExtractor(LanguageLabel.LOGIC_NOT)