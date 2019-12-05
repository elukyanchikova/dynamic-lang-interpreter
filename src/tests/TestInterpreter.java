package tests;

import interpreter.Interpreter;
import syntaxer.SyntaxParser;
import syntaxer.entities.Program;

import java.io.IOException;

public class TestInterpreter {
    public static void main(String[] args) {
        Program program = null;
        try {
            program = new SyntaxParser("./test_programs/simple.pas").parse();
            Interpreter interpreter = new Interpreter(program);
            interpreter.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
