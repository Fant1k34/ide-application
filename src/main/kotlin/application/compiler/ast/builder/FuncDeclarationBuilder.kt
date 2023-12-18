package application.compiler.ast.builder

import application.compiler.ast.ABlock
import application.compiler.ast.AFunDeclaration
import application.compiler.ast.AParamDeclaration
import application.compiler.ast.AstNode
import application.compiler.lexer.FuncToken
import application.compiler.lexer.IdentifierToken
import application.compiler.syntax.CstNode

object FuncDeclarationBuilder : AstNodeBuilder {

    private const val CHILDREN_NUMBER = 3

    override fun check(node: CstNode): Boolean =
        node.token is FuncToken && node.children.size == CHILDREN_NUMBER

    override fun build(node: CstNode): AstNode {
        val funcName = getFuncName(node.children[0])
        val params = getParams(node.children[1])
        val block = getBlock(node.children[2])
        val funcToken = getFunc(node)

        return AFunDeclaration(funcName, params, block, funcToken.loc)
    }

    private fun getFuncName(node: CstNode): String = getIdentifier(node).value

    private fun getFunc(node: CstNode): FuncToken {
        if (node.token is FuncToken) {
            return node.token
        }
        error("Expected FuncToken, but found ${node.token}")
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

    private fun getReturnNode(node: CstNode): CstNode {
        val commandsNumber = node.children.size - 1
        return node.children[commandsNumber]
    }

    private fun getBlockNode(node: CstNode): CstNode {
        val commandsNumber = node.children.size

        val commands = ArrayDeque<CstNode>()

        commands.addAll(node.children.subList(0, commandsNumber))

        return CstNode(children = commands)
    }
}