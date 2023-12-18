package application.compiler.ast.builder

import application.compiler.ast.AAssignStmt
import application.compiler.ast.AExpr
import application.compiler.ast.AIdentifier
import application.compiler.ast.AstNode
import application.compiler.lexer.AssignToken
import application.compiler.lexer.IdentifierToken
import application.compiler.syntax.CstNode

object AssignmentBuilder : AstNodeBuilder {

    private const val CHILDREN_NUMBER = 2

    override fun check(node: CstNode): Boolean =
        node.token is AssignToken && node.children.size == CHILDREN_NUMBER

    override fun build(node: CstNode): AstNode {
        val id = getId(node.children[0])
        val expr = ExpressionBuilder.build(node.children[1]) as AExpr
        val assign = getAssign(node)

        return AAssignStmt(id, expr, assign.loc)
    }

    private fun getId(node: CstNode): AIdentifier {
        val idToken = getIdentifier(node)
        return AIdentifier(idToken.value, idToken.loc)
    }

    private fun getAssign(node: CstNode): AssignToken {
        if (node.token is AssignToken) {
            return node.token
        }
        error("Expected AssignToken, but found ${node.token}")
    }

    private fun getIdentifier(node: CstNode): IdentifierToken {
        if (node.token is IdentifierToken) {
            return node.token
        }
        error("Expected IdentifierToken, but found ${node.token}")
    }
}