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
            program = new SyntaxParser("./test_programs/Conatenations.dy").parse();
            program = new SemanticAnalyzer(program).getAst();
            System.out.println();
            Interpreter interpreter = new Interpreter(program);
            interpreter.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
