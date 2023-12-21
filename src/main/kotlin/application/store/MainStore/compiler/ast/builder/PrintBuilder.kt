package application.store.MainStore.compiler.ast.builder

import application.store.MainStore.compiler.ast.AExpr
import application.store.MainStore.compiler.ast.APrintStmt
import application.store.MainStore.compiler.ast.AstNode
import application.store.MainStore.compiler.lexer.PrintToken
import application.store.MainStore.compiler.syntax.CstNode

object PrintBuilder : AstNodeBuilder {

    private const val CHILDREN_NUMBER = 1

    override fun check(node: CstNode): Boolean =
        node.token is PrintToken && node.children.size == CHILDREN_NUMBER

    override fun build(node: CstNode): AstNode {
        val printToken = getPrint(node)
        return APrintStmt(getParam(node.children[0]) as AExpr, printToken.loc)
    }

    private fun getPrint(node: CstNode): PrintToken {
        if (node.token is PrintToken) {
            return node.token
        }
        error("Expected PrintToken, but found ${node.token}")
    }

    private fun getParam(node: CstNode) = ExpressionBuilder.build(node)
}