package example;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GoogleResultCountExample {

	public static final String ENDPOINT = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
	public static final String ENCODING = "UTF-8";
	public static final String RESULT_COUNT_ID = "\"estimatedResultCount\":\"";
	
	/**
	 * The only purpose here is to obtain the estimated number of hits for a 
	 * given keyterm. The rest of the search data is not utilized.
	 */
	public static void main(String[] args) {
		
		String rawTerm = "AIME";
		InputStream inputStream = null;
		DataInputStream dataStream;
		int intChar;
		StringBuilder str = new StringBuilder();
		
		try {
			
			String term = URLEncoder.encode(rawTerm,ENCODING);
			URL queryURL = new URL(ENDPOINT + term);
			inputStream = queryURL.openStream();
			dataStream = new DataInputStream(new BufferedInputStream(inputStream));
			while ((intChar = dataStream.read()) != -1) {
				str.append((char)intChar);
			}
			String result = str.toString();
			int countIndex = result.indexOf(RESULT_COUNT_ID);
			if (countIndex != -1) {
				result = result.substring(countIndex+RESULT_COUNT_ID.length());
				result = result.substring(0, result.indexOf('\"'));
				float count = Integer.parseInt(result)/1000000f;
				System.out.println("The key phrase: \"" + rawTerm + "\" has " + count + " million hits on Google.");
			}
			
		} catch (UnsupportedEncodingException e) {
			System.err.println("Error: " + ENCODING + " is not a valid encoding type.");
		} catch (MalformedURLException e) {
			System.err.println("Error: invalid query URL. Details below:");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error: could not open a connection. Details below:");
			e.printStackTrace();
		}
		
		
	}
	
	
	
}
