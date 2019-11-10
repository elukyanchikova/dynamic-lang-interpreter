import javax.xml.stream.FactoryConfigurationError;
import java.lang.Number;

public class RawToken {

    public String val;
    public int line, position;
    public TokenType type;
    private LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();

    protected enum TokenType {
        DELIMITER,
        IDENTIFIER,
        LITERAL,
        KEYWORD,
        OPERATOR,
        NOT_A_TYPE,
    }

    public RawToken(String val, int line, int position) {
        this.val = val;
        this.line = line;
        this.position = position;
        this.type = TokenType.NOT_A_TYPE;

    }

    public RawToken(char val, int line, int position) {
        this.val = String.valueOf(val);
        this.line = line;
        this.position = position;
        this.type = TokenType.NOT_A_TYPE;

    }

    protected void setTokenType(TokenType tokenType) {
        this.type = tokenType;
    }

    public Boolean isDelimiter() {
        return this.lexicalAnalysis.getDelimitersList().containsValue(this.val);
    }

    private Boolean consistLegal() {
        char[] temp = this.val.toCharArray();
        for (int i = 0; i < temp.length; i++) {
            if (!((int) temp[i] == (int) '_' ||
                    (65 <= (int) temp[i]) && ((int) temp[i] <= 90) ||
                    (97 <= (int) temp[i]) && (int) temp[i] <= 122)) {
                return false;
            } else {
                if (this.lexicalAnalysis.getOperatorsList().containsValue(String.valueOf(temp[i])) ||
                        this.lexicalAnalysis.getDelimitersList().containsValue(String.valueOf(temp[i]))) {
                    return false;
                }
            }
        }

        return true;
    }

    public Boolean isIdentifier() {
        return this.consistLegal();
    }


    public Boolean isOperator() {
        return this.lexicalAnalysis.getOperatorsList().containsValue(this.val);

    }

    public Boolean isKeyword() {
        return this.lexicalAnalysis.getKeywordsList().containsValue(this.val);
    }

}
