package application.store.MainStore.compiler.ast.builder

import application.store.MainStore.compiler.ast.ABinaryOp
import application.store.MainStore.compiler.ast.ABoolean
import application.store.MainStore.compiler.ast.ACallExpr
import application.store.MainStore.compiler.ast.AExpr
import application.store.MainStore.compiler.ast.AIdentifier
import application.store.MainStore.compiler.ast.ANumber
import application.store.MainStore.compiler.ast.AString
import application.store.MainStore.compiler.ast.AUnaryOp
import application.store.MainStore.compiler.ast.And
import application.store.MainStore.compiler.ast.AstNode
import application.store.MainStore.compiler.ast.BinaryOperator
import application.store.MainStore.compiler.ast.Divide
import application.store.MainStore.compiler.ast.Equal
import application.store.MainStore.compiler.ast.GreaterOrEqualThan
import application.store.MainStore.compiler.ast.GreaterThan
import application.store.MainStore.compiler.ast.LessOrEqualThan
import application.store.MainStore.compiler.ast.LessThan
import application.store.MainStore.compiler.ast.Minus
import application.store.MainStore.compiler.ast.Not
import application.store.MainStore.compiler.ast.NotEqual
import application.store.MainStore.compiler.ast.Or
import application.store.MainStore.compiler.ast.Plus
import application.store.MainStore.compiler.ast.Times
import application.store.MainStore.compiler.ast.UnaryOperator
import application.store.MainStore.compiler.lexer.AndToken
import application.store.MainStore.compiler.lexer.DivideToken
import application.store.MainStore.compiler.lexer.EqualToken
import application.store.MainStore.compiler.lexer.FalseToken
import application.store.MainStore.compiler.lexer.GreaterOrEqualThanToken
import application.store.MainStore.compiler.lexer.GreaterThanToken
import application.store.MainStore.compiler.lexer.IdentifierToken
import application.store.MainStore.compiler.lexer.LessOrEqualThanToken
import application.store.MainStore.compiler.lexer.LessThanToken
import application.store.MainStore.compiler.lexer.MinusToken
import application.store.MainStore.compiler.lexer.NotEqualToken
import application.store.MainStore.compiler.lexer.NotToken
import application.store.MainStore.compiler.lexer.NumberToken
import application.store.MainStore.compiler.lexer.OrToken
import application.store.MainStore.compiler.lexer.PlusToken
import application.store.MainStore.compiler.lexer.StringToken
import application.store.MainStore.compiler.lexer.TimesToken
import application.store.MainStore.compiler.lexer.Token
import application.store.MainStore.compiler.lexer.TrueToken
import application.store.MainStore.compiler.syntax.CstNode

object ExpressionBuilder : AstNodeBuilder {
    override fun check(node: CstNode): Boolean = error("Not supported")

    override fun build(node: CstNode): AstNode {
        if (node.token is NumberToken) {
            return ANumber(node.token.value.toInt(), node.token.loc)
        }
        if (node.token is StringToken) {
            return AString(node.token.value, node.token.loc)
        }
        if (node.token is FalseToken || node.token is TrueToken) {
            return ABoolean(node.token is TrueToken, node.token.loc)
        }
        if (node.token is IdentifierToken) {
            return getIdOrCall(node)
        }

        return getOperation(node)
    }

    private fun getIdOrCall(node: CstNode): AstNode {
        if (node.children.size == 0) {
            val id = getIdentifier(node)
            return AIdentifier(id.value, id.loc)
        }
        else if (node.children.size == 1) {
            return getCall(getIdentifier(node), node.children[0]) as AstNode
        }
        error("Expected id or call expression, but found $node")
    }

    private fun getCall(id: IdentifierToken, params: CstNode): AExpr {
        val callParams = params.children.map { build(it) as AExpr }.toList()
        return ACallExpr(id.value, callParams, id.loc)
    }

    private fun getIdentifier(node: CstNode): IdentifierToken {
        if (node.token is IdentifierToken) {
            return node.token
        }
        error("Expected IdentifierToken, but found ${node.token}")
    }

    private fun getOperation(node: CstNode): AstNode {
        if (node.children.size == 1) return getUnaryOperation(node)
        if (node.children.size == 2) return getBinaryOperation(node)

        error("Expected operator in $node")
    }

    private fun getUnaryOperation(node: CstNode): AUnaryOp {
        val subexpr = build(node.children[0]) as AExpr
        val operator = getUnaryOperator(node.token ?: error("Expected operator in expression"))

        return AUnaryOp(operator, subexpr, node.token.loc)
    }

    private fun getBinaryOperation(node: CstNode): ABinaryOp {
        val leftExpr = build(node.children[0]) as AExpr
        val rightExpr = build(node.children[1]) as AExpr
        val operator = getBinaryOperator(node.token ?: error("Expected operator in expression"))

        return ABinaryOp(operator, leftExpr, rightExpr, node.token.loc)
    }

    private fun getUnaryOperator(token: Token): UnaryOperator {
        if (token is MinusToken) return Minus
        if (token is NotToken) return Not

        error("Expected unary operator, but was found $token")
    }

    private fun getBinaryOperator(token: Token): BinaryOperator {
        if (token is PlusToken) return Plus
        if (token is MinusToken) return Minus
        if (token is TimesToken) return Times
        if (token is DivideToken) return Divide
        if (token is AndToken) return And
        if (token is OrToken) return Or
        if (token is NotEqualToken) return NotEqual
        if (token is EqualToken) return Equal
        if (token is GreaterOrEqualThanToken) return GreaterOrEqualThan
        if (token is GreaterThanToken) return GreaterThan
        if (token is LessOrEqualThanToken) return LessOrEqualThan
        if (token is LessThanToken) return LessThan

        error("Expected binary operator, but was found $token")
    }
}