import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class SpellingErrorCorrector {
	
	static HashMap<String, Integer> wordCounts = new HashMap< String, Integer>();
	static HashMap<String, Integer> bigramCounts = new HashMap< String, Integer>();
	static HashMap<String, Integer> unigramCounts = new HashMap< String, Integer>();
	static int totalWordCount = 0;
	
	static List<String> output = new ArrayList<String>();
	
	
	static HashMap<Character, HashMap <Character, Integer>> deletionConfusionMatrix = new HashMap<Character, HashMap <Character, Integer>>(); 
	static HashMap<Character, HashMap <Character, Integer>> insertionConfusionMatrix = new HashMap<Character, HashMap <Character, Integer>>(); 
	static HashMap<Character, HashMap <Character, Integer>> replacementConfusionMatrix = new HashMap<Character, HashMap <Character, Integer>>(); 
	static HashMap<Character, HashMap <Character, Integer>> transpositionConfusionMatrix = new HashMap<Character, HashMap <Character, Integer>>(); 
	
	//static int[][] deletionConfusionMatrix, insertionConfusionMatrix, replacementConfusionMatrix, transpositionConfusionMatrix;
	
	public static void main(String[] args) {
		
		Consumer<Entry<String, Integer> > printKeyValue = entry -> System.out.println(entry.getKey() + ": " + entry.getValue());
		
		FileRead.readCorpus();			
		
		
		FileRead.readSpellErrors();
		
		
		FileRead.readInputFile();	
		
		FileRead.readCorrectOutput();
		
		/*
		for(String prediction : output) {
			System.out.println(prediction);
		}
		*/
		
		
		//wordCounts.entrySet().stream().forEach(printKeyValue);
	}

}
