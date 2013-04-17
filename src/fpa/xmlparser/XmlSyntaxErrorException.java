package fpa.xmlparser;

@SuppressWarnings("serial")
public class XmlSyntaxErrorException extends Exception {

	public XmlSyntaxErrorException(String message) {
		super(message);

	}

	public XmlSyntaxErrorException(String message, int i, String text, int errorArea) {
		super("Error - "+ message	+ " near: \n"	+ text.substring((i - errorArea < 0 ? 0 : i - errorArea), 
				(i	+ errorArea > text.length() - 1 ? text.length() - 1 : i	+ errorArea)));
	}

}
