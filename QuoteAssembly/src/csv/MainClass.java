package csv;

import java.util.List;

import quote.QuoteAssembler;

public class MainClass {

	public static void main(String[] args) {
		String path = "C:\\Users\\LemonadeCandy\\OneDrive\\Wikia\\League of Legends voices\\Soraka\\Lines.csv";
		QuoteAssembler qa = new QuoteAssembler();
		qa.generateWikiText(path);
	}

}
