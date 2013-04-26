package fpa.xmlparser;

public class ParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String xml = "<root><abc></abc>";
		String xml2 = "<quelle><adresse>http://www.willy-online.de/</adresse><beschreibung>alles zum wichtigsten Teil am Manne</beschreibung></quelle>";
		String xml3 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
				"<verzeichnis><titel>Wikipedia Städteverzeichnis</titel><eintrag>"+
				"<stichwort>Genf</stichwort><eintragstext>Genf ist der Sitz von ...</eintragstext>"+
				"</eintrag><eintrag><stichwort>Köln</stichwort><eintragstext>Köln ist eine Stadt, die ...</eintragstext>"+
				"</eintrag></verzeichnis>";
		try {
			System.out.println(XmlParser.parse(xml3, true));
		} catch (XmlSyntaxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RootElementNotClosedException e) {

			e.printStackTrace();
		}

	}

}
