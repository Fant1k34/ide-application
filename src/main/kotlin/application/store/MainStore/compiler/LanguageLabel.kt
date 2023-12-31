package application.store.MainStore.compiler

enum class LanguageLabel(val label: String) {
    VARIABLE("var"),
    TRUE("true"),
    FALSE("false"),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    FUNCTION("func"),
    RETURN("return"),
    PROCEDURE("proc"),
    PRINT("print"),
    SEMICOLON(";"),
    OPENING_BRACKET("("),
    CLOSING_BRACKET(")"),
    OPENING_CURLY_BRACKET("{"),
    CLOSING_CURLY_BRACKET("}"),
    COMMA(","),
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDE("/"),
    LOGIC_AND("&&"),
    LOGIC_OR("||"),
    NOT_EQUAL("!="),
    EQUAL("=="),
    GREATER_OR_EQUAL(">="),
    GREATER(">"),
    LESS_OR_EQUAL("<="),
    LESS("<"),
    ASSIGN("="),
    LOGIC_NOT("!")

}