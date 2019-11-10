
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
            put(15,",");
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
            put(15,"/=");
            put(16,"is");
            put(17, "=>");
        }
    };
    private HashMap<Integer, String> keywordsList = new HashMap<>() {
        {
            put(1, "var");
            put(2, "empty");
            put(3, "if");
            put(4, "then");
            put(5,"else");
            put(6,"then");
            put(7,"end");
            put(8,"for");
            put(9,"while");
            put(10,"loop");
            put(11,"in");
            put(12,"return");
            put(13,"print");
            put(14,"int");
            put(15,"real");
            put(16,"bool");
            put(17,"string");
            put(18,"func");
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