package tests;

import interpreter.Interpreter;
import semanter.SemanticAnalyzer;
import syntaxer.SyntaxParser;
import syntaxer.entities.Program;

import java.io.IOException;

public class TestInterpreter {
    public static void main(String[] args) {
        Program program = null;
        try {
            // Concatenations, Dynamic, FuncToFunc, SemanticError, SyntaxWrong, LoopIf
            program = new SyntaxParser("./test_programs/LoopIf.dy").parse();
            program = new SemanticAnalyzer(program).getAst();
            System.out.println(program);
            Interpreter interpreter = new Interpreter(program);
            interpreter.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
