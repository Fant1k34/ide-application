package application.compiler.ast

import application.compiler.Location

sealed interface Operator
sealed interface BinaryOperator : Operator
sealed interface UnaryOperator : Operator

object Plus : BinaryOperator
object Minus : BinaryOperator, UnaryOperator
object Times : BinaryOperator
object Divide : BinaryOperator
object And : BinaryOperator
object Or : BinaryOperator
object NotEqual : BinaryOperator
object Equal : BinaryOperator
object GreaterThan : BinaryOperator
object GreaterOrEqualThan : BinaryOperator
object LessThan : BinaryOperator
object LessOrEqualThan : BinaryOperator
object Not : UnaryOperator


abstract class AstNode(val loc: Location?)

sealed interface AExprOrIdentifierDeclaration
sealed interface AExpr : AExprOrIdentifierDeclaration
sealed interface AAtomicExpr : AExpr
sealed interface ADeclaration
sealed interface AStmt
sealed interface AStmtInNestedBlock : AStmt
sealed interface ABlockCommand


data class ACallExpr(val name: String, val args: List<AExpr>, val location: Location) : AstNode(location),
    ABlockCommand, AExpr
open class AParamDeclaration(val name: String, val location: Location) : AstNode(location), ADeclaration,
    AExprOrIdentifierDeclaration {
    override fun toString(): String {
        return "${this::class.java.simpleName}('$name')"
    }
}
data class AVarDeclaration(val name: String, val value: AExpr, val location: Location) : AstNode(location),
    ADeclaration {
    override fun toString(): String {
        return "${this::class.java.simpleName}('$name' = '$value')"
    }
}

data class AVarDeclarations(val declarations: List<AVarDeclaration>, val location: Location?) : AstNode(location),
    ABlockCommand

data class AIdentifier(val name: String, val location: Location) : AstNode(location), AAtomicExpr
data class ABinaryOp(val operator: BinaryOperator, val left: AExpr, val right: AExpr, val location: Location) : AstNode(location),
    AExpr
data class AUnaryOp(val operator: UnaryOperator, val subexp: AExpr, val location: Location) : AstNode(location), AExpr
data class ANumber(val value: Int, val location: Location) : AstNode(location), AAtomicExpr, ABlockCommand
data class AString(val value: String, val location: Location) : AstNode(location), AAtomicExpr, ABlockCommand
data class ABoolean(val value: Boolean, val location: Location) : AstNode(location), AAtomicExpr, ABlockCommand
data class AAssignStmt(val left: AIdentifier, val right: AExpr, val location: Location) : AstNode(location),
    AStmtInNestedBlock, ABlockCommand
data class ABlock(val body: List<ABlockCommand>, val location: Location?) : AstNode(location), AStmt, ABlockCommand
data class AIfStmt(val guard: AExpr, val ifBranch: ABlock, val elseBranch: ABlock? = null, val location: Location) : AstNode(location),
    ABlockCommand, AStmtInNestedBlock
data class APrintStmt(val exp: AExpr, val location: Location) : AstNode(location), AStmtInNestedBlock, AExpr,
    ABlockCommand
data class AReturnStmt(val exp: AExpr, val location: Location) : AstNode(location), ABlockCommand
data class AWhileStmt(val guard: AExpr, val innerBlock: ABlock, val location: Location) : AstNode(location),
    AStmtInNestedBlock, ABlockCommand
data class AFunDeclaration(val name: String, val params: List<AParamDeclaration>, val stmts: ABlock, val location: Location) :
    AstNode(location), ADeclaration
data class AProcDeclaration(val name: String, val params: List<AParamDeclaration>, val stmts: ABlock, val location: Location) :
    AstNode(location), ADeclaration
data class AProgram(val commands : List<AstNode>, val location: Location?) : AstNode(location)
