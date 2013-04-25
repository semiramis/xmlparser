package fpa.xmlparser;

public class ParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String xml = "<root><abc></abc>";
		String xml2 = "<quelle><adresse>http://www.willy-online.de/</adresse><beschreibung>alles zum wichtigsten Teil am Manne</beschreibung></quelle>";
		try {
			XmlParser.parse(xml2, true);
		} catch (XmlSyntaxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RootElementNotClosedException e) {

			e.printStackTrace();
		}

	}

}
