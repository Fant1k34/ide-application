package application.store.MainStore.compiler.lexer

import application.store.MainStore.compiler.LanguageLabel
import application.store.MainStore.compiler.Location

sealed class Token(val value: String, val loc: Location) {
    open val length: Int
        get() = value.length

    override fun toString(): String {
        return "${this::class.java.simpleName}('$value')($loc)"
    }
}

sealed class Operator(value: String, loc: Location) : Token(value, loc)
sealed class Keyword(value: String, loc: Location): Token(value, loc)
sealed class SyntaxSign(value: String, loc: Location): Token(value, loc)
sealed interface BracketToken


class VarToken(loc: Location) : Keyword(LanguageLabel.VARIABLE.label, loc)
class TrueToken(loc: Location) : Keyword(LanguageLabel.TRUE.label, loc)
class FalseToken(loc: Location) : Keyword(LanguageLabel.FALSE.label, loc)
class IfToken(loc: Location) : Keyword(LanguageLabel.IF.label, loc)
class ElseToken(loc: Location) : Keyword(LanguageLabel.ELSE.label, loc)
class WhileToken(loc: Location) : Keyword(LanguageLabel.WHILE.label, loc)
class FuncToken(loc: Location) : Keyword(LanguageLabel.FUNCTION.label, loc)
class ReturnToken(loc: Location) : Keyword(LanguageLabel.RETURN.label, loc)
class ProcToken(loc: Location) : Keyword(LanguageLabel.PROCEDURE.label, loc)
class PrintToken(loc: Location) : Keyword(LanguageLabel.PRINT.label, loc)


class SemicolonToken(loc: Location) : SyntaxSign(LanguageLabel.SEMICOLON.label, loc)
class OpeningBracketToken(loc: Location) : SyntaxSign(LanguageLabel.OPENING_BRACKET.label, loc), BracketToken
class ClosingBracketToken(loc: Location) : SyntaxSign(LanguageLabel.CLOSING_BRACKET.label, loc), BracketToken
class OpeningCurlyBracketToken(loc: Location) : SyntaxSign(LanguageLabel.OPENING_CURLY_BRACKET.label, loc)
class ClosingCurlyBracketToken(loc: Location) : SyntaxSign(LanguageLabel.CLOSING_CURLY_BRACKET.label, loc)
class CommaToken(loc: Location) : SyntaxSign(LanguageLabel.COMMA.label, loc)


class PlusToken(loc: Location) : Operator(LanguageLabel.PLUS.label, loc)
class MinusToken(loc: Location) : Operator(LanguageLabel.MINUS.label, loc)
class TimesToken(loc: Location) : Operator(LanguageLabel.TIMES.label, loc)
class DivideToken(loc: Location) : Operator(LanguageLabel.DIVIDE.label, loc)
class AndToken(loc: Location) : Operator(LanguageLabel.LOGIC_AND.label, loc)
class OrToken(loc: Location) : Operator(LanguageLabel.LOGIC_OR.label, loc)
class NotEqualToken(loc: Location) : Operator(LanguageLabel.NOT_EQUAL.label, loc)
class EqualToken(loc: Location) : Operator(LanguageLabel.EQUAL.label, loc)
class GreaterOrEqualThanToken(loc: Location) : Operator(LanguageLabel.GREATER_OR_EQUAL.label, loc)
class GreaterThanToken(loc: Location) : Operator(LanguageLabel.GREATER.label, loc)
class LessOrEqualThanToken(loc: Location) : Operator(LanguageLabel.LESS_OR_EQUAL.label, loc)
class LessThanToken(loc: Location) : Operator(LanguageLabel.LESS.label, loc)
class AssignToken(loc: Location) : Operator(LanguageLabel.ASSIGN.label, loc)
class NotToken(loc: Location) : Operator(LanguageLabel.LOGIC_NOT.label, loc)


class IdentifierToken(value: String, loc: Location) : Token(value, loc)
class NumberToken(value: String, loc: Location) : Token(value, loc)
class StringToken(value: String, loc: Location) : Token(value, loc) {
    override val length: Int
        get() = super.length + 2
}
