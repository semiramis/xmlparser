package fpa.xmlparser;

import java.util.Stack;

/**
 * Let's play a game :)
 */
public class XmlParser {

	public static void parse(String text) throws XmlSyntaxErrorException {

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
				if (!inComment && !inQuotes) {
					
					if(chars[i+1] == '/'){
						inClosingTag = true;
					}

					if (chars[i + 1] == '!' && chars[i + 2] == '-'
							&& chars[i + 3] == '-') {
						inComment = true;

					} else {
						inTag = true;
					}

				}

				break;

			case '>':
				if (inComment && chars[i - 2] == '-' && chars[i - 1] == '-') {
					inComment = false;
				} else if (!inComment && !inQuotes) {
					
					if(chars[i-1] == '/'){
						if(chars[i-2] != ' '){
							throw new XmlSyntaxErrorException("Error - whitespace expected near: \n"+ text.substring((i-10 < 0 ? 0 : i-10), (i+10>chars.length-1 ? chars.length-1 : i+10)));
						}
						
					}else{
						if(inClosingTag){
							String opening = stack.pop();
							String closing = tag.toString().substring(3);
							if(!opening.equals(closing)){
								throw new XmlSyntaxErrorException("Error - \"</" + closing + ">\" expected near: \n"+ text.substring((i-10 < 0 ? 0 : i-10), (i+10>chars.length-1 ? chars.length-1 : i+10)));
							}
						}else{
							stack.add(tag.toString().substring(1));
						}
						
					}
					inTag = false;
					inClosingTag = false;
					tag = tag.delete(0, tag.length()-1);
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

			// strings bauen und stack überprüfen

			if(inTag){
				tag.append(chars[i]);
			}
			xmlDoc.append(chars[i]);
		}
		
		System.out.println(xmlDoc.toString());
	}
}
