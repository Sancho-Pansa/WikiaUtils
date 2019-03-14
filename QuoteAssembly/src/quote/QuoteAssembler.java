package quote;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import csv.CSVReader;

/**
 * This class performs assembly of Wiki page of champion voice-lines.
 * It processes .csv file with filenames, quotes and possible skins and 
 * makes a wiki-style text.
 * @author LemonadeCandy
 *
 */
public class QuoteAssembler {
	private List<List<String>> clientQuotes;
	private List<List<String>> startQuotes;
	private List<List<String>> attackQuotes;
	private List<List<String>> movementQuotes;
	private List<List<String>> jokes;
	private List<List<String>> taunts;
	private List<List<String>> abilityQuotes;
	private List<List<String>> laugh;
	private List<List<String>> death;
	private List<List<String>> recall;
	private List<List<String>> respawn;
	private List<List<String>> kills;
	private List<List<String>> otherQuotes;
	private List<List<String>> unsortedQuotes;
	
	private List<List<String>> table;
	
	public QuoteAssembler() {
		
	}
	
	public String generateWikiText(String csvPath) {
		CSVReader reader = new CSVReader();
		reader.setPath(csvPath);
		table = reader.readCSV();
		distributeQuotes();
		StringBuilder wikiText = new StringBuilder("");
		// Окно выбора чемпиона
		wikiText.append("== Окно выбора чемпиона ==\n");
		clientQuotes.forEach(x -> {
			if(x.get(x.size() - 1).equals("Выбор"))
			{
				wikiText.append(";Выбор\n")
				.append(wrapIn(x));
			} else if(x.get(x.size() - 1).equals("Бан"))
			{
				wikiText.append(";Бан\n")
				.append(wrapIn(x));
			}
		});
		
		// Начало игры
		if (!startQuotes.isEmpty()) {
			wikiText.append("\n== Начало матча ==\n");
			startQuotes.stream().map(this::wrapIn).forEach(wikiText::append);
		}
		// Атака
		wikiText.append("\n== Атака ==\n");
		attackQuotes.stream().map(this::wrapIn).forEach(wikiText::append);
		System.out.println(wikiText.toString());
		return wikiText.toString();
		//TODO: Завершить
	}
	
	private void distributeQuotes() {		
		clientQuotes = filterBySection("Окно выбора чемпиона");
		startQuotes = filterBySection("Начало игры");
		attackQuotes = filterBySection("Атака");
		movementQuotes = filterBySection("Передвижение");
		jokes = filterBySection("Шутки");
		taunts = filterBySection("Насмешки");
		abilityQuotes = filterBySection("Использование умений");
		laugh = filterBySection("Смех");
		death = filterBySection("Смерть");
		kills  = filterBySection("При убийствах");
		recall = filterBySection("Возвращение");
		respawn = filterBySection("При возрождении");
		otherQuotes = filterBySection("Другие фразы");
		unsortedQuotes = filterBySection("Несортированные");
	}
	
	private List<List<String>> filterBySection(String sectionName) {
		return table.stream()
				.filter(x -> {
					return x.get(x.size() - 2).equals(sectionName);
				})
				.collect(Collectors.toList());
	}
	
	private String wrapIn(List<String> line) {
		String stroke = "* ";
		String[] soundFiles = line.get(3).split(",");
		String[] skins = line.get(2).split(",");
		if(soundFiles.length == 1) {
			stroke += "{{фч|" + line.get(0) + soundFiles[0] + ".ogg|"
					+ line.get(1);
			if(line.get(line.size() - 2).equals("Смех") ||
					line.get(line.size() - 2).equals("Смерть") &&
					line.get(1).startsWith("Предсмертн"))
				stroke += "|sound = true";
			stroke += "}}\n";
		}
		else {
			if(skins.length < soundFiles.length)
				return "Ошибка: число образов меньше числа файлов\n";
			int last = soundFiles.length - 1;
			for(int i = 0; i < last; i++) {
				stroke += "{{фч|" + line.get(0) + soundFiles[i] + ".ogg}} ";
			}
			stroke += "{{фч|" + line.get(0) + soundFiles[last] + ".ogg|"
					+ line.get(1);
			if(line.get(line.size() - 2).equals("Смех") ||
					line.get(line.size() - 2).equals("Смерть") &&
					line.get(1).startsWith("Предсмертн"))
				stroke += "|sound = true";
			stroke += "}}\n";
		}
		return stroke;
	}
}
