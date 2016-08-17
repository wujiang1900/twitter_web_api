/**
 * 
 */
package com.test.twitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * @author john
 * 
 * Singleton implementation using enum.
 * Reusable in multi-threading scenario.
 * 
 * The user AccessToken should actually be persisted to persistence store,
 * instead of using in-memory hashMap as implemented here.
 *
 */

enum TwitterSingleton {
	INSTANCE;
	
	/*******************************************************
	 * 
	 *   Lazy initiation of our TwitterSingleton object.
	 *   We initialize a twitter4j.Twitter object here 
	 *   so that we don't need to initialize it multiple 
	 *   times in case its initialization is expensive.
	 *   
	 */
	TwitterSingleton(){
		store = new HashMap<Long, AccessToken>();
		// The factory instance is re-useable and thread safe.
	    twitter = TwitterFactory.getSingleton();	   
	}
	public Twitter twitter;
	private Map<Long, AccessToken> store;
	public void init(String key, String secret) throws TwitterException {
	    twitter.setOAuthConsumer(key, secret);
	    RequestToken requestToken = twitter.getOAuthRequestToken();
	    AccessToken accessToken = null;
	    Scanner in = new Scanner(System.in);
	    while (null == accessToken) {
	      System.out.println("Please open the following URL from a browser and \"Authorize app\" access to your account:");
	      System.out.println(requestToken.getAuthorizationURL());
	      System.out.print("Enter the PIN that was displayed in the browser after the access is granted: ");
	      String pin = in.next();
	      try{
	         if(pin.length() > 0){
	           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
	         }else{
	           accessToken = twitter.getOAuthAccessToken();
	         }
	      } catch (TwitterException te) {
	        if(401 == te.getStatusCode()){
	          System.out.println("Unable to get the access token.");
	        }else{
	          te.printStackTrace();
	        }
	      }
	    }
	  //persist to the accessToken for future reference.
	    storeAccessToken(twitter.verifyCredentials().getId() , accessToken);	   	
	}
	 
	  private  void storeAccessToken(long useId, AccessToken accessToken) throws TwitterException{
	    //store accessToken.getToken()
	    //store accessToken.getTokenSecret()
	    // should actually store the token to persistence store
	    store.put(twitter.verifyCredentials().getId() , accessToken);	
	  }
	  
	public AccessToken loadAccessToken(long user) {
		//should actually load AccessToken from persistence store
		return store.get(user);
	}
}

