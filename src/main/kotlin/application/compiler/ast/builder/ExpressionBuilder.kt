package application.compiler.ast.builder

import application.compiler.ast.ABinaryOp
import application.compiler.ast.ABoolean
import application.compiler.ast.ACallExpr
import application.compiler.ast.AExpr
import application.compiler.ast.AIdentifier
import application.compiler.ast.ANumber
import application.compiler.ast.AString
import application.compiler.ast.AUnaryOp
import application.compiler.ast.And
import application.compiler.ast.AstNode
import application.compiler.ast.BinaryOperator
import application.compiler.ast.Divide
import application.compiler.ast.Equal
import application.compiler.ast.GreaterOrEqualThan
import application.compiler.ast.GreaterThan
import application.compiler.ast.LessOrEqualThan
import application.compiler.ast.LessThan
import application.compiler.ast.Minus
import application.compiler.ast.Not
import application.compiler.ast.NotEqual
import application.compiler.ast.Or
import application.compiler.ast.Plus
import application.compiler.ast.Times
import application.compiler.ast.UnaryOperator
import application.compiler.lexer.AndToken
import application.compiler.lexer.DivideToken
import application.compiler.lexer.EqualToken
import application.compiler.lexer.FalseToken
import application.compiler.lexer.GreaterOrEqualThanToken
import application.compiler.lexer.GreaterThanToken
import application.compiler.lexer.IdentifierToken
import application.compiler.lexer.LessOrEqualThanToken
import application.compiler.lexer.LessThanToken
import application.compiler.lexer.MinusToken
import application.compiler.lexer.NotEqualToken
import application.compiler.lexer.NotToken
import application.compiler.lexer.NumberToken
import application.compiler.lexer.OrToken
import application.compiler.lexer.PlusToken
import application.compiler.lexer.StringToken
import application.compiler.lexer.TimesToken
import application.compiler.lexer.Token
import application.compiler.lexer.TrueToken
import application.compiler.syntax.CstNode

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