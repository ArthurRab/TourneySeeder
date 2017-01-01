package me.ArthurRabinovich;

import org.json.JSONObject;

public class Participant {
	
	JSONObject jo;
	
	public static Participant[] fromJson(JSONObject[] j){
		Participant[] p = new Participant[j.length];
		
		for(int i=0;i<j.length;i++){
			p[i]=new Participant(j[i]);
		}
		return p;
	}
	
	public Participant(JSONObject jo){
		this.jo=jo;
	}
	
	public String getChallongeUsername() {
		return jo.getString("challonge_username");
	}

	public String getName() {
		return jo.getString("name");
	}

}
