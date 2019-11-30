import entities.Program;
import entities.Statement;

import java.io.IOException;
import java.util.List;

public class test_syntaxer {

    public static Program someMethod(Program b) {
        return new Program();
    }

    public static void main(String[] args) throws IOException {
        SyntaxParser parser = new SyntaxParser("test_programs/simple.pas");
        Program ast = parser.parse();
        List<Statement> statements = ast.getStatements();
        int counter = 0;
        for(Statement statement: statements) {
            System.out.println(statement);
        }
        Program a = someMethod(ast);
    }


}
