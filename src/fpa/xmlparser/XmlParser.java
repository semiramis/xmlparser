package fpa.xmlparser;

import java.util.Stack;

/**
 * Let's play a game :)
 * @author Maria Lindemann, Franz Benthin, Kathrin Konkol
 * 
 */
public class XmlParser {
	
	/*
	 * parse erwartet einen String im xml Format
	 * folgende Syntax Regeln werden ber�cksichtigt:
	 * passende opening und closing tags
	 * erkennt empty tags und wirft eine exception, falls das leerzeichen fehlt
	 * < au�erhalb eines tags wirft eine exception
	 * > au�erhalb eines tags wird erkannt und nicht ber�cksichtigt
	 * tagnamen mit case sensitiv
	 * vor der ausgabe wird gepr�ft, ob alle opening tags geschlossen wurden, die datei also 
	 * 	vollst�ndig abgearbeitet werden konnte
	 * TODO
	 * kommentare
	 * erkennung eines kommentars mit richtiger syntax
	 * 
	 * arbeitsweise der methode:
	 * string wird char f�r char gelesen. bestimmte symbole werden auf konditionen gepr�ft und als openingtag,
	 * closingtag, emptytag, kommentar oder nichts von alledem erkannt. dementsprechend wird ein opening tag auf 
	 * den stack geschmissen. der n�chste closingtag, der erkannt wird, wird direkt mit dem obersten opening tag 
	 * auf dem stack verglichen. stimmen die strings nicht �berein, wird eine exception ausgel�st. 
	 * 
	 */
	public static void parse(String text) throws XmlSyntaxErrorException, RootElementNotClosedException {

		final int ERROR_AREA = 30;

		char[] chars = text.toCharArray();
		boolean inTag = false;
		boolean inClosingTag = false;
		boolean inQuotes = false;
		boolean inComment = false;
		int depth = 0;
		StringBuilder xmlDoc = new StringBuilder();
		StringBuilder element = new StringBuilder();
		StringBuilder tag = new StringBuilder();
		Stack<String> stack = new Stack<String>(); // stacks the elements and
													// pops the last opened

		for (int i = 0; i < chars.length; i++) {

			switch (chars[i]) {
			case '<':
				if (inTag) {
					throw new XmlSyntaxErrorException("illegal '<'", i, text,ERROR_AREA);

				}
				if (!inComment && !inQuotes) {

					if (chars[i + 1] == '/') {
						inClosingTag = true;
						inTag = true;
					}else if (chars[i + 1] == '!' && chars[i + 2] == '-'
							&& chars[i + 3] == '-') {
						inComment = true;

					}else if(){
						inTag = true;
					}else{
						
					}

				}

				break;

			case '>':
				if (inTag) {
					if (inComment && chars[i - 2] == '-' && chars[i - 1] == '-') {
						inComment = false;
					} else if (!inComment && !inQuotes) {

						if (chars[i - 1] != '/') {
							if (inClosingTag) {
								String opening = stack.pop().trim();
								String closing = tag.toString().substring(2)
										.trim();
								if (!opening.equals(closing)) {
									
									throw new XmlSyntaxErrorException("\"</"+ opening + ">\" expected", i,text, ERROR_AREA);

								}
							} else {
								stack.add(tag.toString().substring(1));
							}


						} 
						
						
						inTag = false;
						inClosingTag = false;
						tag = tag.delete(0, tag.length());
					}
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
			// hier um weitere Fälle erweitern
			}

			// strings bauen 

			if (inTag) {
				tag.append(chars[i]);
			}
			xmlDoc.append(chars[i]);
		}

		if(stack.empty()){
		System.out.println(xmlDoc.toString());
		}
		else{
			throw new RootElementNotClosedException("There are opened Elements left...");
		}
		
	}
}
