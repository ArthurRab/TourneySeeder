package me.ArthurRabinovich;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		JSONObject config = null;
		try {
			config = new JSONObject(urlToTxt.getTxtFromFile("config.txt"));
		} catch (JSONException | FileNotFoundException e3) {
			try {
				makeConfigFile();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		try {
			config = new JSONObject(urlToTxt.getTxtFromFile("config.txt"));
		} catch (JSONException | FileNotFoundException e3) {
			e3.printStackTrace();
			return;
		}

		GUI gui = new GUI(config);

		gui.setVisible(true);
	}

	public static void run(String url, String rankingsFileName, String bansFileName) {
		ArrayList<String> bannedNames = new ArrayList<String>();
		Scanner scan;
		try {
			scan = new Scanner(new File(bansFileName));
			while (scan.hasNext()) {
				bannedNames.add(scan.next());// CHANGED MAKE SURE IT WORKS IF
												// SHIT BRAKES DOWN
			}
			scan.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"'" + bansFileName + "' File Not Found\nBanned Players Will Not Be Detected", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
		ArrayList<String> bannedPlayersFound = new ArrayList<String>();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Participant> parts = new ArrayList<Participant>();

		// Get the list of participants from the challonge tournament site
		try {
			for (Participant par : Participant.fromJson(urlToTxt.getParticipantsFromUrl(
					"https://api.challonge.com/v1/tournaments/" + url + "/participants.json"))) {
				parts.add(par);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Tournament Not Found", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		// FIND AND REMEMBER BANNED PLAYERS AND REMOVE THEM FROM LIST
		ArrayList<Participant> PartsCpy = new ArrayList<Participant>(parts);
		for (String i : bannedNames) {
			for (Participant p : PartsCpy) {
				if (p.getChallongeUsername().equalsIgnoreCase(i)) {
					String temp = p.getChallongeUsername();
					if (!p.getName().equals("")) {
						temp += " (AKA: " + p.getName() + ")";
					}
					bannedPlayersFound.add(temp);
					parts.remove(p);
					break;
				}
			}
		}

		try {

			BufferedReader br = new BufferedReader(new FileReader(new File("./" + rankingsFileName)));
			String line = br.readLine();
			int namesPos = -1;

			String[] tokens = line.split(",");
			for (int i = 0; i < tokens.length; i++) {
				if (tokens[i].equalsIgnoreCase("\"names\"") || tokens[i].equalsIgnoreCase("\"name\"")) {
					namesPos = i;
					break;
				}
			}

			while ((line = br.readLine()) != null) {
				String name = line.split(",")[namesPos];
				name = name.substring(1, name.length() - 1);
				PartsCpy = new ArrayList<Participant>(parts);
				for (Participant p : PartsCpy) {
					if (p.getName().equalsIgnoreCase(name) || p.getChallongeUsername().equalsIgnoreCase(name)) {
						if (!p.getName().equals("")) {
							names.add(p.getName());
						} else {
							names.add(p.getChallongeUsername());
						}
						parts.remove(p);
					}
				}
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Rankings File Not Found\nNames will be in order of registration",
					"Error", JOptionPane.ERROR_MESSAGE);

		}

		for (Participant p : parts) {
			if (!p.getName().equals("")) {
				names.add(p.getName());
			} else {
				names.add(p.getChallongeUsername());
			}
		}
		// Write the player list to a file called SeededList.txt
		try {
			PrintWriter writer = new PrintWriter("SeededList.txt", "UTF-8");
			for (String i : names) {
				writer.println(i);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// Make a pop-up window that displays banned players, if there were any
		// that entered
		if (bannedPlayersFound.size() > 0) {
			String players = "";
			for (String i : bannedPlayersFound) {
				players += i + "\n";
			}
			players = players.substring(0, players.length() - 1);
			JOptionPane.showMessageDialog(null,
					"The Following Banned Players Have Attempted Entering This Tournament: \n" + players, "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
		// Say that you finished
		JOptionPane.showMessageDialog(null, "Done!", "Done!", JOptionPane.INFORMATION_MESSAGE);
	}

	private static void makeConfigFile() throws FileNotFoundException {
		JSONObject j = new JSONObject();

		String[] x = { "NationalChampionshipSeries", "EasternChampionshipSeries", "CentralChampionshipSeries",
				"WesternChampionshipSeries" };
		
		String[] subdomains = {"narivals",""};
		
		j.put("Tournament_Names", x);
		j.put("Subdomains", subdomains);
		j.put("Rankings_File", "seed.txt");
		j.put("Bans_File", "bans.txt");
		PrintWriter pw = new PrintWriter("config.txt");

		pw.write(j.toString(1));
		pw.close();
	}

}
