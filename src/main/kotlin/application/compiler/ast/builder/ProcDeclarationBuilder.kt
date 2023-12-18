package application.compiler.ast.builder

import application.compiler.ast.ABlock
import application.compiler.ast.AParamDeclaration
import application.compiler.ast.AProcDeclaration
import application.compiler.ast.AstNode
import application.compiler.lexer.IdentifierToken
import application.compiler.lexer.ProcToken
import application.compiler.syntax.CstNode

object ProcDeclarationBuilder : AstNodeBuilder {

    private const val CHILDREN_NUMBER = 3

    override fun check(node: CstNode): Boolean =
        node.token is ProcToken && node.children.size == CHILDREN_NUMBER

    override fun build(node: CstNode): AstNode {
        val procName = getProcName(node.children[0])
        val params = getParams(node.children[1])
        val block = getBlock(node.children[2])
        val proc = getProc(node)

        return AProcDeclaration(procName, params, block, proc.loc)
    }

    private fun getProcName(node: CstNode): String = getIdentifier(node).value

    private fun getProc(node: CstNode): ProcToken {
        if (node.token is ProcToken) {
            return node.token
        }
        error("Expected ProcToken, but found ${node.token}")
    }

    private fun getIdentifier(node: CstNode): IdentifierToken {
        if (node.token is IdentifierToken) {
            return node.token
        }
        error("Expected IdentifierToken, but found ${node.token}")
    }

    private fun getParams(node: CstNode): List<AParamDeclaration> =
        node.children.map {
            val id = getIdentifier(it)
            AParamDeclaration(id.value, id.loc)
        }.toList()

    private fun getBlock(node: CstNode): ABlock = BlockBuilder.build(node)
}