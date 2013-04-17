package fpa.xmlparser;

public class ParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String xml = "<root><abc /></root>";
		try {
			XmlParser.parse(xml);
		} catch (XmlSyntaxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

	}

}
