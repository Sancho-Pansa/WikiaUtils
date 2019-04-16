package main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReplaceName {

	private List<String> engNames;
	
	public ReplaceName() {
		Path namesPath = Paths.get("src\\main\\resources", "LeagueChamps.csv");
		try(Stream<String> lineStream = Files.lines(namesPath))
		{
			engNames = lineStream.map((String x) -> {
				String[] line = x.split(",");
				return line[1];
			}).sorted().collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void performTransition(String docPath) {
		Path path = Paths.get(docPath);
		Path newPath = Paths.get(path.getParent().toString(), "Translated.txt");
		ChampionTranslator ct = new ChampionTranslator();
		try(Stream<String> lineStream = Files.lines(path))
		{
			List<String> lines;
			lines = lineStream.map((String x)-> {
				System.out.println(x);
				for(int i = 0; i < engNames.size(); i++)
					x = x.replaceFirst(engNames.get(i), ct.translate(engNames.get(i)));
				System.out.println(x);
				return x;
			})
			.collect(Collectors.toList());
			Files.write(newPath, lines, StandardCharsets.UTF_8);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
