package application.store.MainStore.compiler.ast.builder

import application.store.MainStore.compiler.ast.AExpr
import application.store.MainStore.compiler.ast.AReturnStmt
import application.store.MainStore.compiler.ast.AstNode
import application.store.MainStore.compiler.lexer.ReturnToken
import application.store.MainStore.compiler.syntax.CstNode

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