package me.ArthurRabinovich;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class urlToTxt {
	
	private static void auth(){
		Authenticator.setDefault(new Authenticator(){
			@Override
		    protected PasswordAuthentication getPasswordAuthentication() {          
		        return new PasswordAuthentication("ForApiUsage", "pnLdKyjSu5J2wnUXdRJeD6COGM6yi4Fd7mqKN8mQ".toCharArray());
		    }
		});
	}
	
	public static String getTxtFromFile(String name) throws FileNotFoundException{
		Scanner scan = new Scanner(new File(name));
		String temp="";
		
		while(scan.hasNextLine()){
			temp+=scan.nextLine()+"\n";
		}
		
		scan.close();
		return temp;
	}
	
	public static String getTxtFromUrl(String url) throws IOException{
		auth();
		
		URL u = new URL(url);
		String output="";
		BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
		String line;
		while((line=br.readLine())!=null){
			output+=line+"\n";
		}
		return output;
	}
	
	public static JSONObject getJsonFromUrl(String url) throws JSONException, IOException{
		JSONObject j = new JSONObject(new JSONArray(getTxtFromUrl(url)));
		return j;
	}
	
	public static JSONObject[] getParticipantsFromUrl(String url) throws JSONException, IOException{
		JSONObject[] p;
		
		JSONArray ja = new JSONArray(getTxtFromUrl(url));
		
		p = new JSONObject[ja.length()];
		
		for(int i=0;i<ja.length();i++){
			p[i]=(JSONObject)((JSONObject)ja.get(i)).get("participant");
		}
		
		return p;
	}
}
