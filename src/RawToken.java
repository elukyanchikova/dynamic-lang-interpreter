public class RawToken {
    
    public String val;
    public int line, position;
    public TokenType type;
    private LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();

    private enum TokenType {
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

    private void setTokenType(TokenType tokenType) {
        this.type = tokenType;
    }

    public Boolean isDelimiter() {
        if (this.lexicalAnalysis.getDelimitersList().containsValue(this.val)) {
            setTokenType(TokenType.DELIMITER);
            return true;
        }
        return false;
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
        if (this.consistLegal()
        ) {
            setTokenType(TokenType.IDENTIFIER);
            return true;
        } else return false;
    }
 //TODO  add strings and reals
    public Boolean isLiteral() {
        try {
            Integer.parseInt(this.val);
            setTokenType(TokenType.LITERAL);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public Boolean isOperator() {
        if (this.lexicalAnalysis.getOperatorsList().containsValue(this.val)) {
            setTokenType(TokenType.OPERATOR);
            return true;
        }
        return false;

    }

    public Boolean isKeyword() {
        if (this.lexicalAnalysis.getKeywordsList().containsValue(this.val)) {
            setTokenType(TokenType.KEYWORD);
            return true;
        }
        return false;
    }

}
