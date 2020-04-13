import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpellingErrorCorrector {
	
	static HashMap<String, Integer> wordCounts = new HashMap< String, Integer>();
	static HashMap<String, Integer> bigramCounts = new HashMap< String, Integer>();
	static HashMap<String, Integer> unigramCounts = new HashMap< String, Integer>();
	static int totalWordCount = 0;
	
	static List<String> output = new ArrayList<String>();
	static List<String> outputWithSmoothing = new ArrayList<String>();
	
	
	static HashMap<Character, HashMap <Character, Integer>> deletionConfusionMatrix = new HashMap<Character, HashMap <Character, Integer>>(); 
	static HashMap<Character, HashMap <Character, Integer>> insertionConfusionMatrix = new HashMap<Character, HashMap <Character, Integer>>(); 
	static HashMap<Character, HashMap <Character, Integer>> replacementConfusionMatrix = new HashMap<Character, HashMap <Character, Integer>>(); 
	static HashMap<Character, HashMap <Character, Integer>> transpositionConfusionMatrix = new HashMap<Character, HashMap <Character, Integer>>(); 
	

	
	public static void main(String[] args) {
		
		
		FileRead.readCorpus();	// to create my dictionary
		
		FileRead.readSpellErrors(); // to create confusion matrices
		
		FileRead.readInputFile(args[0]);
		
		FileRead.printOutput();
		
		
		/*  If you wan to print confusion matrices, you can uncomment this block. 
		MatrixPrinter.printDeletionConfusionMatrix();
		MatrixPrinter.printInsertionConfusionMatrix();
		MatrixPrinter.printReplacementConfusionMatrix();
		MatrixPrinter.printTranspositionConfusionMatrix();
		*/
				
		
		//FileRead.readCorrectOutput(); I used this line to see the accuracy ratio with provided correct output file.
		
	}

}
