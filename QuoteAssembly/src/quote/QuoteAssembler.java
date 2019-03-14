package quote;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		// ���� ������ ��������
		wikiText.append("== ���� ������ �������� ==\n");
		clientQuotes.forEach(x -> {
			if(x.get(x.size() - 1).equals("�����"))
			{
				wikiText.append(";�����\n")
				.append(wrapIn(x));
			} else if(x.get(x.size() - 1).equals("���"))
			{
				wikiText.append(";���\n")
				.append(wrapIn(x));
			}
		});
		
		// ������ ����
		appendSection(wikiText, startQuotes, "������ ����");
		
		// �����
		appendSection(wikiText, attackQuotes, "�����");
		
		// ������
		appendSection(wikiText, abilityQuotes, "������������� ������");
		
		// ������������
		appendSection(wikiText, movementQuotes, "������������");
		
		// �����
		appendSection(wikiText, jokes, "�����");
		
		// ��������
		appendSection(wikiText, taunts, "��������");
		
		// ��������
		appendSection(wikiText, kills, "��������");
		
		// ��� �����������
		appendSection(wikiText, recall, "��� {{si|�����������|�����������}}");
		
		// ������ �����
		appendSection(wikiText, otherQuotes, "������ �����");
		
		// ����
		appendSection(wikiText, laugh, "����");
		
		// ������
		appendSection(wikiText, death, "������");
		
		// ���������������
		appendSection(wikiText, unsortedQuotes, "���������������");
		
		return wikiText.toString();
	}
	
	private void distributeQuotes() {		
		clientQuotes = filterBySection("���� ������ ��������");
		startQuotes = filterBySection("������ ����");
		attackQuotes = filterBySection("�����");
		movementQuotes = filterBySection("������������");
		jokes = filterBySection("�����");
		taunts = filterBySection("��������");
		abilityQuotes = filterBySection("������������� ������");
		laugh = filterBySection("����");
		death = filterBySection("������");
		kills  = filterBySection("��� ���������");
		recall = filterBySection("�����������");
		respawn = filterBySection("��� �����������");
		otherQuotes = filterBySection("������ �����");
		unsortedQuotes = filterBySection("���������������");
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
			stroke += "{{��|" + line.get(0) + soundFiles[0] + ".ogg|"
					+ line.get(1);
			if(line.get(line.size() - 2).equals("����") ||
					line.get(line.size() - 2).equals("������") &&
					line.get(1).startsWith("����������"))
				stroke += "|sound = true";
			stroke += "}}\n";
		}
		else {
			if(skins.length < soundFiles.length)
				return "������: ����� ������� ������ ����� ������\n";
			int last = soundFiles.length - 1;
			for(int i = 0; i < last; i++) {
				if(i > 0) {
					stroke += "{{��|" + line.get(0) + soundFiles[i] + ".ogg" +
							"||" + line.get(0) + "|" + skins[i] + "}} ";
				} else
					stroke += "{{��|" + line.get(0) + soundFiles[i] + ".ogg||"
							+ line.get(0) + "|" + skins[i] + "}} ";
			}
			stroke += "{{��|" + line.get(0) + soundFiles[last] + ".ogg|"
					+ line.get(1);
			if(last != 0) {
				stroke += "|" + line.get(0) + "|" + skins[last];
			}
			
			if(line.get(line.size() - 2).equals("����") ||
					line.get(line.size() - 2).equals("������") &&
					line.get(1).startsWith("����������"))
				stroke += "|sound = true";
			stroke += "}}\n";
		}
		return stroke;
	}
	
	private void appendSection(StringBuilder text, List<List<String>> section, String sectionName) {
		if (!section.isEmpty()) {
			text.append("\n== " + sectionName + " ==\n");
			section.stream()
				.filter(x -> x.get(x.size() - 1).equals("���"))
				.map(this::wrapIn)
				.forEach(text::append);
			
			Set<String> subSections = section.stream()
				.map(x -> x.get(x.size() - 1))
				.filter(x -> !x.equals("���"))
				.distinct()
				.collect(Collectors.toSet());
			
			if(subSections.size() > 0) {
				subSections.forEach(x -> {
					text.append(";")
					.append(x + "\n");
					section.stream()
						.filter(y -> { return y.get(y.size() - 1).equals(x); })
						.map(this::wrapIn)
						.forEach(text::append);
				});
			}
		}
	}
}
