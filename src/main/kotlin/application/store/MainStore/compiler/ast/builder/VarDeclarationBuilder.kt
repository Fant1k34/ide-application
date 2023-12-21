package application.store.MainStore.compiler.ast.builder

import application.store.MainStore.compiler.ast.AExpr
import application.store.MainStore.compiler.ast.AVarDeclaration
import application.store.MainStore.compiler.ast.AVarDeclarations
import application.store.MainStore.compiler.ast.AstNode
import application.store.MainStore.compiler.lexer.IdentifierToken
import application.store.MainStore.compiler.lexer.VarToken
import application.store.MainStore.compiler.syntax.CstNode

object VarDeclarationBuilder : AstNodeBuilder {

    private const val CHILDREN_NUMBER = 1

    override fun check(node: CstNode): Boolean =
        node.token is VarToken && node.children.size == CHILDREN_NUMBER

    override fun build(node: CstNode): AstNode {
        val vars = node.children[0].children.map { buildVarAssignment(it) }
        val varToken = getVar(node)
        return AVarDeclarations(vars, varToken.loc)
    }

    private fun buildVarAssignment(node: CstNode): AVarDeclaration {
        val id = getIdentifier(node.children[0])
        return AVarDeclaration(id.value, ExpressionBuilder.build(node.children[1]) as AExpr, id.loc)
    }

    private fun getVar(node: CstNode): VarToken {
        if (node.token is VarToken) {
            return node.token
        }
        error("Expected VarToken, but found ${node.token}")
    }

    private fun getIdentifier(node: CstNode): IdentifierToken {
        if (node.token is IdentifierToken) {
            return node.token
        }
        error("Expected IdentifierToken, but found ${node.token}")
    }
}