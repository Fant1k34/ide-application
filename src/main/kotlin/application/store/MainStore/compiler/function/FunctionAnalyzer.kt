package application.store.MainStore.compiler.function

import application.store.MainStore.compiler.ast.ABlock
import application.store.MainStore.compiler.ast.ABlockCommand
import application.store.MainStore.compiler.ast.ADeclaration
import application.store.MainStore.compiler.ast.AFunDeclaration
import application.store.MainStore.compiler.ast.AIfStmt
import application.store.MainStore.compiler.ast.AReturnStmt
import application.store.MainStore.compiler.ast.AWhileStmt
import application.store.MainStore.compiler.ast.AstNode

class FunctionAnalyzer(private val links: Map<AstNode, ADeclaration>) {

    fun analyze(): Map<AReturnStmt, AFunDeclaration> {
        val resultMap = mutableMapOf<AReturnStmt, AFunDeclaration>()

        links.values
            .filterIsInstance<AFunDeclaration>()
            .forEach { func -> analyzeBlock(func.stmts).forEach { resultMap[it] = func } }

        return resultMap
    }

    private fun analyzeBlock(block: ABlock): List<AReturnStmt> = block.body.flatMap { analyzeBlockCommand(it) }

    private fun analyzeBlockCommand(command: ABlockCommand): List<AReturnStmt> {
        if (command is ABlock) return analyzeBlock(command)
        if (command is AReturnStmt) return listOf(command)
        if (command is AWhileStmt) return analyzeBlock(command.innerBlock)
        if (command is AIfStmt) {
            val ifResults = analyzeBlock(command.ifBranch)
            val elseResults = command.elseBranch?.let { analyzeBlock(it) } ?: emptyList()
            return ifResults + elseResults
        }
        return listOf()
    }

}