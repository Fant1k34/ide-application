package application.compiler.ast.builder

import application.compiler.ast.ABlock
import application.compiler.ast.ABlockCommand
import application.compiler.syntax.CstNode

object BlockBuilder : AstNodeBuilder {

    private val commandBuilders = buildList {
        add(VarDeclarationBuilder)
        add(IfBuilder)
        add(WhileBuilder)
        add(PrintBuilder)
        add(AssignmentBuilder)
        add(CallBuilder)
        add(ReturnBuilder)
    }

    override fun check(node: CstNode): Boolean =
        node.token == null

    override fun build(node: CstNode): ABlock {
        val body = node.children
            .filterNot { it.token == null && it.children.isEmpty() }
            .map { getCommand(it) }
        return ABlock(body, null)
    }

    private fun getCommand(node: CstNode): ABlockCommand {
        for (builder in commandBuilders) {
            if (builder.check(node)) {
                val result = builder.build(node)
                return result as? ABlockCommand ?: error("Result $result is not ABlockCommand")
            }
        }
        error("BlockBuilder didn't found suitable ast node builder for node $node")
    }
}