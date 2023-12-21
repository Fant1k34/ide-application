package application.store.MainStore.compiler.cfg

import application.store.MainStore.compiler.ast.AExpr
import application.store.MainStore.compiler.ast.AstNode

abstract class CFGNode(var child: CFGNode? = null)
class AssignmentCFGNode(val variable: AstNode, val value: AExpr, child: CFGNode? = null) : CFGNode(child)
class PrintCFGNode(val value: AExpr, child: CFGNode? = null) : CFGNode(child)
class StartCFGNode(child: CFGNode? = null) : CFGNode(child)
