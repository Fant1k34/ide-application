package application.store.MainStore.compiler.cfg

import application.store.MainStore.compiler.ast.APrintStmt
import application.store.MainStore.compiler.ast.AProgram
import application.store.MainStore.compiler.ast.AstNode

class CFGBuilder(private val tree: AstNode) {

    private val result: CFGNode = StartCFGNode()
    private var currentNode: CFGNode = result

    fun build(): CFGNode {
        if (tree is AProgram) {
            tree.commands.forEach {
                when (it) {
                    is APrintStmt -> printNode(it)
//                    is AVarDeclarations -> varDeclarations(it)
//                    is AAssignStmt -> assignment(it)
//                    is AIfStmt -> ifStmt(it)
//                    is AWhileStmt -> whileStmt(it)
//                    is ACallExpr -> callStmt(it)
                }
            }
            return result
        }
        error("Cannot analyze program with $tree")
    }

    private fun printNode(node: AstNode) {
        if (node is APrintStmt) {
            val newNode = PrintCFGNode(node.exp)
            currentNode.child = newNode
            currentNode = newNode
        }
    }
}