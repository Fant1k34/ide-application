package application.compiler.function

import application.compiler.ast.ABlock
import application.compiler.ast.ABlockCommand
import application.compiler.ast.ADeclaration
import application.compiler.ast.AFunDeclaration
import application.compiler.ast.AIfStmt
import application.compiler.ast.AReturnStmt
import application.compiler.ast.AWhileStmt
import application.compiler.ast.AstNode

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