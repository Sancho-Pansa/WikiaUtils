package csv;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import quote.QuoteAssembler;

public class MainClass {

	public static void main(String[] args) {
		String path = "C:\\Users\\Вадим\\OneDrive\\Wikia\\League of Legends voices\\Soraka\\Lines.csv";
		QuoteAssembler qa = new QuoteAssembler();
		String result = qa.generateWikiText(path);
		Path output = Paths.get("\\result.txt");
		System.out.println(output.toAbsolutePath().toString());
		
		try(OutputStream os = Files.newOutputStream(output, StandardOpenOption.CREATE)) {
			os.write(result.getBytes());
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
