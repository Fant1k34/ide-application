package application.store.MainStore.compiler.ast.builder

import application.store.MainStore.compiler.ast.AstNode
import application.store.MainStore.compiler.syntax.CstNode

interface AstNodeBuilder {

    fun check(node: CstNode): Boolean

    fun build(node: CstNode): AstNode
}