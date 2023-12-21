package application.store.MainStore.compiler.ast.builder

import application.store.MainStore.compiler.ast.ABlock
import application.store.MainStore.compiler.ast.AExpr
import application.store.MainStore.compiler.ast.AWhileStmt
import application.store.MainStore.compiler.ast.AstNode
import application.store.MainStore.compiler.lexer.WhileToken
import application.store.MainStore.compiler.syntax.CstNode

object WhileBuilder : AstNodeBuilder {

    private const val CHILDREN_NUMBER = 2

    override fun check(node: CstNode): Boolean =
        node.token is WhileToken && node.children.size == CHILDREN_NUMBER

    override fun build(node: CstNode): AstNode {
        val condition = getCondition(node.children[0]) as AExpr
        val block = getBlock(node.children[1])
        val whileToken = getWhileToken(node)

        return AWhileStmt(condition, block, whileToken.loc)
    }

    private fun getWhileToken(node: CstNode): WhileToken {
        if (node.token is WhileToken) {
            return node.token
        }
        error("Expected WhileToken, but found ${node.token}")
    }

    private fun getCondition(node: CstNode): AstNode = ExpressionBuilder.build(node)

    private fun getBlock(node: CstNode): ABlock = BlockBuilder.build(node)
}