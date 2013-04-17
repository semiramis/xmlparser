package fpa.xmlparser;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: maria
 * Date: 13.04.13
 * Time: 21:06
 * To change this template use File | Settings | File Templates.
 */
public class XmlParser{

	 public static void parse(String text){

		  char[] chars = text.toCharArray();
		  boolean inTag = false;
		  boolean inQuotes = false;
		  boolean inComment = false;
		  int depth = 0;
		  StringBuilder xmlDoc = new StringBuilder();
		  StringBuilder element = new StringBuilder();
		  Stack<String> stack = new Stack<String>(); // stacks the elements and pops the last opened

		  for(int i=0; i<chars.length; i++){

				switch(chars[i]){
					 case '<':
						  if(!inComment && !inQuotes){
								inTag = true;
						  }

						  break;

					 case '>':
						  if(!inComment && !inQuotes){
								inTag = false;
						  }

						  break;

					 case '"':
						  if(!inComment){
								if(!inQuotes){
									 inQuotes=true;
								}else{
									 inQuotes=false;
								}

						  }

						  break;
					 //hier um weitere Fälle erweitern
				}

				//strings bauen und stack überprüfen

				xmlDoc.append(chars[i]);
		  }
	 }
}
