package dygraph.compare;

import com.restfb.types.StatusMessage;

public class KeywordData {

	final static public double DEFAULT_WEIGHT = 1.0;
	final static public double DEFAULT_SENTIMENT= 0.0;
	
	protected String keyword;
	protected StatusMessage[] myStatuses;
	
	public double weight;
	public double sentiment;
	
	public KeywordData(String keyword, double weight, double sentiment, StatusMessage[] myStatuses) {
		this.myStatuses = myStatuses;
		this.keyword = keyword;
		this.weight = weight;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public KeywordData(String keyword, StatusMessage[] myStatuses) {
		this(keyword,DEFAULT_WEIGHT,DEFAULT_SENTIMENT,myStatuses);
	}
	public String toString() {
		return "[" + keyword + "]";
	}
}
