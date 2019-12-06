# [F19] Compilers Construction - Course Project
The **Interpreter for Toy Dynamic Language** has been written as a project for Innopolis University **Compilers Constuction** course, Fall 2019.
## Specification
### Functionality
The interprter (it is an interpreter, not compiler, as the intermediate representation is excecuted without translating into machine code) should perform lexical, syntax and semantic analysis successively and then interpret the result to process the output of source program in the Toy language.

![](https://i.imgur.com/huylKDt.jpg)
#### Lexical analyser
The idea of lexical analysis is to take the source code and break it into chunks called tokens. Token - is a string with an assigned and thus identified meaning.
Toy language states the following types of tokens:

| Token type| Example|
| -------- | -------- |
| keyword | **string**, **return**, **print**, **while**, etc.}
| delimiter| **,**, **:**, **(** , **]** |
|operator| **+**, **or**, **is**, **:=**|
|identifier| **abc123**, **Abc_123** *// no start with digit or '_'*|
|literal | any string|

At first step, lexer divide all the input code into chunks, similar to what is in the table. At the second, lexer identifies theirs types. The token itself contains the following fields :
`token(String value, TokenType type, int positionY, int positionX)`*, 
where positionY - line of code, positionX - place in line*

Classified tokens are transferred to Syntax analyser then.

#### Syntax analyser

Syntax analyser performs comparing to Toy Language Grammar rules, thus simultaneously checks the validity of target program syntax (if not valid - raise SyntaxError) and build and Abstract Syntax Tree -  tree representation of the abstract syntactic structure of source code. Each node of the tree denotes a construct occurring in the source code.

The hierarchy of Grammar entities is presented as follows. 
![](https://i.imgur.com/p6WSzvH.png)
For more information about Grammar rules, please, check the [Language specification](https://github.com/elukyanchikova/dynamic-lang-interpreter/tree/master/language%20specification)

If source program has no syntax errors, the AST is transferred to Semantic analyser, otherwise, none of the follows stages proceeds.

#### Semantic analyser

Semantic analysis stage tends to simplify the
AST (find the smallest possible representation of the particular entity) and check validity of some operations and calls(for example, undefined or redefined variable).

Simplification is done as follows: starting at the root (see picture of Grammar entities hierarchy above), semanter tries to cast the target entity to its "children" entities (based on number and type of parameters) recurcivelly and stop at the "child" that is suitable.

For example, some string **t** is given as an elemenent of an array in the source program `var arr = [t, "anotherString"]`. In the AST after syntax analysis **t** will be a class "Expression". At semantic simplification stage this expression will firstly simplified to *Unary*,then to *Primary*, *Literal* and finally *StringLiteral*. Thus, semanter "takes off" the excess entities class wrappers.

#### Interpreter 
The purpose of intepreter is to evaluate all expressions inside the AST in the way which depend on theirs type and add the correspondig result in the scope. Toy language allows "prints", so, if the source program states so, results of differents expressions could be outputed.

As the interpreter does not perform translation into machine code, but does directly execute the code, due to **dynamic typing** of language, the interpreter also catch runTime errors.

### Language specification
Look for the language definition [here](https://github.com/elukyanchikova/dynamic-lang-interpreter/tree/master/language%20specification).

## Source
Implementation language - Java. Look for the source code [here](https://github.com/elukyanchikova/dynamic-lang-interpreter).


## Team & Contribution
Maxim Burov - syntaxer, semanter, interpreter    
Arina Fedorovskaya - lexer, interpreter   
Elena Lukyanchikova - lexer, interpreter   
Marsel Shaykhin - syntaxer, interpreter   
SE-17-01   
