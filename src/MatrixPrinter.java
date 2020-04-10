import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class MatrixPrinter {
	
	static Consumer<Entry<Character, Integer> > printColumnTitles = entry -> {
		if(entry.getKey() == ' ') {
			System.out.print("\tempty");
		}else System.out.print("\t" + entry.getKey());
	};
	static Consumer<Entry<Character, Integer> > printColumnValues = entry -> System.out.print(entry.getValue() + "\t");
	
	static Consumer<Entry<Character, HashMap <Character, Integer>> > printConfusionMatrix 
		= entry -> {
			System.out.println();
			if(entry.getKey() == ' ') {
				System.out.print("empty:\t");
			}else System.out.print(entry.getKey() + ":\t");
			
			entry.getValue().entrySet().stream().forEach(printColumnValues);
			
		};
		
	static void printInsertionConfusionMatrix() {
		for(int i = 0; i < 26; i++) {
			System.out.print("\t" + (char)(i + 97));
			
		}
		System.out.println();
		
			
			Character rowChar = ' ';
			if(SpellingErrorCorrector.insertionConfusionMatrix.containsKey(rowChar)) {
				System.out.println();
				System.out.print("Empty:");
				for(int j = 0; j < 26; j++) {
					
					Character colChar = (char)(j + 97);
					
					if(SpellingErrorCorrector.insertionConfusionMatrix.get(rowChar).containsKey(colChar)) {
						System.out.print("\t" + SpellingErrorCorrector.insertionConfusionMatrix.get(rowChar).get(colChar));
					}else {
						System.out.print("\t0");
					}
				}
				System.out.println();
			}
		
		
	}
	
	static void printDeletionConfusionMatrix() {
		SpellingErrorCorrector.deletionConfusionMatrix.get('a').entrySet().stream().forEach(printColumnTitles);
		SpellingErrorCorrector.deletionConfusionMatrix.entrySet().stream().forEach(printConfusionMatrix);
	}
	
	static void printReplacementConfusionMatrix() {
		
		for(int i = 0; i < 26; i++) {
			System.out.print("\t" + (char)(i + 97));
			
		}
		System.out.println();
		for(int i = 0; i < 26; i++) {
			
			Character rowChar = (char)(i+97);
			if(SpellingErrorCorrector.replacementConfusionMatrix.containsKey(rowChar)) {
				System.out.println();
				System.out.print(rowChar);
				for(int j = 0; j < 26; j++) {
					
					Character colChar = (char)(j + 97);
					
					if(SpellingErrorCorrector.replacementConfusionMatrix.get(rowChar).containsKey(colChar)) {
						System.out.print("\t" + SpellingErrorCorrector.replacementConfusionMatrix.get(rowChar).get(colChar));
					}else {
						System.out.print("\t0");
					}
				}
				System.out.println();
			}
		}
	}
	static void printTranspositionConfusionMatrix() {
		
		for(int i = 0; i < 26; i++) {
			System.out.print("\t" + (char)(i + 97));
			
		}
		System.out.println();
		for(int i = 0; i < 26; i++) {
			
			Character rowChar = (char)(i+97);
			if(SpellingErrorCorrector.transpositionConfusionMatrix.containsKey(rowChar)) {
				System.out.println();
				System.out.print(rowChar);
				for(int j = 0; j < 26; j++) {
					
					Character colChar = (char)(j + 97);
					
					if(SpellingErrorCorrector.transpositionConfusionMatrix.get(rowChar).containsKey(colChar)) {
						System.out.print("\t" + SpellingErrorCorrector.transpositionConfusionMatrix.get(rowChar).get(colChar));
					}else {
						System.out.print("\t0");
					}
				}
				System.out.println();
			}
		}
	}
	

}
