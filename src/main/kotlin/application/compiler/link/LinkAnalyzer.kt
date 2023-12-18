package application.compiler.link

import application.compiler.ast.AAssignStmt
import application.compiler.ast.ABinaryOp
import application.compiler.ast.ABlock
import application.compiler.ast.ACallExpr
import application.compiler.ast.ADeclaration
import application.compiler.ast.AFunDeclaration
import application.compiler.ast.AIdentifier
import application.compiler.ast.AIfStmt
import application.compiler.ast.AParamDeclaration
import application.compiler.ast.APrintStmt
import application.compiler.ast.AProcDeclaration
import application.compiler.ast.AProgram
import application.compiler.ast.AReturnStmt
import application.compiler.ast.AUnaryOp
import application.compiler.ast.AVarDeclaration
import application.compiler.ast.AVarDeclarations
import application.compiler.ast.AWhileStmt
import application.compiler.ast.AstNode

class LinkAnalyzer(private val ast: AstNode) {

    private val result = mutableMapOf<AstNode, ADeclaration>()
    private val context = mutableListOf<MutableList<ADeclaration>>()
        .also { it.add(mutableListOf()) }

    fun analyze(): Map<AstNode, ADeclaration> {
        if (ast is AProgram) {
            ast.commands.forEach { analyze(it) }
            return result
        }

        error("Expected Ast is AProgram, but found $ast")
    }

    private fun analyze(command: AstNode) {
        when (command) {
            is AVarDeclarations -> command.declarations.forEach { analyze(it) }
            is AVarDeclaration -> {
                result[command] = command
                context.last().add(command)
                analyze(command.value as AstNode)
            }
            is AAssignStmt -> {
                result[command.left] = findDeclaration(command.left)
                analyze(command.right as AstNode)
            }
            is APrintStmt -> analyze(command.exp as AstNode)
            is AIdentifier -> result[command] = findDeclaration(command)
            is AFunDeclaration -> {
                context.last().add(command)
                updateContext()
                command.params.forEach { analyze(it) }
                analyze(command.stmts)
                rollbackContext()
            }
            is AProcDeclaration -> {
                context.last().add(command)
                updateContext()
                command.params.forEach { analyze(it) }
                analyze(command.stmts)
                rollbackContext()
            }
            is ACallExpr -> {
                result[command] = findDeclaration(command)
                command.args.forEach { analyze(it as AstNode) }
            }
            is ABinaryOp -> {
                analyze(command.left as AstNode)
                analyze(command.right as AstNode)
            }
            is AUnaryOp -> analyze(command.subexp as AstNode)
            is ABlock -> command.body.forEach { analyze(it as AstNode) }
            is AReturnStmt -> analyze(command.exp as AstNode)
            is AParamDeclaration -> {
                result[command] = command
                context.last().add(command)
            }
            is AWhileStmt -> {
                analyze(command.guard as AstNode)
                command.innerBlock.body.forEach { analyze(it as AstNode) }
            }
            is AIfStmt -> {
                analyze(command.guard as AstNode)
                command.ifBranch.body.forEach { analyze(it as AstNode) }
                command.elseBranch?.body?.forEach { analyze(it as AstNode) }
            }
            else -> {}
        }
    }

    private fun updateContext() = context.add(mutableListOf())
    private fun rollbackContext() = context.removeLast()

    private fun findDeclaration(command: AIdentifier): ADeclaration {
        for (i in context.size - 1 downTo 0) {
            val declaration = context[i].firstOrNull {
                (it is AVarDeclaration && it.name == command.name)
                        || (it is AParamDeclaration && it.name == command.name)
            }

            if (declaration != null) {
                return declaration
            }
        }
        error("Declaration of $command is not found")
    }

    private fun findDeclaration(command: ACallExpr): ADeclaration {
        for (i in context.size - 1 downTo 0) {
            val declaration = context[i].firstOrNull {
                (it is AFunDeclaration && it.name == command.name && it.params.size == command.args.size)
                        || (it is AProcDeclaration && it.name == command.name && it.params.size == command.args.size)
            }

            if (declaration != null) {
                return declaration
            }
        }
        error("Declaration of $command is not found")
    }
}