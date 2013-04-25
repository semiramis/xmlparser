package fpa.xmlparser;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Let's play a game :)
 * 
 * @author Maria Lindemann, Franz Benthin, Kathrin Konkol
 * 
 */
public class XmlParser {

	public static final Pattern LEGAL_TAG_START = Pattern
			.compile("([a-z]|[A-Z]|\\-|\\.|:|_)");
	public static final String TAG = "TAG";
	public static final String CLOSING_TAG = "CLOSING_TAG";
	public static final String ELEMENT = "ELEMENT";
	public static final String PROCESS_INSTR = "PROCESS_INSTRUCTION";

	/*
	 * parse erwartet einen String im xml Format folgende Syntax Regeln werden
	 * ber�cksichtigt: passende opening und closing tags erkennt empty tags und
	 * ber�cksichtigt sie nicht < au�erhalb eines tags wirft eine exception >
	 * au�erhalb eines tags wird erkannt und nicht ber�cksichtigt tagnamen in
	 * case sensitiv und beliebig vielen leerzeichen vor der ausgabe wird
	 * gepr�ft, ob alle opening tags geschlossen wurden, die datei also
	 * vollst�ndig abgearbeitet werden konnte es werden nur tagnamen als solche
	 * erkannte, die mit einem der folgenden zeichen beginnen:
	 * "Buchstabe, -, ., :, _"
	 * 
	 * @TODO kommentare erkennung eines kommentars mit richtiger syntax
	 * 
	 * arbeitsweise der methode: string wird char f�r char gelesen. bestimmte
	 * symbole werden auf konditionen gepr�ft und als openingtag, closingtag,
	 * emptytag, kommentar oder nichts von alledem erkannt. dementsprechend wird
	 * ein opening tag auf den stack geschmissen. der n�chste closingtag, der
	 * erkannt wird, wird direkt mit dem obersten opening tag auf dem stack
	 * verglichen. stimmen die strings nicht �berein, wird eine exception
	 * ausgel�st.
	 */
	public static String parse(String text, boolean print)
			throws XmlSyntaxErrorException, RootElementNotClosedException {

		final int ERROR_AREA = 30;

		Matcher m;

		char[] chars = text.toCharArray();
		boolean inTag = false;
		boolean inClosingTag = false;
		boolean inQuotes = false;
		boolean inComment = false;
		boolean inPI = false;

		int depth = 0;
		int rowCount = 0;
		ArrayList<String[]> sList = new ArrayList<String[]>();
		StringBuilder xmlDoc = new StringBuilder();
		StringBuilder element = new StringBuilder();
		StringBuilder tag = new StringBuilder();
		StringBuilder processInstruction = new StringBuilder();
		Stack<String> stack = new Stack<String>(); // stacks the elements and
													// pops the last opened

		if (chars[0] != '<') {
			throw new XmlSyntaxErrorException("document must start with '<'",
					rowCount, 0, text, ERROR_AREA);
		}

		for (int i = 0; i < chars.length; i++) {

			switch (chars[i]) {
			case '<':
				if (!element.toString().equals(""))
					sList.add(new String[] { element.toString(),
							XmlParser.ELEMENT });
				if (inTag) {
					throw new XmlSyntaxErrorException("illegal '<'", rowCount,
							i, text, ERROR_AREA);
				}

				m = LEGAL_TAG_START.matcher(Character.toString(chars[i + 1]));

				if (!inComment && !inQuotes && !inPI) {

					if (m.find()) { // legaler tag-start: [a-z]|[A-Z]|\-|\.|:|_
						inTag = true;
					} else if (chars[i + 1] == '/') { // schliessender tag
						inClosingTag = true;
						inTag = true;
					} else if (chars[i + 1] == '!' && chars[i + 2] == '-'
							&& chars[i + 3] == '-') { // kommentar
						inComment = true;
					} else if (chars[i + 1] == '?') {// process instruction
						inPI = true;
					} else {
						throw new XmlSyntaxErrorException("illegal tag start",
								rowCount, i, text, ERROR_AREA);
					}
				}

				break;

			case '>':
				if (inTag) {
					if (!inComment && !inQuotes && !inPI) {

						if (chars[i - 1] != '/') {
							if (inClosingTag) {
								String opening = stack.pop().trim();
								String closing = tag.toString().substring(2)
										.trim();

								sList.add(new String[] { closing,
										XmlParser.CLOSING_TAG });

								if (!opening.equals(closing)) {

									throw new XmlSyntaxErrorException("\"</"
											+ opening + ">\" expected",
											rowCount, i, text, ERROR_AREA);

								}
							} else {

								stack.add(tag.toString().substring(1));
								sList.add(new String[] { stack.lastElement(),
										XmlParser.TAG });

							}

						}

						inTag = false;
						inClosingTag = false;
						tag = tag.delete(0, tag.length());
					} else if (inPI && chars[i - 1] == '?') {
						// ende der process instruction
						// processInstruction.append("\n\n");
						// sList.add(new String[]{processInstruction.toString(),
						// XmlParser.PROCESS_INSTR});
						inPI = false;
					} else if (inPI && chars[i - 1] != '?') {
						// syntaxfehler bei erstellung des PI
					}
				}

				if (inComment && chars[i - 2] == '-' && chars[i - 1] == '-') {
					inComment = false;
				}
				break;

			case '"':
				if (!inComment) {
					if (!inQuotes) {
						inQuotes = true;
					} else {
						inQuotes = false;
					}

				}

				break;

			case '\\':
				if (chars[i + 1] == 'n' && chars[i - 1] != '\\') {
					rowCount++;
				}

				// hier um weitere Fälle erweitern

				// Hier werden die elemente-strings gebaut
			default:
				if (!inComment && !inTag && !inClosingTag && !inPI) {
					element.append(chars[i]);
				} else {
					if (!element.equals(""))
						element = new StringBuilder();
				}

				if (inPI) {
					// sList.add(new String[]{"bd", XmlParser.PROCESS_INSTR});
					inPI = false;
				}
				break;

			}

			// strings bauen

			if (inTag) {
				tag.append(chars[i]);
			}
			xmlDoc.append(chars[i]);
		}

		if (stack.empty() && print) {

			return printXml(sList);

		} else {
			throw new RootElementNotClosedException(
					"There are opened Elements left...");
		}

	}

	private static String printXml(ArrayList<String[]> sList) {
		StringBuilder str = new StringBuilder();

		int k = 0;
		for (String[] s1 : sList) {
			if (!s1[0].equals("")) {
				if (s1[1].equals(XmlParser.TAG)) {
					for (int j = 0; j < k; j++) {
						str.append("\t");
					}
					k++;
					str.append("<" + s1[0] + ">" + "\n");
				}
				if (s1[1].equals(XmlParser.ELEMENT)) {
					for (int j = 0; j < k; j++) {
						str.append("\t");
					}
					str.append(s1[0] + "\n");
				}
				if (s1[1].equals(XmlParser.CLOSING_TAG)) {
					k--;
					for (int j = 0; j < k; j++) {
						str.append("\t");
					}
					str.append("</" + s1[0] + ">" + "\n");
				}
				if (s1[1].equals(XmlParser.PROCESS_INSTR)) {
					str.append(s1[0] + "\n\n");

				}
			}
		}

		System.out.println(str);

		return str.toString();
	}
}
