package csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
	private final char DELIMETER = ';';
	private final char QUOTE = '"';
	
	private Path filepath;
	
	public CSVReader() {
		
	}
	
	public void setPath(String path) {
		filepath = Paths.get(path);
	}
	
	public List<List<String>> readCSV(int cols) {
		List<List<String>> table = new ArrayList<>();
		try(BufferedReader bf = Files.newBufferedReader(filepath, Charset.forName("UTF-8"))) {
			String line = bf.readLine(); // skip header
			while((line = bf.readLine()) != null)
			{
				table.add(parseLine(line));
				
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println(table.get(1));
		return table;
	}
	
	private List<String> parseLine(String line) {
		List<String> result = new ArrayList<>();
		if(line.isEmpty())
			return result;
		
		StringBuffer buf = new StringBuffer();
		char[] charLine = line.toCharArray();
		
		boolean inQuotes = false;
		boolean doubleQuotes = false;
		boolean permitCollection = false;
		
		for(char ch: charLine) {
			if(inQuotes) {
				permitCollection = true;
				if(ch == QUOTE) {
					if(doubleQuotes) {
						buf.append('"');
						doubleQuotes = false;
					} else {
						inQuotes = false;
					}
				} else 
					buf.append(ch);
			} else
			{
				if(ch == QUOTE) {
					inQuotes = true;
					
					if(permitCollection)
						buf.append('"');
				} else if(ch == DELIMETER) {
					result.add(buf.toString());
					buf = new StringBuffer();
					permitCollection = false;
				} else if(ch == '\r')
					continue;
				else if(ch == '\n')
					break;
				else
					buf.append(ch);
			}
		}
		
		return result;
	}
}
