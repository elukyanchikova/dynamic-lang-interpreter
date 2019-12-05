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
            program = new SyntaxParser("./test_programs/simple.pas").parse();
            program = new SemanticAnalyzer(program).getAst();
            Interpreter interpreter = new Interpreter(program);
            interpreter.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
