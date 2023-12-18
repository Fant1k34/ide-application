package application.compiler.analysis.type

import application.compiler.ast.AstNode

sealed interface IdlType
sealed interface AtomicType : IdlType
object IntegerType : AtomicType {
    override fun toString(): String = "Integer"
}
object StringType : AtomicType {
    override fun toString(): String = "String"
}
object BooleanType : AtomicType {
    override fun toString(): String = "Boolean"
}
data class TypeWrapper(val node: AstNode) : IdlType