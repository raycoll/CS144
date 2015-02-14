package edu.ucla.cs.cs144;

import java.util.Calendar;
import java.util.Date;

import edu.ucla.cs.cs144.AuctionSearch;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearchTest {
	public static void main(String[] args1)
	{
		AuctionSearch as = new AuctionSearch();
		
		String message = "Starting Basic Search Test!";
		String reply = as.echo(message);
		System.out.println("Reply: " + reply);
		
		String[] queries = {"superman","kitchenware","star trek"};
		for (String query : queries) {	
		SearchResult[] basicResults = as.basicSearch(query, 0, 1500);
		System.out.println("Basic Search Query: " + query);
		System.out.println("Received " + basicResults.length + " results");

	
		}

	/*for(SearchResult result : basicResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		} */
		
		SearchRegion region =
		    new SearchRegion(33.774, -118.63, 34.201, -117.38); 
		SearchResult[] spatialResults = as.spatialSearch("camera", region, 0, 20);
		System.out.println("Spatial Seacrh");
		System.out.println("Received " + spatialResults.length + " results");
		for(SearchResult result : spatialResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}

		SearchResult[] spatialResults2 = as.spatialSearch("superman", region, 0, 200);
		System.out.println("Spatial Seacrh");
		System.out.println("Received " + spatialResults2.length + " results");
		for(SearchResult result2 : spatialResults2) {
			System.out.println(result2.getItemId() + ": " + result2.getName());
		}
		
		//No bid, no description
		String itemId = "1497595357";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		//Multiple bids
		String itemId2 = "1045776745";
		String item2 = as.getXMLDataForItemId(itemId2);
		System.out.println("XML data for ItemId: " + itemId2);
		System.out.println(item2);

		//No longitude,latitude
		String itemId3 = "1679455238";
		String item3 = as.getXMLDataForItemId(itemId3);
		System.out.println("XML data for ItemId: " + itemId3);
		System.out.println(item3);

		//Has buyprice
		String itemId4 = "1679391688";
		String item4= as.getXMLDataForItemId(itemId4);
		System.out.println("XML data for ItemId: " + itemId4);
		System.out.println(item4);


		// Add your own test here
	}
}
