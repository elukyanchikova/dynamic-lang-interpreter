import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;


public class Tokenizer {
	private BufferedReader reader;
	private List<RawToken> tokens;
	private int nLine, nPlace;

	public Tokenizer(InputStream stream) {
		InputStreamReader inputReader = new InputStreamReader(stream);
		this.reader = new BufferedReader(inputReader);

		this.tokens = new LinkedList<>();
	}

	public void tokenize() throws IOException {
		int cursor = reader.read();
		char read;
		StringBuilder tokenBuf = new StringBuilder();
		states state = states.WORD, prevState = states.WORD;
		nLine = 1;
		nPlace = 1;

		while (cursor != -1) {
			read = (char) cursor;
			do {
				if (read == '\'') {
					if (state == states.STRING1) {
						state = states.WORD;
						tokenBuf.append(read);
						flushTokenBuf(tokenBuf);
					} else {
						flushTokenBuf(tokenBuf);
						state = states.STRING1;
						tokenBuf.append(read);
					}
					break;
				}

				if (read == '\"') {
					if (state == states.STRING2) {
						state = states.WORD;
						tokenBuf.append(read);
						flushTokenBuf(tokenBuf);
					} else {
						flushTokenBuf(tokenBuf);
						state = states.STRING2;
						tokenBuf.append(read);
					}
					break;
				}

				if (state == states.STRING1 || state == states.STRING2) {
					tokenBuf.append(read);
					break;
				}

				if (read == ' ' || read == '\n') {
					flushTokenBuf(tokenBuf);
					if (read == '\n') {
						nLine++;
						nPlace = 1;
					}
					break;
				}

				if (Character.isLetter(read) || read == '_') {
					state = states.WORD;

					if (prevState == states.NUMBER) {
						if (tokenBuf.length() > 1 && tokenBuf.charAt(0) == '-') {
							tokenBuf.deleteCharAt(0);
							tokens.add(new RawToken('-', nLine, nPlace));
							nPlace++;
						}
					} else if (prevState != states.WORD) {
						flushTokenBuf(tokenBuf);
					}

					tokenBuf.append(read);
					break;
				}

				if (Character.isDigit(read)) {
					if (prevState == states.WORD || prevState == states.NUMBER) {
						tokenBuf.append(read);
					} else if (prevState == states.SUB) {
						state = states.NUMBER;
						tokenBuf.append(read);
					} else {
						state = states.NUMBER;
						flushTokenBuf(tokenBuf);
						tokenBuf.append(read);
					}
					break;
				}

				if (read == '-') {
					state = states.SUB;
					flushTokenBuf(tokenBuf);
					tokenBuf.append(read);
					break;
				}

				if (";,{}[]+".contains(String.valueOf(read))) {
					state = states.OTHER;
					flushTokenBuf(tokenBuf);
					tokens.add(new RawToken(read, nLine, nPlace));
					nPlace++;
					break;
				}

				if (read == ':') {
					state = states.COLON;
					flushTokenBuf(tokenBuf);
					tokenBuf.append(read);
					break;
				}

				if (read == '=') {
					state = states.EQ;
					if (prevState == states.COLON) {
						state = states.ASSIGN;
						tokenBuf.append(read);
						flushTokenBuf(tokenBuf);
					} else if (prevState == states.LT) {
						state = states.LE;
						tokenBuf.append(read);
						flushTokenBuf(tokenBuf);
					} else if (prevState == states.GT) {
						state = states.GE;
						tokenBuf.append(read);
						flushTokenBuf(tokenBuf);
					} else {
						flushTokenBuf(tokenBuf);
						tokens.add(new RawToken(read, nLine, nPlace));
						nPlace++;
					}
					break;
				}


				if (read == '<') {
					state = states.LT;
					flushTokenBuf(tokenBuf);
					tokenBuf.append(read);
					break;
				}

				if (read == '>') {
					state = states.GT;
					if (prevState == states.LT) {
						state = states.NE;
						tokenBuf.append(read);
						flushTokenBuf(tokenBuf);
					} else {
						flushTokenBuf(tokenBuf);
						tokenBuf.append(read);
					}
					break;
				}

				if (read == '(') {
					state = states.BR_OPEN;
					flushTokenBuf(tokenBuf);
					tokenBuf.append(read);
					break;
				}

				if (read == '*') {
					state = states.MUL;
					if (prevState == states.BR_OPEN) {
						state = states.COMM_OPEN;
						tokenBuf.append(read);
						flushTokenBuf(tokenBuf);
					} else {
						flushTokenBuf(tokenBuf);
						tokenBuf.append(read);
					}
					break;
				}

				if (read == ')') {
					state = states.BR_CLOSE;
					if (prevState == states.MUL) {
						state = states.COMM_CLOSE;
						tokenBuf.append(read);
						flushTokenBuf(tokenBuf);
					} else {
						flushTokenBuf(tokenBuf);
						tokens.add(new RawToken(read, nLine, nPlace));
						nPlace++;
					}
					break;
				}

				if (read == '/') {
					state = states.DIV;
					if (prevState == states.DIV) {
						state = states.COMM_LINE;
						tokenBuf.append(read);
						flushTokenBuf(tokenBuf);
					} else {
						flushTokenBuf(tokenBuf);
						tokenBuf.append(read);
					}
					break;
				}

				if (read == '.') {
					state = states.PERIOD;
					if (prevState == states.PERIOD) {
						state = states.RANGE;
						tokenBuf.append(read);
						flushTokenBuf(tokenBuf);
					} else {
						flushTokenBuf(tokenBuf);
						tokenBuf.append(read);
					}
					break;
				}

			} while (false);

			cursor = reader.read();
			prevState = state;
		}

		flushTokenBuf(tokenBuf);
	}

	public List<RawToken> getTokens() {
		return tokens;
	}

	private void flushTokenBuf(StringBuilder tokenBuf) {
		if (tokenBuf.length() > 0) {
			tokens.add(new RawToken(tokenBuf.toString(), nLine, nPlace));
			tokenBuf.setLength(0);
			nPlace++;
		}
	}

	private enum states {
		WORD, STRING1, STRING2, NUMBER,
		COLON, ASSIGN, PERIOD, RANGE,
		EQ, NE, LT, LE, GT, GE, MUL, DIV, SUB,
		BR_OPEN, BR_CLOSE,
		COMM_OPEN, COMM_CLOSE, COMM_LINE,
		OTHER
	}
}
