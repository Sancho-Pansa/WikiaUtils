package main;

import java.util.Scanner;

public class ChampionTranslator {
	
	private final String path = "resources/LeagueChamps.csv";
	
	private static final String SEPARATOR = ",";
	
	public ChampionTranslator() {
		
	}
	
	public String translate(String name) {
		String result = "Not found";
		String[] current;
		Scanner sc = new Scanner(this.getClass().getResourceAsStream(path));
		while (sc.hasNextLine()) {
			current = sc.nextLine().split(SEPARATOR);
			if (current[0].equals(name)) {
				result = current[1];
				break;
			} else if (current[1].equals(name)) {
				result = current[0];
				break;
			}
		}
		sc.close();

		return result;
	}
}
