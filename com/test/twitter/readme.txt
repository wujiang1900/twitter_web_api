This program will retrieve all tweets talking about a celebrity(e.g.: Tom Cruise) from a given location using Twitter APIs and then find the top 10 ten tweets (based on favorite count):
	Input:
	Celebrity Name (String) eg: Tom Cruise
	Location: String (Name)
	Output: List of tweet texts
	API to use:
	https://dev.twitter.com/docs/api/1.1

To run this program in Eclipse:

1. Import the project to your Eclipse.
	First save twitter.zip file to your pc.
	Then in Eclipse, go to File - Import, choose General - Existing project into..., then select archive file, browse where you saved the zip file, then ok.
	You might need to Resolve some error if your jdk version and path is different from mine.
	You will need java 8 and above because I used java 8 lamda expression feature.

2. Then you can run my app from TweetsSearch.java. You will need [consumer key] [consumer secret] (of your twitter account) in the command line arguement to run this app. If you don't have one, you can use mine.

3. After the program starts running, you'll need to follow the instruction, copy & paste "app authorize" URL in a browser, get the PIN from that webiste and input it to the program console.

If you have any questions, please email me: wujiang1900@yahoo.com.

Enjoy!
