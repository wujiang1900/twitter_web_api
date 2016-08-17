/**
 * 
 */
package com.test.twitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.Query.Unit;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * @author john Wu
 * 
 * Write an Program to all tweets talking about a celebrity(e.g.: Tom Cruise) from a given location using Twitter APIs and then find the 10 ten tweets (based on tweet count) using the best data structure.
	Input:
	Celebrity Name (String) eg: Tom Cruise
	Location: String (Name)
	Output: List of tweet texts
	API to use:
	https://dev.twitter.com/docs/api/1.1
	The code should run, be correct, efficient and be readable
	Twitter API will return all the tweets for that celebrity and location.. You need to use the proper logic and data structure to output the top ten tweets..
 *
 *
 *
 */
public class CopyOfTweetsSearch {

	private static final int TOP = 10;
	private static final int MAX_PLACE = 5;
	private static final double RADIUS = 25;
	private static final Unit UNIT = Unit.mi;
	/**
	 * @param args
	 * @throws TwitterException 
	 * @throws IOException 
	 */

	private static long sum=0;
	private static int count=0;
	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			System.out.println("USAGE: java TweetsSearch [consumer key] [consumer secret]");
			System.exit(-1);
		}
		TwitterSingleton search = getTwitter(args[0], args[1]);
		prompt();
				
		Scanner in = new Scanner(System.in);
		String again = "y";
		while("y".equalsIgnoreCase(again)||"yes".equalsIgnoreCase(again)) {
			System.out.print("About whom would you like to search?: ");
			String name = in.nextLine();
//			String place = null;

			Twitter twitter = search.twitter;
			try {
	        	Set<Status> results = doSearch(twitter, in, name);
	        	
	        	List<Status> tweets = new ArrayList(results);
	        	Comparator<Status> favoriteCountComparator = 
	        	new Comparator<Status>(){
	        		@Override
	    			public int compare(Status o1, Status o2) {
	    				return o1.getFavoriteCount() - o2.getFavoriteCount();
	    			}
	        	};
	        	tweets.sort(favoriteCountComparator.reversed());
	        	resultsHeading(name);
	        	for(int i = 0; i<Math.min(tweets.size(), TOP); i++) {
	        		System.out.println(tweets.get(i).getText());
	        	}
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to search tweets: " + te.getMessage());
	            System.exit(-1);
	        }

			System.out.println("**************************************************************************************");
			System.out.println("Would you like another search?[y or n]: ");
			again = in.nextLine();
			count++;
		}
    	System.out.println("Average time to retrieve all tweets (SINGLE THREAD) is "+(float)(sum/count)/1000 + " seconds.");
		
	    in.close();
	    System.exit(0);
	  }
	
	private static Set<Status> doSearch(Twitter twitter, Scanner in, String name) throws TwitterException {
		Set<Status> results = new HashSet();
		ResponseList<Place> places = getPlace(in, twitter);
    	long start = new Date().getTime();
    	for(Place p: places) {
    		 GeoLocation[][] locs = p.getBoundingBoxCoordinates();
    		 Query query = new Query(name);
    		 for(int i = 0; i<locs.length; i++) {
    			 for(int j = 0; j<locs[i].length; j++) {
    				query.setGeoCode(locs[i][j], RADIUS, UNIT);
        		    QueryResult result = twitter.search(query);
        		    results.addAll(result.getTweets());
        		 }
    		 }
    	}
    	long end = new Date().getTime();
    	sum += (end-start);
    	return results;
	}

	private static ResponseList<Place> getPlace(Scanner in, Twitter twitter) throws TwitterException {
		String place;
    	ResponseList<Place> places;
    	while (true) {
			System.out.print("Which location would you like to search?: ");
			place = in.nextLine();
        	String dummy = "0.0.0.0";
        	GeoQuery query = new GeoQuery(dummy);
        	query.setQuery(place);
        	query.setMaxResults(MAX_PLACE);
        	places = twitter.searchPlaces(query);
        	if(places.size()>1) break;
        	System.out.println("Can't find your place. Please re-enter! ");
        }
        return places;
	}

	private static void resultsHeading(String name) {
		System.out.println("**************************************************************************************");
		System.out.println("*                                                                                    *");
		System.out.println("* The followings are the top "+TOP+" tweets (based on favorite_count)  about "+name+"  *");
		System.out.println("*                                                                                    *");
		System.out.println("**************************************************************************************"+"\n");
	}

	private static void prompt() {
		System.out.println("**************************************************************************************");
		System.out.println("*                                                                                    *");
		System.out.println("* This program can search all tweets talking about a celebrity from a given location *");
		System.out.println("* and present you top "+TOP+" tweets (based on favorite_count)                       *");
		System.out.println("*                                                                                    *");
		System.out.println("**************************************************************************************");
	}

	private static TwitterSingleton getTwitter(String key, String secret) {
		TwitterSingleton twitter = TwitterSingleton.INSTANCE;
		try {
			twitter.init(key, secret);
		} catch (TwitterException e) {
			e.printStackTrace();
            System.out.println("Failure occurred during OAuth initialization: " + e.getMessage());
            System.exit(-1);
		}		
		return twitter;
	}
}
