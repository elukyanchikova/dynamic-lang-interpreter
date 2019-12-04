import entities.Program;
import entities.Statement;

import java.io.IOException;

public class test_semanter {

    public static void main(String[] args) throws IOException {
        SyntaxParser parser = new SyntaxParser("test_programs/simple.pas");
        Program ast = parser.parse();
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(ast);
        for(Statement s: semanticAnalyzer.getAst().getStatements()) {
            System.out.println(s);
        }
    }
}
