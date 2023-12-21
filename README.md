# Mini-IDE for IDELang
Mini-mini IDE for IDELang that provides you compile and launch your code

## IDELang description
IDELang is an educational programming language designed for the IDE Development course. 
With a focus on simplicity, it incorporates basic data types, arithmetic and boolean operations, 
conditional structures, as well as function and procedure definitions

*********
**Data types:**
* number: Support for integer arithmetic  
```var number = 42;```
* string: Represent textual data  
```var greeting = "Hello, IDELang!";```
* bool: Defined as `true` and `false`  
```var isTrue = true;```
*********

**Variables**  
  
Variables are declared using the `var` keyword, with type being inferred from the assigned value  
```var x = 5;```
*********

**Arithmetic Operations**  
  
Supported operations: +, -, *, /  
```var sum = x + 10;```
*********

**String Operations**  

Supported concatenation  
```var fullName = "Hello " + "World!";```

*********

**Boolean Operations**  
  
Supported operations: &&, ||, !, ==, !=  
```var bothTrue = (x > 4) && (z == true);```
*********

**Conditional Structures**  
* if(condition) { ... }
* if(condition) { ... } else { ... }  
Examples:  
```if (x > 10) { y = "Greater"; }```  
```if (z) { y = "True"; } else { y = "False"; }```
*********

**Loops**  

While loops: while(condition) { ... }  
```
var i = 0;
while (i < 5) {
   i = i + 1;
}
```
*********

**Functions**  
  
Functions are defined using the `func` keyword and their return type is inferred 
based on the type of the returned value

```
func add(a, b) 
{
  return a + b;
}
```
*********

**Procedures**  
  
Procedures are introduced using the `proc` keyword and do not return a value  

```
proc display(message) {
   ...
}
```
*********

**Print Operation**  
  
The print operation is used to display output  

```
print("Hello, World!");  // Displays: Hello, World!
print(x);                // Displays the value of x
print(y + " there!");    // Displays: hello there!
```      

## Compilation process description

he main class in my work is Compiler. 
When creating an object of the class there are several stages: 
* lexical parsing: transform text to tokens
* syntactic parsing: transform tokens to CST
* creation AST: transform CST to AST
* link resolution: map variables/func calls/proc calls to their declaration
* static type analysis: type checking and type inference

If the class object is successfully created, it can be considered that the program has been successfully compiled.  
In order to launch the program code, it is necessary to call the run method of the Compiler class object
