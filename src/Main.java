import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        //example  of tokenizator usage
		/*String sampleCode = "if (alpha>beta) then";
		InputStream fromString = new ByteArrayInputStream(sampleCode.getBytes());
		InputStream fromFile = new FileInputStream("SamplePascal.pas");
		Tokenizer tokenizer = new Tokenizer(fromFile);

		tokenizer.tokenize();
		fromFile.close();

		List<RawToken> tokens = tokenizer.getTokens();

		System.out.println("Lexical analysis START\n");
		LexicalAnalysis la = new LexicalAnalysis();
		for (String tok : tokens) {
			//System.out.println(tok);
			Token token = new Token(tok,1,1);
			la.classify(token);
		for (RawToken tok : tokens) {
			System.out.printf("[%d, %d] %s\n", tok.line, tok.place, tok.val);
		}
		*/

        System.out.println("Input the name of .pas file (like 'Name.pas') or press Enter to watch a sample file: ");
        Scanner in = new Scanner(System.in);
        String inputFile = in.nextLine();
        LexicalAnalysis la = new LexicalAnalysis();
        if (inputFile.isEmpty()) {
            inputFile = "SamplePascal.pas";
        }
        la.performLexicalAnalysis(inputFile, "Output.txt");

    }

}
