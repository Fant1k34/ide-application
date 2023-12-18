package application.compiler.ast.builder

import application.compiler.ast.AstNode
import application.compiler.syntax.CstNode

interface AstNodeBuilder {

    fun check(node: CstNode): Boolean

    fun build(node: CstNode): AstNode
}