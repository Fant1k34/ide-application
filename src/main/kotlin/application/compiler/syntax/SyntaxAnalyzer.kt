package application.compiler.syntax

import application.compiler.lexer.AssignToken
import application.compiler.lexer.ClosingBracketToken
import application.compiler.lexer.ClosingCurlyBracketToken
import application.compiler.lexer.CommaToken
import application.compiler.lexer.DivideToken
import application.compiler.lexer.ElseToken
import application.compiler.lexer.FalseToken
import application.compiler.lexer.FuncToken
import application.compiler.lexer.IdentifierToken
import application.compiler.lexer.IfToken
import application.compiler.lexer.MinusToken
import application.compiler.lexer.NumberToken
import application.compiler.lexer.OpeningBracketToken
import application.compiler.lexer.OpeningCurlyBracketToken
import application.compiler.lexer.Operator
import application.compiler.lexer.PlusToken
import application.compiler.lexer.PrintToken
import application.compiler.lexer.ProcToken
import application.compiler.lexer.ReturnToken
import application.compiler.lexer.SemicolonToken
import application.compiler.lexer.StringToken
import application.compiler.lexer.TimesToken
import application.compiler.lexer.Token
import application.compiler.lexer.TrueToken
import application.compiler.lexer.VarToken
import application.compiler.lexer.WhileToken
import java.lang.reflect.Type

class SyntaxAnalyzer(private val tokens: List<Token>) {
    private var currentTokenIndex = 0

    fun parse(): CstNode {
        val nodes = ArrayDeque<CstNode>()
        while (peekToken() != null) {
            nodes.add(node())
        }
        return CstNode(children = nodes)
    }

    private fun node() =
        when (peekToken()) {
            is ProcToken -> procDeclaration()
            is FuncToken -> funcDeclaration()
            is VarToken -> varDeclaration()
            is IfToken -> ifDeclaration()
            is WhileToken -> whileDeclaration()
            is PrintToken -> printCall()
            is IdentifierToken -> assignmentOrFuncOrProcCall()
            else -> error("${peekToken()} cannot be recognized")
        }

    private fun nodeInBlock() : CstNode? =
        when(peekToken()) {
            is VarToken -> varDeclaration()
            is IfToken -> ifDeclaration()
            is WhileToken -> whileDeclaration()
            is PrintToken -> printCall()
            is IdentifierToken -> assignmentOrFuncOrProcCall()
            is ReturnToken -> returnStmt()
            else -> null
        }

    private fun assignmentOrFuncOrProcCall(): CstNode {
        val id = consumeIdToken()

        val transitions = sequenceOf(::assignment, ::funcOrProcCall)
        val node = transitions.mapNotNull { it.invoke(id) }
            .firstOrNull() ?: error("${peekToken()} cannot be recognized")

        consumeToken(SemicolonToken::class.java)
        return node
    }

    private fun funcOrProcCall(funcOrProcName: Token): CstNode? {
        if (peekToken() is OpeningBracketToken) {
            consumeToken(OpeningBracketToken::class.java)
            val params = callParams()
            consumeToken(ClosingBracketToken::class.java)
            return CstNode(funcOrProcName).apply { children.add(params) }
        }
        return null
    }

    private fun callParams(): CstNode {
        if (peekToken() is ClosingBracketToken) return CstNode()

        val firstParam = expression()
        val nextParams = nextCallParams() ?: CstNode()
        nextParams.children.addFirst(firstParam)

        return nextParams
    }

    private fun nextCallParams(): CstNode? {
        if (peekToken() is CommaToken) {
            consumeToken(CommaToken::class.java)
            return callParams().also { if (it.children.isEmpty()) error("Expression was not found") }
        }
        return null
    }

    private fun assignment(variableName: Token): CstNode? {
        if (peekToken() is AssignToken) {
            val assignToken = consumeToken(AssignToken::class.java)
            return CstNode(assignToken).apply {
                children.add(CstNode(variableName))
                children.add(expression())
            }
        }
        return null
    }

    /**
     * print(...);
     */
    private fun printCall(): CstNode {
        val printToken = consumeToken(PrintToken::class.java)
        return CstNode(printToken).apply {
            children.add(printParam())
            consumeToken(SemicolonToken::class.java)
        }
    }

    /**
     * (...)
     */
    private fun printParam(): CstNode {
        consumeToken(OpeningBracketToken::class.java)
        val param = expression()
        consumeToken(ClosingBracketToken::class.java)

        return param
    }

    /**
     * while (...) { ... }
     */
    private fun whileDeclaration(): CstNode {
        val whileToken = consumeToken(WhileToken::class.java)
        return CstNode(whileToken).apply {
            children.add(condition())
            children.add(blockDeclaration())
        }
    }

    /**
     * if (...) { ... } else { ... }
     */
    private fun ifDeclaration(): CstNode {
        val ifToken = consumeToken(IfToken::class.java)
        return CstNode(ifToken).apply {
            children.add(condition())
            children.add(blockDeclaration())
            elseDeclaration()?.also { children.add(it) }
        }
    }

    /**
     * (...)
     */
    private fun condition(): CstNode {
        consumeToken(OpeningBracketToken::class.java)
        val expr = expression()
        consumeToken(ClosingBracketToken::class.java)
        return expr
    }

    /**
     * else {...}
     */
    private fun elseDeclaration(): CstNode? {
        if (peekToken() is ElseToken) {
            val elseToken = consumeToken(ElseToken::class.java)
            return CstNode(elseToken).apply { children.add(blockDeclaration()) }
        }
        return null
    }

    /**
     * var ...;
     */
    private fun varDeclaration(): CstNode {
        val varToken = consumeToken(VarToken::class.java)
        return CstNode(varToken).apply {
            children.add(varsDeclaration())
            consumeToken(SemicolonToken::class.java)
        }
    }

    /**
     * ... = ...
     */
    private fun varsDeclaration(): CstNode {
        val id = identifier()
        val assignToken = consumeToken(AssignToken::class.java)

        val firstVar = CstNode(assignToken).apply {
            children.add(id)
            children.add(expression())
        }

        val nextVars = nextVarsDeclaration() ?: CstNode()
        nextVars.children.addFirst(firstVar)

        return nextVars

    }

    /**
     * , ...
     */
    private fun nextVarsDeclaration(): CstNode? {
        if (peekToken() is CommaToken) {
            consumeToken(CommaToken::class.java)
            return varsDeclaration().also { if (it.children.isEmpty()) error("IdentifierToken was not found") }
        }
        return null
    }

    private fun funcDeclaration(): CstNode {
        val funcToken = consumeToken(FuncToken::class.java)

        val children = ArrayDeque<CstNode>().apply {
            add(identifier())
            add(paramsDeclarationForProcOrFun())
            add(funcBlockDeclaration())
        }

        return CstNode(funcToken, children)
    }

    private fun procDeclaration(): CstNode {
        val procToken = consumeToken(ProcToken::class.java)
        return CstNode(procToken).apply {
            children.add(identifier())
            children.add(paramsDeclarationForProcOrFun())
            children.add(blockDeclaration())
        }
    }

    private fun paramsDeclarationForProcOrFun(): CstNode {
        consumeToken(OpeningBracketToken::class.java)
        val params = paramsDeclaration()
        consumeToken(ClosingBracketToken::class.java)

        return params
    }

    private fun paramsDeclaration(): CstNode {
        if (peekToken() is IdentifierToken) {
            val variable = CstNode(nextToken())

            val nextParams = nextParamsDeclaration() ?: CstNode()
            nextParams.children.addFirst(variable)

            return nextParams
        }
        return CstNode()
    }

    private fun nextParamsDeclaration(): CstNode? {
        if (peekToken() is CommaToken) {
            consumeToken(CommaToken::class.java)
            return paramsDeclaration().also { if (it.children.isEmpty()) error("IdentifierToken was not found") }
        }
        return null
    }

    private fun blockDeclaration(): CstNode {
        consumeToken(OpeningCurlyBracketToken::class.java)

        var node = nodeInBlock()
        val nodes = ArrayDeque<CstNode>()

        while (node != null) {
            nodes.add(node)
            node = nodeInBlock()
        }

        consumeToken(ClosingCurlyBracketToken::class.java)
        return CstNode(children = nodes)
    }

    private fun funcBlockDeclaration(): CstNode {
        consumeToken(OpeningCurlyBracketToken::class.java)

        var node = nodeInBlock()
        val nodes = ArrayDeque<CstNode>()

        while (node != null) {
            nodes.add(node)
            node = nodeInBlock()
        }

        consumeToken(ClosingCurlyBracketToken::class.java)

        return CstNode().apply { children.addAll(nodes) }
    }

    private fun returnStmt(): CstNode {
        val returnToken = consumeToken(ReturnToken::class.java)
        return CstNode(returnToken).apply {
            children.add(expression())
            consumeToken(SemicolonToken::class.java)
        }
    }

    private fun expression(): CstNode {
        val exprStack = mutableListOf<CstNode>()
        val unOperatorStack = mutableListOf<CstNode>()
        val binOperatorStack = mutableListOf<CstNode>()

        var lastTokenIsOperator = true

        while (isExpressionToken(peekToken())) {
            if (tryHandleOperatorInExpression(lastTokenIsOperator, exprStack, unOperatorStack, binOperatorStack)) {
                lastTokenIsOperator = true
                continue
            }
            if (peekToken() is ClosingBracketToken) {
                foldExpression(exprStack, unOperatorStack, binOperatorStack)
                return exprStack.firstOrNull() ?: error("Expression was not parsed")
            }

            lastTokenIsOperator = false
            if (!tryHandleNoOperatorInExpression(exprStack)) {
                error("Token ${peekToken()} is not applicable for expression")
            }
        }

        foldExpression(exprStack, unOperatorStack, binOperatorStack)
        return exprStack.firstOrNull() ?: error("Expression was not parsed")
    }

    private fun variableOrFuncOrProcCall(): CstNode {
        val id = consumeIdToken()
        val transitions = sequenceOf(::funcOrProcCall)
        return transitions.mapNotNull { it.invoke(id) }.firstOrNull() ?: CstNode(id)
    }

    private fun expressionInBrackets(): CstNode {
        consumeToken(OpeningBracketToken::class.java)
        val expr = expression()
        consumeToken(ClosingBracketToken::class.java)
        return expr
    }

    private fun tryHandleNoOperatorInExpression(exprStack: MutableList<CstNode>): Boolean {
        if (peekToken() is OpeningBracketToken) {
            exprStack.add(expressionInBrackets())
            return true
        }
        if (peekToken() is IdentifierToken) {
            exprStack.add(variableOrFuncOrProcCall())
            return true
        }
        if (tokenIsAtom(peekToken())) {
            exprStack.add(CstNode(nextToken()))
            return true
        }
        return false
    }

    private fun tokenIsAtom(token: Token?): Boolean =
        token is NumberToken
                || token is TrueToken
                || token is FalseToken
                || token is StringToken

    private fun tryHandleOperatorInExpression(lastTokenIsOperator: Boolean,
                                              exprStack: MutableList<CstNode>,
                                              unOperatorStack: MutableList<CstNode>,
                                              binOperatorStack: MutableList<CstNode>) : Boolean {
        if (peekToken() is Operator) {
            val token = nextToken()
            if (!lastTokenIsOperator) {
                foldUnaryExpression(exprStack, unOperatorStack, token)
                foldBinaryExpression(exprStack, binOperatorStack, token)
                binOperatorStack.add(CstNode(token))
            } else unOperatorStack.add(CstNode(token))
            return true
        }
        return false
    }

    private fun foldUnaryExpression(
        exprStack: MutableList<CstNode>,
        unOperatorStack: MutableList<CstNode>,
        currentToken: Token
    ) {
        while (unOperatorStack.isNotEmpty()) {
            val lastOp = unOperatorStack.removeLast()
            val rightNode =
                exprStack.removeLastOrNull() ?: error("Expression parsing exception with $currentToken operator")

            exprStack.add(CstNode(lastOp.token).apply { children.add(rightNode) })
        }
    }

    private fun foldBinaryExpression(
        exprStack: MutableList<CstNode>,
        binOperatorStack: MutableList<CstNode>,
        currentToken: Token
    ) {
        while (binOperatorStack.isNotEmpty()
            && binOperatorStack.last().token !is OpeningBracketToken
            && priority(binOperatorStack.last().token!!) >= priority(currentToken)
        ) {
            val rightNode =
                exprStack.removeLastOrNull() ?: error("Expression parsing exception with $currentToken operator")
            val leftNode =
                exprStack.removeLastOrNull() ?: error("Expression parsing exception with $currentToken operator")
            val lastOp = binOperatorStack.removeLast()

            exprStack.add(CstNode(lastOp.token).apply {
                children.add(leftNode)
                children.add(rightNode)
            })
        }
    }

    private fun foldExpression(
        exprStack: MutableList<CstNode>,
        unOperatorStack: MutableList<CstNode>,
        binOperatorStack: MutableList<CstNode>
    ) {
        while (unOperatorStack.isNotEmpty()) {
            val lastOp = unOperatorStack.removeLast()
            val rightNode = exprStack.removeLastOrNull() ?: error("Expression parsing exception with $lastOp operator")

            exprStack.add(CstNode(lastOp.token).apply { children.add(rightNode) })
        }

        while (binOperatorStack.isNotEmpty()) {
            val lastOp = binOperatorStack.removeLast()
            val rightNode = exprStack.removeLastOrNull() ?: error("Expression parsing exception with $lastOp operator")
            val leftNode = exprStack.removeLastOrNull() ?: error("Expression parsing exception with $lastOp operator")

            exprStack.add(CstNode(lastOp.token).apply {
                children.add(leftNode)
                children.add(rightNode)
            })
        }
    }

    private fun isExpressionToken(token: Token?): Boolean =
        token is NumberToken
                || token is Operator
                || token is StringToken
                || token is OpeningBracketToken
                || token is ClosingBracketToken
                || token is TrueToken
                || token is FalseToken
                || token is IdentifierToken

    private fun priority(token: Token): Int =
        when (token) {
            is PlusToken -> 1
            is MinusToken -> 1
            is TimesToken -> 2
            is DivideToken -> 2
            else -> error("Token $token is not an operator")
        }

    private fun identifier() : CstNode {
        if (peekToken() is IdentifierToken) {
            return CstNode(nextToken())
        }
        error("Expected IdentifierToken, but found: ${peekToken()}.")
    }

    private fun consumeToken(expected: Type): Token {
        val nextToken = peekToken()
        if (nextToken != null && nextToken::class.java == expected) {
            return nextToken()
        }
        error("Expected token: $expected, but found: ${peekToken()}.")
    }

    private fun consumeIdToken(): Token {
        if (peekToken() is IdentifierToken) {
            return nextToken()
        }
        error("Expected token: IdentifierToken, but found: ${peekToken()}.")
    }

    private fun peekToken(): Token? = if (currentTokenIndex < tokens.size) tokens[currentTokenIndex] else null

    private fun nextToken(): Token = tokens[currentTokenIndex++]
}
