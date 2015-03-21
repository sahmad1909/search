package search;

import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

@Component
public class LoadRank {

	public static String hello;

	public static String path = "C:\\Users\\L404008\\git\\seck\\seck\\resource\\";

	
	// this map contains URL and its page data. 
	public static Map<String, String> metaDataMap = new HashMap<String, String>();
	
	// This map contains the word and its exisitng of each page with count.
	public static Map<String, Map<String, Long>> wordMap = new HashMap<String, Map<String, Long>>();
	
	
	
	public static Map<String, Map<String, Double>> rankMapWthSumMap = new HashMap<String, Map<String, Double>>();

	
	public static Set<String> wordsList = new HashSet<String>();

	@PostConstruct
	public void init() throws Exception{
		
		loadMetadata();		
		loadIndexing();		
		loadRank();
		
		System.out.println(" ============ Map Loaded Successfully ================");
		
		
	}
	
	
	public static void main(String[] args) throws Exception {
		
		loadMetadata();		
		loadIndexing();		
		loadRank();
		
		System.out.println(" ======== ");
	}

	
	/**
	 * 
	 * @throws Exception
	 */
	public static void loadRank() throws Exception {

		JSONArray jsonArray = readJsonFile(path + "rank.json");

		for (Object object : jsonArray) {

			JSONObject jsonObject = (JSONObject) object;

			Double linkPageRank = (Double) jsonObject.get("linkPageRank");
			String url = (String) jsonObject.get("doc");
			JSONArray TFIDFjsonArray = (JSONArray) jsonObject.get("TFIDF");
			
			Map<String,Double> rankMap = new HashMap<String,Double>();

			
			for(Object tfidfObject:TFIDFjsonArray){				
				rankMap.putAll(getWordRank(tfidfObject,linkPageRank));
			}

			rankMapWthSumMap.put(url, rankMap);
		}

	}
	
	
	/**
	 * 
	 * @param object
	 * @param linkPageRank
	 * @return
	 */
	public static Map<String,Double> getWordRank(Object object, Double linkPageRank){
		
		JSONObject jsonObject = (JSONObject) object;
		Map<String,Double> rankMap = new HashMap<String,Double>();

		if(jsonObject.keySet()!=null && jsonObject.keySet().size() > 0){
			
			Iterator iterator = jsonObject.keySet().iterator();
			
			if(iterator!=null && iterator.hasNext()){
				String word = (String)iterator.next();				
				JSONArray rankingArray = (JSONArray)jsonObject.get(word);					
				rankMap.put(word.toLowerCase(), getRankSum(rankingArray,linkPageRank));
				
			}			
		}	
		
		return rankMap;
	}
	
	/**
	 * 
	 * @param rankingArray
	 * @param linkPageRank
	 * @return
	 */
	public static double getRankSum(JSONArray rankingArray, Double linkPageRank){
		
		double rank = 0d;
		
		for(Object object:rankingArray){
			
			rank += (Double)object;
		}
		
		Double pageRank = new Double(linkPageRank);		
		rank += pageRank;
		return formatDecimal(rank);
	}

	// Loading Index Mapping into Map
	
	/**
	 * 
	 * @throws Exception
	 */
	public static void loadIndexing() throws Exception {

		JSONArray jsonArray = readJsonFile(path + "index.json");

		for (Object object : jsonArray) {

			JSONObject jsonObject = (JSONObject) object;
			


			// Getting the Word document from Array which has count exist in
			// each dcoument
			if (jsonObject != null && jsonObject.keySet().size() == 1) {

				Iterator iterator = jsonObject.keySet().iterator();
				
				if(iterator!=null && iterator.hasNext()){
				String word = (String) iterator.next();
				JSONArray jsonDocumnetWithCountArray = (JSONArray) jsonObject
						.get(word);

				Map<String, Long> documentWordCountMap = new HashMap<String, Long>();
				for (Object documentJosnObject : jsonDocumnetWithCountArray) {

					JSONObject documentCountOjecnt = (JSONObject) documentJosnObject;

					if (documentCountOjecnt != null
							&& documentCountOjecnt.keySet().size() > 0) {
						Iterator documentIterator = documentCountOjecnt.keySet().iterator();
						
						if(documentIterator!=null && documentIterator.hasNext()){
						String url = (String) documentIterator.next();
						Long count = new Long((Long)documentCountOjecnt.get(url));
						documentWordCountMap.put(url, count);
						}
					}
				}

					wordMap.put(word.toLowerCase(), documentWordCountMap);
					wordsList.add(word.toLowerCase());
				}
			}

		}

	}

	/**
	 * 
	 * @throws Exception
	 */
	public static void loadMetadata() throws Exception {

		String metaDataFile = path + "hw2.json";
		JSONArray jsonArray = readJsonFile(metaDataFile);

		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			String rootURL = (String) jsonObject.get("url");
			String pageData = (String) jsonObject.get("pagedata");
			metaDataMap.put(rootURL, pageData);
		}

	}

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static JSONArray readJsonFile(String fileName) throws Exception {
		JSONParser parser = new JSONParser();
		Object object = parser.parse(new FileReader(fileName));
		JSONArray jsonArray = (JSONArray) object;
		return jsonArray;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static double formatDecimal(Double value){		
		if(value!=null){
			
			DecimalFormat newFormat = new DecimalFormat("#.#####");
			return Double.valueOf(newFormat.format(value));
		}
		
		return 0.0;
	}

}
