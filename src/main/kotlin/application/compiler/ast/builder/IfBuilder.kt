package application.compiler.ast.builder

import application.compiler.ast.ABlock
import application.compiler.ast.AExpr
import application.compiler.ast.AIfStmt
import application.compiler.ast.AstNode
import application.compiler.lexer.IfToken
import application.compiler.syntax.CstNode

object IfBuilder : AstNodeBuilder {

    private const val CHILDREN_NUMBER = 2
    private const val CHILDREN_NUMBER_WITH_ELSE = CHILDREN_NUMBER + 1

    override fun check(node: CstNode): Boolean =
        node.token is IfToken &&
                (node.children.size == CHILDREN_NUMBER || node.children.size == CHILDREN_NUMBER_WITH_ELSE)

    override fun build(node: CstNode): AstNode {
        val condition = getCondition(node.children[0]) as AExpr
        val ifBranch = getBlock(node.children[1])
        val elseBranch = if (node.children.size == CHILDREN_NUMBER_WITH_ELSE) getBlock(node.children[2].children[0]) else null
        val ifToken = getIf(node)

        return AIfStmt(condition, ifBranch, elseBranch, ifToken.loc)
    }

    private fun getIf(node: CstNode): IfToken {
        if (node.token is IfToken) {
            return node.token
        }
        error("Expected IfToken, but found ${node.token}")
    }

    private fun getCondition(node: CstNode): AstNode = ExpressionBuilder.build(node)

    private fun getBlock(node: CstNode): ABlock = BlockBuilder.build(node)
}