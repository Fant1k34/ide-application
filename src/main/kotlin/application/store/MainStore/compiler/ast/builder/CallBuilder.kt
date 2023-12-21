package application.store.MainStore.compiler.ast.builder

import application.store.MainStore.compiler.ast.ACallExpr
import application.store.MainStore.compiler.ast.AExpr
import application.store.MainStore.compiler.ast.AstNode
import application.store.MainStore.compiler.lexer.IdentifierToken
import application.store.MainStore.compiler.syntax.CstNode

object CallBuilder : AstNodeBuilder {

    private const val CHILDREN_NUMBER = 1

    override fun check(node: CstNode): Boolean =
        node.token is IdentifierToken && node.children.size == CHILDREN_NUMBER

    override fun build(node: CstNode): AstNode {
        val id = getIdentifier(node)
        val callParams = getParams(node.children[0])

        return ACallExpr(id.value, callParams, id.loc)
    }

    private fun getIdentifier(node: CstNode): IdentifierToken {
        if (node.token is IdentifierToken) {
            return node.token
        }
        error("Expected IdentifierToken, but found ${node.token}")
    }

    private fun getParams(params: CstNode): List<AExpr> =
        params.children.map { ExpressionBuilder.build(it) as AExpr }.toList()
}