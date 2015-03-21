package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import antlr.collections.List;

@Controller
public class SearchController {

	@RequestMapping(value = "/result.html", method = RequestMethod.POST)
	public String search(@RequestParam(required = false) String searchString,
			ModelMap modelMap) {

		System.out.println("Search Called " + searchString);
		modelMap.put("searchString", searchString);
		
		if (!StringUtils.isEmpty(searchString)) {

			// Get the document List in which word exist
			// Validate if word map counts is not null
			// Get the URL of word and search the word in Ranking Map
			// Get the word and their rank from Ranking Map
			// Create new map contains the rank with URL and page data.

			
			TreeMap<Double,PageData> treeMapWithResult = new TreeMap<Double,PageData>();
			
			Map<String, Map<String, Long>> wordMap = LoadRank.wordMap;

			Map<String, Map<String, Double>> rankMapWthSumMap = LoadRank.rankMapWthSumMap;

			Map<String, Long> urlMapWithDocumentCount = wordMap.get(searchString);

			if (urlMapWithDocumentCount != null && urlMapWithDocumentCount.size() > 0) {
				
				  // Now get the URL from the Map and Itreate them.
				
				for(Map.Entry<String,Long> urlDocumentEntry: urlMapWithDocumentCount.entrySet()){
					
					PageData pageData = new PageData();
					
					     String url =  urlDocumentEntry.getKey();
					      
					     Map<String, Double> allWordInThisURL =  rankMapWthSumMap.get(url);					     
					     Double wordRank =   allWordInThisURL.get(searchString);					     
					     String documentData = LoadRank.metaDataMap.get(url);
					     
					     pageData.setUrl(url);
					     pageData.setPageDate(documentData);					     
					     treeMapWithResult.put(wordRank, pageData);
					
				}
				    

			}			
			
			
			modelMap.put("pagedata", getListFromMap(treeMapWithResult));
		}

		return "result";
	}

	@RequestMapping(value = "/result.html", method = RequestMethod.GET)
	public String search1(@RequestParam(required = false) String searchString,
			ModelMap modelMap) {

		System.out.println("Search Called GET " + searchString);

		modelMap.put("searchString", searchString);

		if (!StringUtils.isEmpty(searchString)) {

			// Get the document List in which word exist
			// Validate if word map counts is not null
			// Get the URL of word and search the word in Ranking Map
			// Get the word and their rank from Ranking Map
			// Create new map contains the rank with URL and page data.

			
			TreeMap<Double,PageData> treeMapWithResult = new TreeMap<Double,PageData>();
			
			Map<String, Map<String, Long>> wordMap = LoadRank.wordMap;

			Map<String, Map<String, Double>> rankMapWthSumMap = LoadRank.rankMapWthSumMap;

			Map<String, Long> urlMapWithDocumentCount = wordMap.get(searchString);

			if (urlMapWithDocumentCount != null && urlMapWithDocumentCount.size() > 0) {
				
				  // Now get the URL from the Map and Itreate them.
				
				for(Map.Entry<String,Long> urlDocumentEntry: urlMapWithDocumentCount.entrySet()){
					
					PageData pageData = new PageData();
					
					     String url =  urlDocumentEntry.getKey();
					      
					     Map<String, Double> allWordInThisURL =  rankMapWthSumMap.get(url);					     
					     Double wordRank =   allWordInThisURL.get(searchString);					     
					     String documentData = LoadRank.metaDataMap.get(url);
					     
					     pageData.setUrl(url);
					     pageData.setPageDate(documentData);					     
					     treeMapWithResult.put(wordRank, pageData);
					
				}
				    

			}			
			
			
			modelMap.put("pagedata", getListFromMap(treeMapWithResult));
		}

		return "result";
	}
	
	
	public ArrayList<PageData> getListFromMap(TreeMap treeMapWithResult){
		
		Map<Double,PageData> map = treeMapWithResult.descendingMap();
		
		ArrayList<PageData> list = new ArrayList<PageData>();
		
		for(Map.Entry<Double, PageData> entry: map.entrySet()){
			
			System.out.println(entry.getValue().url);
			
			list.add(entry.getValue());		
		}
		
		for(PageData pageData:list){
			System.out.println(pageData.url);

		}
		
		return list;
	}

	@RequestMapping(value = "/search.html", method = RequestMethod.GET)
	public String searchpage(@RequestParam(required = false) String searchString) {
		System.out.println("Search Called " + searchString);
		return "search";
	}

	@RequestMapping(value = "/autosearch.html", method = RequestMethod.GET)
	public String autosearch(
			@RequestParam(required = false) String searchString,
			HttpServletResponse response) throws JSONException, IOException {

		System.out.println("Search Called " + searchString);

		JSONArray jsonArray = new JSONArray();

		if (!StringUtils.isEmpty(searchString)) {

			for (String st : searchString(searchString)) {

				Map<String, String> json = new HashMap<String, String>();
				json.put("id", st);
				json.put("value", st);
				json.put("label", st);
				jsonArray.put(json);

			}
		}

		response.setContentType("application/json");
		jsonArray.write(response.getWriter());

		return null;
	}

	public Set<String> searchString(String searchString) {

		Set<String> searchStringSet = new HashSet<String>();

		searchString = searchString.toLowerCase();

		for (String st : LoadRank.wordsList) {

			st = st.toLowerCase();

			if (st.startsWith(searchString)) {
				searchStringSet.add(st);
			}

			if (searchStringSet != null && searchStringSet.size() == 6) {
				break;
			}
		}

		if (searchStringSet != null && searchStringSet.size() < 6) {

			for (String st : LoadRank.wordsList) {

				st = st.toLowerCase();

				if (st.contains(searchString)) {
					searchStringSet.add(st);
				}

				if (searchStringSet != null && searchStringSet.size() == 6) {
					break;
				}
			}

		}

		return new TreeSet(searchStringSet);
	}
	
	


	// Load Index, Ranking and metadata json file.
	// create map which has url and page data.
	// create index map
	// create Ranking map.
	// Sort the map base on ranking.
	// Search the data base on input show only 7 word
	// On click show the result on result page.
	// show number of page.
	//

	// Suppress Warning
	// Update PageRank parameters.
	//
}
