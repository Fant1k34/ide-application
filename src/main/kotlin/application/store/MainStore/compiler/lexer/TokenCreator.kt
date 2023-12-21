package application.store.MainStore.compiler.lexer

import application.store.MainStore.compiler.LanguageLabel
import application.store.MainStore.compiler.Location

object TokenCreator {

    fun create(token: LanguageLabel, location: Location): Token =
        when(token) {
            LanguageLabel.VARIABLE -> VarToken(location)
            LanguageLabel.TRUE -> TrueToken(location)
            LanguageLabel.FALSE -> FalseToken(location)
            LanguageLabel.IF -> IfToken(location)
            LanguageLabel.ELSE -> ElseToken(location)
            LanguageLabel.WHILE -> WhileToken(location)
            LanguageLabel.FUNCTION -> FuncToken(location)
            LanguageLabel.RETURN -> ReturnToken(location)
            LanguageLabel.PROCEDURE -> ProcToken(location)
            LanguageLabel.PRINT -> PrintToken(location)
            LanguageLabel.SEMICOLON -> SemicolonToken(location)
            LanguageLabel.OPENING_BRACKET -> OpeningBracketToken(location)
            LanguageLabel.CLOSING_BRACKET -> ClosingBracketToken(location)
            LanguageLabel.OPENING_CURLY_BRACKET -> OpeningCurlyBracketToken(location)
            LanguageLabel.CLOSING_CURLY_BRACKET -> ClosingCurlyBracketToken(location)
            LanguageLabel.COMMA -> CommaToken(location)
            LanguageLabel.PLUS -> PlusToken(location)
            LanguageLabel.MINUS -> MinusToken(location)
            LanguageLabel.TIMES -> TimesToken(location)
            LanguageLabel.DIVIDE -> DivideToken(location)
            LanguageLabel.LOGIC_AND -> AndToken(location)
            LanguageLabel.LOGIC_OR -> OrToken(location)
            LanguageLabel.NOT_EQUAL -> NotEqualToken(location)
            LanguageLabel.EQUAL -> EqualToken(location)
            LanguageLabel.GREATER_OR_EQUAL -> GreaterOrEqualThanToken(location)
            LanguageLabel.GREATER -> GreaterThanToken(location)
            LanguageLabel.LESS_OR_EQUAL -> LessOrEqualThanToken(location)
            LanguageLabel.LESS -> LessThanToken(location)
            LanguageLabel.ASSIGN -> AssignToken(location)
            LanguageLabel.LOGIC_NOT -> NotToken(location)
        }

}