
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class LexicalAnalysis {

    private ArrayList<RawToken> tokens;
    private ArrayList<RawToken> nonSupportedTokens;

    public LexicalAnalysis() {
        tokens = new ArrayList<>();
        nonSupportedTokens = new ArrayList<>();
    }

    private HashMap<Integer, String> delimitersList = new HashMap<>() {
        {
            put(1, "{");
            put(2, ";");
            put(3, ".");
            put(4, "'");
            put(5, "(");
            put(6, "[");
            put(7, "(.");
            put(8, "(*");
            put(9, ":");
            put(10, "}");
            put(11, ")");
            put(12, "]");
            put(13, ".)");
            put(14, "*)");
            put(15, ",");
        }
    };
    private HashMap<Integer, String> operatorsList = new HashMap<>() {
        {
            put(1, "+");
            put(2, "-");
            put(3, "*");
            put(4, "/");
            put(5, "and");
            put(6, "or");
            put(7, "not");
            put(8, "xor");
            put(9, ":=");
            put(10, "<");
            put(11, ">");
            put(12, "=");
            put(13, "<=");
            put(14, ">=");
            put(15, "/=");
            put(16, "is");
            put(17, "=>");
        }
    };
    private HashMap<Integer, String> keywordsList = new HashMap<>() {
        {
            put(1, "var");
            put(2, "empty");
            put(3, "if");
            put(4, "then");
            put(5, "else");
            put(6, "then");
            put(7, "end");
            put(8, "for");
            put(9, "while");
            put(10, "loop");
            put(11, "in");
            put(12, "return");
            put(13, "print");
            put(14, "int");
            put(15, "real");
            put(16, "bool");
            put(17, "string");
            put(18, "func");
            put(19, "true");
            put(20, "false");
            put(21, "readInt");
            put(22, "readReal");
            put(23, "readString");
        }
    };


    public HashMap<Integer, String> getDelimitersList() {
        return delimitersList;
    }

    public HashMap<Integer, String> getKeywordsList() {
        return keywordsList;
    }

    public HashMap<Integer, String> getOperatorsList() {
        return operatorsList;
    }


    private void classify(RawToken given) {

        if (given.isDelimiter()) {
            given.setTokenType(RawToken.TokenType.DELIMITER);
            tokens.add(given);

        } else if (given.isOperator()) {
            given.setTokenType(RawToken.TokenType.OPERATOR);
            tokens.add(given);
        } else if (given.isKeyword()) {
            given.setTokenType(RawToken.TokenType.KEYWORD);
            tokens.add(given);

        } else if (given.isIdentifier()) {
            given.setTokenType(RawToken.TokenType.IDENTIFIER);
            tokens.add(given);

        } else  {
            given.setTokenType(RawToken.TokenType.LITERAL);
            tokens.add(given);
        }
    }

    public ArrayList<RawToken> lexerGetTokens(String inputPath, String outputPath) throws IOException {
        //Open file
        InputStream fromFile = new FileInputStream(inputPath);
        Tokenizer tokenizer = new Tokenizer(fromFile);
        tokenizer.tokenize();
        fromFile.close();

        List<RawToken> tokens = tokenizer.getTokens();

        FileWriter fileWriter = new FileWriter(outputPath);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println("\n\'" + inputPath + "\'" + " to be analysed\n");
        printWriter.println("Lexical analysis START\n");
        System.out.println("\n\'" + inputPath + "\'" + " to be analysed\n");

        System.out.println("Lexical analysis START");
        printWriter.println("\nline | place at line| Token \n\n");

        LexicalAnalysis la = new LexicalAnalysis();
        for (RawToken tok : tokens) {
            la.classify(tok);
        }

        System.out.println("Lexical analysis performed. Look for the full report in lexerOutput.txt");

        printWriter.println("Classified tokens: \n");
        for (RawToken t : la.tokens) {
            printWriter.println(t.line + " " + t.position + " " + t.val + " " + t.type);
            printWriter.println("Misclassified tokens: \n");
        }
        printWriter.close();

        return la.tokens;

    }

}