package application.compiler

import application.compiler.analysis.type.BooleanType
import application.compiler.analysis.type.IdlType
import application.compiler.analysis.type.IntegerType
import application.compiler.analysis.type.StringType
import application.compiler.analysis.type.TypeAnalyzer
import application.compiler.ast.AAssignStmt
import application.compiler.ast.ABinaryOp
import application.compiler.ast.ABoolean
import application.compiler.ast.ACallExpr
import application.compiler.ast.ADeclaration
import application.compiler.ast.AExpr
import application.compiler.ast.AFunDeclaration
import application.compiler.ast.AIdentifier
import application.compiler.ast.AIfStmt
import application.compiler.ast.ANumber
import application.compiler.ast.AParamDeclaration
import application.compiler.ast.APrintStmt
import application.compiler.ast.AProcDeclaration
import application.compiler.ast.AProgram
import application.compiler.ast.AReturnStmt
import application.compiler.ast.AString
import application.compiler.ast.AUnaryOp
import application.compiler.ast.AVarDeclarations
import application.compiler.ast.AWhileStmt
import application.compiler.ast.And
import application.compiler.ast.AstBuilder
import application.compiler.ast.AstNode
import application.compiler.ast.Divide
import application.compiler.ast.Equal
import application.compiler.ast.GreaterOrEqualThan
import application.compiler.ast.GreaterThan
import application.compiler.ast.LessOrEqualThan
import application.compiler.ast.LessThan
import application.compiler.ast.Minus
import application.compiler.ast.Not
import application.compiler.ast.NotEqual
import application.compiler.ast.Or
import application.compiler.ast.Plus
import application.compiler.ast.Times
import application.compiler.function.FunctionAnalyzer
import application.compiler.lexer.LexicalAnalyzer
import application.compiler.link.LinkAnalyzer
import application.compiler.syntax.SyntaxAnalyzer

class Compiler(text: String) {

    private val ast: AstNode
    private val links: Map<AstNode, ADeclaration>
    private val functionReturns: Map<AReturnStmt, AFunDeclaration>
    private val types: Map<AstNode, IdlType>

    private val integers = mutableMapOf<ADeclaration, Int>()
    private val strings = mutableMapOf<ADeclaration, String>()
    private val booleans = mutableMapOf<ADeclaration, Boolean>()

    private var needToStopBlock = false
    private var outputResult: StringBuilder? = null
    private var outputCallback: (String) -> Unit = {
        throw IllegalStateException("Callback was not assigned")
    }

    init {
        val tokens = LexicalAnalyzer(text).tokenize()
        val cstNodes = SyntaxAnalyzer(tokens).parse()

        ast = AstBuilder(cstNodes).build()
        links = LinkAnalyzer(ast).analyze()
        functionReturns = FunctionAnalyzer(links).analyze()
        types = TypeAnalyzer(ast, links, functionReturns).analyze()
    }

    fun run(outputCallback: (String) -> Unit) {
        integers.clear()
        strings.clear()
        booleans.clear()
        outputResult = null
        this.outputCallback = outputCallback

        if (ast is AProgram) {
            ast.commands.forEach {
                when (it) {
                    is APrintStmt -> runPrint(it)
                    is AVarDeclarations -> varDeclarations(it)
                    is AAssignStmt -> assignment(it)
                    is AIfStmt -> ifStmt(it)
                    is AWhileStmt -> whileStmt(it)
                    is ACallExpr -> callStmt(it)
                }
            }
            return
        }
        error("Cannot run program with $ast")
    }

    private fun callStmt(astNode: ACallExpr) {
        val declaration = links[astNode] ?: error("Declaration for $astNode was not found")
        if (declaration is AProcDeclaration) procStmt(astNode)
        else if (declaration is AFunDeclaration) funcStmt(astNode)
    }

    private fun procStmt(astNode: ACallExpr) {
        val declaration = links[astNode] ?:  error("Declaration for $astNode was not found")
        if (declaration is AProcDeclaration) {
            assignParams(astNode, declaration.params)
            declaration.stmts.body.forEach { blockCommand(it as AstNode) }
        }
    }

    private fun funcStmt(astNode: ACallExpr): String {
        val declaration = links[astNode] ?:  error("Declaration for $astNode was not found")
        if (declaration is AFunDeclaration) {
            assignParams(astNode, declaration.params)
            declaration.stmts.body.forEach { blockCommand(it as AstNode) }

            val result = extractFunctionResult(declaration) ?: error("Result for $astNode was not calculated")
            needToStopBlock = false

            return result
        }
        error("Declaration $declaration is not AFunDeclaration")
    }

    private fun extractFunctionResult(declaration: AFunDeclaration): String? =
        when (types[declaration]) {
            is IntegerType -> extractIntFunctionResult(declaration)?.toString()
            is BooleanType -> extractBoolFunctionResult(declaration)?.toString()
            is StringType -> extractStringFunctionResult(declaration)
            else -> null
        }

    private fun extractIntFunctionResult(declaration: AFunDeclaration): Int? {
        val result = integers[declaration]
        integers.remove(declaration)
        return result
    }

    private fun extractBoolFunctionResult(declaration: AFunDeclaration): Boolean? {
        val result = booleans[declaration]
        booleans.remove(declaration)
        return result
    }

    private fun extractStringFunctionResult(declaration: AFunDeclaration): String? {
        val result = strings[declaration]
        strings.remove(declaration)
        return result
    }

    private fun funcBoolStmt(astNode: ACallExpr): Boolean {
        val declaration = links[astNode] ?:  error("Declaration for $astNode was not found")
        if (declaration is AFunDeclaration) {
            assignParams(astNode, declaration.params)
            declaration.stmts.body.forEach { blockCommand(it as AstNode) }

            val result = extractBoolFunctionResult(declaration) ?: error("Result for $astNode was not calculated")
            needToStopBlock = false

            return result
        }
        error("Declaration $declaration is not AFunDeclaration")
    }

    private fun assignParams(astNode: ACallExpr, params: List<AParamDeclaration>) {
        for (i in astNode.args.indices) {
            if (types[astNode.args[i] as AstNode] is BooleanType) booleans[params[i]] = getBoolResult(astNode.args[i])
            else if (types[astNode.args[i] as AstNode] is StringType) strings[params[i]] = getStringResult(astNode.args[i])
            else if (types[astNode.args[i] as AstNode] is IntegerType) integers[params[i]] = getIntResult(astNode.args[i])
        }
    }

    private fun funcIntStmt(astNode: ACallExpr): Int {
        val declaration = links[astNode] ?:  error("Declaration for $astNode was not found")
        if (declaration is AFunDeclaration) {
            assignParams(astNode, declaration.params)
            declaration.stmts.body.forEach { blockCommand(it as AstNode) }

            val result = extractIntFunctionResult(declaration) ?: error("Result for $astNode was not calculated")
            needToStopBlock = false

            return result
        }
        error("Declaration $declaration is not AFunDeclaration")
    }

    private fun funcStringStmt(astNode: ACallExpr): String {
        val declaration = links[astNode] ?:  error("Declaration for $astNode was not found")
        if (declaration is AFunDeclaration) {
            assignParams(astNode, declaration.params)
            declaration.stmts.body.forEach { blockCommand(it as AstNode) }

            val result = extractStringFunctionResult(declaration) ?: error("Result for $astNode was not calculated")
            needToStopBlock = false

            return result
        }
        error("Declaration $declaration is not AFunDeclaration")
    }

    private fun whileStmt(astNode: AWhileStmt) {
        if (needToStopBlock) return

        while (getBoolResult(astNode.guard)) {
            astNode.innerBlock.body.forEach { blockCommand(it as AstNode) }
        }
    }

    private fun ifStmt(astNode: AIfStmt) {
        if (needToStopBlock) return

        if (getBoolResult(astNode.guard)) astNode.ifBranch.body.forEach { blockCommand(it as AstNode) }
        else astNode.elseBranch?.body?.forEach { blockCommand(it as AstNode) }
    }

    private fun blockCommand(astNode: AstNode) {
        if (needToStopBlock) return

        when (astNode) {
            is APrintStmt -> runPrint(astNode)
            is AVarDeclarations -> varDeclarations(astNode)
            is AAssignStmt -> assignment(astNode)
            is AIfStmt -> ifStmt(astNode)
            is AWhileStmt -> whileStmt(astNode)
            is ACallExpr -> callStmt(astNode)
            is AReturnStmt ->  {
                val declaration = functionReturns[astNode] ?: error("Function for $astNode was not found")
                val expr = astNode.exp

                if (types[expr as AstNode] is BooleanType) booleans[declaration] = getBoolResult(expr)
                else if (types[expr] is StringType) strings[declaration] = getStringResult(expr)
                else if (types[expr] is IntegerType) integers[declaration] = getIntResult(expr)

                needToStopBlock = true
            }
        }
    }

    private fun assignment(astNode: AAssignStmt) {
        val declaration = links[astNode.left] ?: error("Declaration for ${astNode.left} was not found")

        if (booleans.containsKey(declaration)) booleans[declaration] = getBoolResult(astNode.right)
        else if (strings.containsKey(declaration)) strings[declaration] = getStringResult(astNode.right)
        else if (integers.containsKey(declaration)) integers[declaration] = getIntResult(astNode.right)
    }

    private fun varDeclarations(astNode: AVarDeclarations) {
        for (node in astNode.declarations) {
            if (types[node] is BooleanType) booleans[node] = getBoolResult(node.value)
            else if (types[node] is StringType) strings[node] = getStringResult(node.value)
            else if (types[node] is IntegerType) integers[node] = getIntResult(node.value)
        }
    }

    private fun runPrint(astNode: APrintStmt) {
        val result = expressionResult(astNode.exp)
        outputCallback.invoke(result)
//        if (outputResult == null) {
//            outputResult = StringBuilder()
//        }
//        outputResult?.appendLine(result)
        println(result)
    }

    private fun expressionResult(node: AExpr): String {
        if (node is ACallExpr) return funcStmt(node)
        else if (types[node as AstNode] is BooleanType) return getBoolResult(node).toString()
        else if (types[node] is StringType) return getStringResult(node)
        else if (types[node] is IntegerType) return getIntResult(node).toString()

        error("Cannot get expression result from $node")
    }

    private fun getStringResult(node: AExpr): String {
        if (node is AString) return node.value
        if (node is ABinaryOp) {
            if (node.operator is Plus) return getStringResult(node.left) + getStringResult(node.right)
        }
        if (node is AIdentifier) {
            val declaration = links[node] ?: error("Declaration for $node was not found")
            return strings[declaration] ?: error("Value for $declaration was not found")
        }
        if (node is ACallExpr) return funcStringStmt(node)
        error("Cannot get string result from $node")
    }

    private fun getIntResult(node: AExpr): Int {
        if (node is ANumber) return node.value
        if (node is AUnaryOp) {
            if (node.operator is Minus) return -getIntResult(node.subexp)
        }
        if (node is ABinaryOp) {
            if (node.operator is Plus) return getIntResult(node.left) + getIntResult(node.right)
            if (node.operator is Minus) return getIntResult(node.left) - getIntResult(node.right)
            if (node.operator is Times) return getIntResult(node.left) * getIntResult(node.right)
            if (node.operator is Divide) return getIntResult(node.left) / getIntResult(node.right)
        }
        if (node is AIdentifier) {
            val declaration = links[node] ?: error("Declaration for $node was not found")
            return integers[declaration] ?: error("Value for $declaration was not found")
        }
        if (node is ACallExpr) return funcIntStmt(node)
        error("Cannot get int result from $node")
    }

    private fun getBoolResult(node: AExpr): Boolean {
        if (node is ABoolean) return node.value
        if (node is AUnaryOp) {
            if (node.operator is Not) return !getBoolResult(node.subexp)
        }
        if (node is ABinaryOp) {
            if (node.operator is And) return getBoolResult(node.left) && getBoolResult(node.right)
            if (node.operator is Or) return getBoolResult(node.left) || getBoolResult(node.right)
            if (node.operator is LessThan) return getIntResult(node.left) < getIntResult(node.right)
            if (node.operator is LessOrEqualThan) return getIntResult(node.left) <= getIntResult(node.right)
            if (node.operator is GreaterThan) return getIntResult(node.left) > getIntResult(node.right)
            if (node.operator is GreaterOrEqualThan) return getIntResult(node.left) >= getIntResult(node.right)
            if (node.operator is Equal) return expressionResult(node.left) == expressionResult(node.right)
            if (node.operator is NotEqual) return expressionResult(node.left) != expressionResult(node.right)
        }
        if (node is AIdentifier) {
            val declaration = links[node] ?: error("Declaration for $node was not found")
            return booleans[declaration] ?: error("Value for $declaration was not found")
        }
        if (node is ACallExpr) return funcBoolStmt(node)
        error("Cannot get boolean result from $node")
    }
}