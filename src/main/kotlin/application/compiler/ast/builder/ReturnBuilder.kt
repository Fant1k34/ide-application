package application.compiler.ast.builder

import application.compiler.ast.AExpr
import application.compiler.ast.AReturnStmt
import application.compiler.ast.AstNode
import application.compiler.lexer.ReturnToken
import application.compiler.syntax.CstNode

object ReturnBuilder : AstNodeBuilder {

    private const val CHILDREN_NUMBER = 1

    override fun check(node: CstNode): Boolean =
        node.token is ReturnToken && node.children.size == CHILDREN_NUMBER

    override fun build(node: CstNode): AstNode {
        val returnExpr = node.children[0]
        val returnToken = getReturn(node)
        return AReturnStmt(ExpressionBuilder.build(returnExpr) as AExpr, returnToken.loc)
    }

    private fun getReturn(node: CstNode): ReturnToken {
        if (node.token is ReturnToken) {
            return node.token
        }
        error("Expected ReturnToken, but found ${node.token}")
    }
}