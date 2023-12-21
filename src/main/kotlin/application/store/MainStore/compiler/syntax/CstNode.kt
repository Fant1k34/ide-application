package application.store.MainStore.compiler.syntax

import application.store.MainStore.compiler.lexer.Token

data class CstNode(val token: Token? = null,
                   val children: ArrayDeque<CstNode> = ArrayDeque())