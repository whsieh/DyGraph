package example;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class AlchemyQueryExample {

	final static public String API_KEY = "d9c63c6528360164c610975a091d4eda26d55be5";
	final static public String ENDPOINT = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords";
	final static public String PARAMS = "?apikey=" + API_KEY + "&keywordExtractMode=strict&text=";
	
	public static void main(String[] args) {
		
		String myText = "Rocking out to K-Pop with . " +
				"Just took my last final at DVC. This semester has been so wonderful -- I've learned so much. Here's to a great start to summer. " + 
				"I love my job. " + 
				"New episode of Game of Thrones tonight. I'm excited. " + 
				"Just received a score of 129 on an Econ exam...oh my. " + 
				"JUST REGISTERED FOR AN APPLE HIRING EVENT. FINALLY. I'M SO EXCITED. (If you couldn't already tell.) " + 
				"Just scored a group interview at Apple. SO PUMPED. " + 
				"Sitting in on a Calc class at Davis. So legit. " + 
				"Just finished Steve Jobs by Isaacson. I'm very satisfied. " + 
				"My heart goes out to all the techies this year. I hope you weren't swamped with too many tech related questions/problems from family members this holiday. " + 
				"\"Don't be encumbered by history. Go off and do something wonderful.\" - Robert Noyce ;" + 
				"Fun times with Sarah Jupina! " + 
				"Home! Signed up for classes in 12 seconds (yes, I timed myself). Good day so far! ;" + 
				"It's time to change the world. " +
				"This is me hacking your Facebook. Obviously totally different from what you were saying. " + 
				"Had such a great day! " + 
				"Just had a meeting with my English teacher. Out of all the classes he teaches, I have the highest overall grade! What a great day! " + 
				"It feels so great to play saxophone again! " + 
				"My investments are in the positive (today at least). Success. :D " + 
				"Wooo HuK! Long live e-Sports! " + 
				"Just had the best coffee with Rik. Clover brewed coffee is amazing. We were talking about Salesforce and cloud computing when a PR manager from box.net comes over and asked if I was a software engineer. She says they were looking for new employees, so I gave her my LinkedIn. It feels like job opportunities just come to me! I love my field. :D " + 
				"Just ordered an iPhone case from the cutest girl in the Apple store. ";
		try {
			myText = URLEncoder.encode(myText,"UTF-8");
			String queryURI = ENDPOINT + PARAMS + myText;
			URL url;
			InputStream is = null;
			DataInputStream dis;
			int intChar;
			try {
			    url = new URL(queryURI);
			    is = url.openStream();  // throws an IOException
			    dis = new DataInputStream(new BufferedInputStream(is));
			    while ((intChar = dis.read()) != -1) {
			        System.out.print((char)intChar);
			    }
			} catch (MalformedURLException mue) {
			     mue.printStackTrace();
			} catch (IOException ioe) {
			     ioe.printStackTrace();
			} finally {
			    try {
			        is.close();
			    } catch (IOException ioe) {
			    }
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
