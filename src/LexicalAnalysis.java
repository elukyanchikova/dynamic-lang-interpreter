
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class LexicalAnalysis {

    private ArrayList<RawToken> delimiters;
    private ArrayList<RawToken> identifiers;
    private ArrayList<RawToken> literals;
    private ArrayList<RawToken> keywords;
    private ArrayList<RawToken> operators;
    private ArrayList<RawToken> nonSupportedTokens;

    public LexicalAnalysis() {

        delimiters = new ArrayList<>();
        identifiers = new ArrayList<>();
        literals = new ArrayList<>();
        keywords = new ArrayList<>();
        operators = new ArrayList<>();
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
        }
    };
    private HashMap<Integer, String> operatorsList = new HashMap<>() {
        {
            put(1, "+");
            put(2, "-");
            put(3, "*");
            put(4, "/");
            put(5, ":=");
            put(6, "div");
            put(7, "mod");
            put(8, "and");
            put(9, "or");
            put(10, "not");
            put(11, "xor");
            put(12, "<>"); //not equal
            put(13, "<");
            put(14, ">");
            put(15, "=");
            put(16, "<=");
            put(17, "=<");
            put(18, ">=");
            put(20, "=>");
            put(21, "in");
        }
    };
    private HashMap<Integer, String> keywordsList = new HashMap<>() {
        {
            put(1, "begin");
            put(2, "boolean");
            put(3, "break");
            put(4, "byte");
            put(5, "do");
            put(6, "double");
            put(7, "if");
            put(8, "else");
            put(9, "end");
            put(10, "false");
            put(11, "true");
            put(12, "integer");
            put(13, "longint");
            put(17, "shortint");
            put(14, "repeat");
            put(15, "shr");
            put(16, "single");
            put(18, "then");
            put(19, "until");
            put(20, "word");
            put(21, "program");
            put(22, "while");
            put(23, "var");
            put(24, "downto");
            put(25, "label");
            put(26, "record");
            put(27, "with");
            put(28, "procedure");
            put(29, "goto");
            put(30, "packed");
            put(31, "const");
            put(32, "type");
            put(33, "case");
            put(34, "function");
            put(35, "to");
            put(36, "of");
            put(37, "for");
            put(38, "array");
            put(39, "file");
            put(40, "set");
            put(41, "file");
            put(42, "char");
            put(43, "string");
            put(44, "writeln");
            put(45, "readln");

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
            delimiters.add(given);

        } else if (given.isOperator()) {
            operators.add(given);
        } else if (given.isKeyword()) {
            keywords.add(given);

        } else if (given.isLiteral()) {
            literals.add(given);

        } else if (given.isIdentifier()) {
            identifiers.add(given);

        } else {
            nonSupportedTokens.add(given);
        }
    }

    public void performLexicalAnalysis(String inputPath, String outputPath) throws IOException {
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
            RawToken token = new RawToken(tok.val, tok.line, tok.position);
            la.classify(token);
        }
        printWriter.println("Delimiters tokens: \n");
        for (RawToken t : la.delimiters) {
            printWriter.println(t.line + " " + t.position + " " + t.val);
            //printWriter.println(t.line +" " + t.place_at_line + " " + t.name + " " + t.type);
        }
        printWriter.println();
        printWriter.println("Literals tokens: \n");
        for (RawToken t : la.literals) {
            printWriter.println(t.line + " " + t.position + " " + t.val);

            //printWriter.println(t.line +" " + t.place_at_line + " " + t.name + " " + t.type);
        }
        printWriter.println();
        printWriter.println("Operators tokens: \n");
        for (RawToken t : la.operators) {
            //printWriter.println(t.line +" " + t.place_at_line + " " + t.name + " " + t.type);
            printWriter.println(t.line + " " + t.position + " " + t.val);

        }
        printWriter.println();
        printWriter.println("Keywords tokens: \n");
        for (RawToken t : la.keywords) {

            printWriter.println(t.line + " " + t.position+ " " + t.val);
            //printWriter.println(t.line +" " + t.place_at_line + " " + t.name + " " + t.type);
        }
        printWriter.println();
        printWriter.println("Identifiers tokens: \n");
        for (RawToken t : la.identifiers) {
            printWriter.println(t.line + " " + t.position + " " + t.val);

//            printWriter.println(t.line +" " + t.place_at_line + " " + t.name + " " + t.type);
        }
        printWriter.println();
        printWriter.println("Not defined tokens: \n");
        for (RawToken t : la.nonSupportedTokens) {
            printWriter.println(t.line + " " + t.position + " " + t.val);
        }

        printWriter.println("\nLexical analysis DONE");
        System.out.println("Lexical analysis DONE \n\nlook for the results in \'" + outputPath + "\'");
        printWriter.close();

    }

}