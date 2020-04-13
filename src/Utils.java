import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class Utils {
	
	static void computeBigramCounts(String word) {
		
		if(word.length() > 0) {
			String bigram, unigram;
			
			
			for(int i = 0; i < word.length(); i++) {
				
				unigram = "" + word.charAt(i);
				if(SpellingErrorCorrector.unigramCounts.containsKey(unigram)) {
					int count;
					count = SpellingErrorCorrector.unigramCounts.get(unigram);
					count ++;
					SpellingErrorCorrector.unigramCounts.replace(unigram, count);
				}else {
					SpellingErrorCorrector.unigramCounts.put(unigram, 1);
				}
				
				if(i == 0) continue;
				
				bigram = "" + word.charAt(i-1) + word.charAt(i);
				if(SpellingErrorCorrector.bigramCounts.containsKey(bigram)) {
					int count;
					count = SpellingErrorCorrector.bigramCounts.get(bigram);
					count ++;
					SpellingErrorCorrector.bigramCounts.replace(bigram, count);
				}else {
					SpellingErrorCorrector.bigramCounts.put(bigram, 1);
				}
			}
		}
		
	}
	
	static void extractWords(String line) {
		
	    StringTokenizer st = new StringTokenizer(line);
	     
	    while (st.hasMoreTokens()) {
	    	String word = st.nextToken();
	    	String pureWord = "";
	    	for ( int i = 0; i< word.length(); i++) {
	    		boolean isAlphabetic = word.charAt(i) >= 'a' && word.charAt(i) <= 'z';
	    		if( isAlphabetic || word.charAt(i) == '-') {
	    			pureWord += word.charAt(i);
	    		}
	    	}
	    	computeBigramCounts(pureWord);
	        int count; 
	        
	        if ( SpellingErrorCorrector.wordCounts.containsKey(pureWord) ) {
	        	count = SpellingErrorCorrector.wordCounts.get(pureWord) + 1 ;
	        	SpellingErrorCorrector.wordCounts.replace(pureWord, count);
	        	
	        }else {
	        	SpellingErrorCorrector.wordCounts.put(pureWord, 1);
	        }
	        SpellingErrorCorrector.totalWordCount++;
	        
	     }
		
	}
	static void updateDeletionConfusionMatrix(String correctWord, String wrongWord, int frequency) {
		for (int i = 0; i < correctWord.length(); i++) {
			if ( i == wrongWord.length() || correctWord.charAt(i) != wrongWord.charAt(i) ) {
				
				boolean rowExists = i > 0 ? 
						SpellingErrorCorrector.deletionConfusionMatrix.containsKey(correctWord.charAt(i-1))
						:
						SpellingErrorCorrector.deletionConfusionMatrix.containsKey(' ');
				
				if (rowExists) {
					
					HashMap<Character, Integer> row;
					if(i > 0) {
						row = SpellingErrorCorrector.deletionConfusionMatrix.get(correctWord.charAt(i-1));
					}else {
						row = SpellingErrorCorrector.deletionConfusionMatrix.get(' ');
					}
					
					if(row.containsKey(correctWord.charAt(i))) {
						int value = row.get(correctWord.charAt(i));
						value += frequency;
						row.replace(correctWord.charAt(i), value);
					}else {
						
						row.put(correctWord.charAt(i), frequency);
					}
					
					if(i > 0) {
						SpellingErrorCorrector.deletionConfusionMatrix.replace(correctWord.charAt(i-1), row);
					}else {
						SpellingErrorCorrector.deletionConfusionMatrix.replace(' ', row);
						
					}	
					
				}else {
					HashMap<Character, Integer> row = new HashMap<Character, Integer>();
					
					row.put(correctWord.charAt(i), frequency);
					
					if(i > 0) {
						SpellingErrorCorrector.deletionConfusionMatrix.put(correctWord.charAt(i-1), row);
					}else {
						SpellingErrorCorrector.deletionConfusionMatrix.put(' ', row);
					}
					
				}
				break;
			}
				
		}
		
	}
	static void updateInsertionConfusionMatrix(String correctWord, String wrongWord, int frequency) {
		
		for (int i = 0; i<wrongWord.length(); i++) {
			
			if ( i == correctWord.length() || correctWord.charAt(i) != wrongWord.charAt(i) ) {
				
				boolean rowExists = i > 0 ? 
						SpellingErrorCorrector.insertionConfusionMatrix.containsKey(correctWord.charAt(i-1))
						:
						SpellingErrorCorrector.insertionConfusionMatrix.containsKey(' ');
						
				if (rowExists) {
					
					HashMap<Character, Integer> row; 
					
					if(i > 0) {
						row = SpellingErrorCorrector.insertionConfusionMatrix.get(correctWord.charAt(i-1));
					}else {
						row = SpellingErrorCorrector.insertionConfusionMatrix.get(' ');
					}
					
					if(row.containsKey(wrongWord.charAt(i))) {
						int value = row.get(wrongWord.charAt(i));
						value += frequency;
						row.replace(wrongWord.charAt(i), value);
					}else {
						row.put(wrongWord.charAt(i), frequency);
					}
					
					if(i > 0) {
						SpellingErrorCorrector.insertionConfusionMatrix.replace(correctWord.charAt(i-1) , row);
					}else {
						SpellingErrorCorrector.insertionConfusionMatrix.replace(' ' , row);
					}
					
					
				}else {
					HashMap<Character, Integer> row = new HashMap<Character, Integer>();
					
					row.put(wrongWord.charAt(i), frequency);
					
					if(i > 0) {
						SpellingErrorCorrector.insertionConfusionMatrix.put(correctWord.charAt(i-1), row);
					}else {
						SpellingErrorCorrector.insertionConfusionMatrix.put(' ' , row);
					}
					
				}
				break;
			}
				
		}
		
	}
	static void updateReplacementConfusionMatrix(String correctWord, String wrongWord, int frequency) {
		for (int i = 0; i<wrongWord.length(); i++) {
			if (  correctWord.charAt(i) != wrongWord.charAt(i) ) {
				if (SpellingErrorCorrector.replacementConfusionMatrix.containsKey(correctWord.charAt(i))) {
					
					HashMap<Character, Integer> row = SpellingErrorCorrector.replacementConfusionMatrix.get(correctWord.charAt(i));
					//
					if(row.containsKey(wrongWord.charAt(i))) {
						int value = row.get(wrongWord.charAt(i));
						value += frequency;
						row.replace(wrongWord.charAt(i), value);
					}else {
						
						row.put(wrongWord.charAt(i), frequency);
					}
					
					SpellingErrorCorrector.replacementConfusionMatrix.replace(correctWord.charAt(i) , row);
					
					
				}else {
					HashMap<Character, Integer> row = new HashMap<Character, Integer>();
					
					row.put(wrongWord.charAt(i), frequency);
					
					SpellingErrorCorrector.replacementConfusionMatrix.put(correctWord.charAt(i) , row);
				}
				break;
			}
				
		}
		
	}
	static void updateTranspositionConfusionMatrix(String correctWord, String wrongWord, int frequency) {
		for (int i = 0; i<wrongWord.length(); i++) {
			if (  correctWord.charAt(i) != wrongWord.charAt(i) ) {
				if (SpellingErrorCorrector.transpositionConfusionMatrix.containsKey(correctWord.charAt(i))) {
					
					HashMap<Character, Integer> row = SpellingErrorCorrector.transpositionConfusionMatrix.get(correctWord.charAt(i));
					//
					if(row.containsKey(wrongWord.charAt(i))) {
						int value = row.get(wrongWord.charAt(i));
						value += frequency;
						row.replace(wrongWord.charAt(i), value);
					}else {
						
						row.put(wrongWord.charAt(i), frequency);
					}
					
					SpellingErrorCorrector.transpositionConfusionMatrix.replace(correctWord.charAt(i) , row);
					
					
				
				}else {
					HashMap<Character, Integer> row = new HashMap<Character, Integer>();
					
					row.put(wrongWord.charAt(i), frequency);
					
					SpellingErrorCorrector.transpositionConfusionMatrix.put(correctWord.charAt(i) , row);
				}
				break;
			}
		}
	}
	static String[] generateEdits1(String word){
        
		Set<String> edits1Set = new HashSet<String>();
		
		String letters = "abcdefghijklmnopqrstuvwxyz";
		
		//Deletions
		for (int i = 0; i< word.length(); i++) {
			String newWord = word.substring(0, i)  + word.substring(i+1, word.length());
			edits1Set.add(newWord);
		}
		
		//Insertion
		for (int i = 0; i <= word.length(); i++) {
			for(int j=0; j < letters.length(); j++) {
				String newWord = word.substring(0, i)  + letters.charAt(j) + word.substring(i, word.length());
				edits1Set.add(newWord);
			}
		}
		
		//Replacement
		for (int i = 0; i < word.length(); i++) {
			for(int j=0; j < letters.length(); j++) {
				String newWord = word.substring(0, i)  + letters.charAt(j) + word.substring(i+1, word.length());	
				if (!(newWord.equals(word))) edits1Set.add(newWord);			
			}
		}
		
		//Tranposition 
		for (int i = 0; i < word.length()-1; i++) {
			String newWord = word.substring(0,i) + word.charAt(i+1) + word.charAt(i) +  word.substring(i+2, word.length());
			if (!(newWord.equals(word))) edits1Set.add(newWord);
		}
		
		
		String[] edits1 = new String[edits1Set.size()];
		for (int i=0; i < edits1Set.size(); i++ ) {
			edits1[i] = (String)edits1Set.toArray()[i];
		}
		
		return edits1;		
	}
	
	static List<String> filterUnknownWords( String[] edits1 ) {
		List<String> knownWords = new ArrayList<String>();
		for (String edit1 : edits1 ) {
			if(SpellingErrorCorrector.wordCounts.containsKey(edit1)) {
				knownWords.add(edit1);
			}
		}
		
		return knownWords;
	}
	
	
	
	static String bestPrediction (String wrongWord, List<String> knownWords ) {
		
		String predictedWord = "not found yet";
		int maxP = 0;
		
		
		for(String knownWord : knownWords) {
			
			EditDistance editDistance = DamerauLevenshteinAlgorithm.findTypeAndChars(knownWord, wrongWord);
			
			int numerator, denominator, pw, P;
			String bigram;
			
			pw = SpellingErrorCorrector.wordCounts.get(knownWord) / SpellingErrorCorrector.totalWordCount;
			
			
			switch(editDistance.getType()) {
			case deletion:
				
				if(
						SpellingErrorCorrector.deletionConfusionMatrix.containsKey(editDistance.getCorrectChar()) 
						&& 
						SpellingErrorCorrector.deletionConfusionMatrix.get(editDistance.getCorrectChar()).containsKey(editDistance.getWrongChar())
					) {
					numerator = SpellingErrorCorrector.deletionConfusionMatrix.get(editDistance.getCorrectChar()).get(editDistance.getWrongChar());
				}else {
					numerator = 0;
				}
				
				if(editDistance.getCorrectChar() == ' ') {
					denominator = SpellingErrorCorrector.unigramCounts.containsKey(editDistance.getWrongChar()) ?
							SpellingErrorCorrector.unigramCounts.get(editDistance.getWrongChar()) : 0;
				}else {
					bigram = "" + editDistance.getCorrectChar() + editDistance.getWrongChar();
					denominator = SpellingErrorCorrector.bigramCounts.containsKey(bigram) ? 
							SpellingErrorCorrector.bigramCounts.get(bigram) : 0;
				}
				
				P = (denominator + numerator) == 0 ? 0 : pw * (numerator / (denominator + numerator));
				if(P >= maxP) {
					maxP = P;
					predictedWord = knownWord;
				}
				break;
				
			case insertion:
				if(
						SpellingErrorCorrector.insertionConfusionMatrix.containsKey(editDistance.getCorrectChar()) 
						&& 
						SpellingErrorCorrector.insertionConfusionMatrix.get(editDistance.getCorrectChar()).containsKey(editDistance.getWrongChar())
					) {
					numerator = SpellingErrorCorrector.insertionConfusionMatrix.get(editDistance.getCorrectChar()).get(editDistance.getWrongChar());
				}else {
					numerator = 0;
				}
				denominator = SpellingErrorCorrector.unigramCounts.containsKey(editDistance.getCorrectChar()) ?
						SpellingErrorCorrector.unigramCounts.get(editDistance.getCorrectChar()) : 0;
						
				P = (denominator + numerator) == 0 ? 0 : pw * (numerator / (denominator + numerator));
				if(P >= maxP) {
					maxP = P;
					predictedWord = knownWord;
				}
				break;
				
			case replacement:
				if(
						SpellingErrorCorrector.replacementConfusionMatrix.containsKey(editDistance.getCorrectChar()) 
						&& 
						SpellingErrorCorrector.replacementConfusionMatrix.get(editDistance.getCorrectChar()).containsKey(editDistance.getWrongChar())
					) {
					numerator = SpellingErrorCorrector.replacementConfusionMatrix.get(editDistance.getCorrectChar()).get(editDistance.getWrongChar());
				}else {
					numerator = 0;
				}
				denominator = SpellingErrorCorrector.unigramCounts.containsKey(editDistance.getCorrectChar()) ?
						SpellingErrorCorrector.unigramCounts.get(editDistance.getCorrectChar()) : 0;
						
				P = (denominator + numerator) == 0 ? 0 : pw * (numerator / (denominator + numerator));
				if(P >= maxP) {
					maxP = P;
					predictedWord = knownWord;
				}
				break;
				
			case transposition:
				if(
						SpellingErrorCorrector.transpositionConfusionMatrix.containsKey(editDistance.getCorrectChar()) 
						&& 
						SpellingErrorCorrector.transpositionConfusionMatrix.get(editDistance.getCorrectChar()).containsKey(editDistance.getWrongChar())
					) {
					numerator = SpellingErrorCorrector.transpositionConfusionMatrix.get(editDistance.getCorrectChar()).get(editDistance.getWrongChar());
				}else {
					numerator = 0;
				}
				
				
				bigram = "" + editDistance.getCorrectChar() + editDistance.getWrongChar();
				denominator = SpellingErrorCorrector.bigramCounts.containsKey(bigram) ? 
						SpellingErrorCorrector.bigramCounts.get(bigram) : 0;
				
				
				P = (denominator + numerator) == 0 ? 0 : pw * (numerator / (denominator + numerator));
				if(P >= maxP) {
					maxP = P;
					predictedWord = knownWord;
				}
				break;
			}
			
			//System.out.println(knownWord + " - " + wrongWord + "\n" + editDistance.getType() + "\n" + editDistance.getCorrectChar() + " - " + editDistance.getWrongChar());
			//System.out.println("--------------------");
			
		}
		
		return predictedWord;
	}
	static String bestPredictionWithSmoothing (String wrongWord, List<String> knownWords ) {
		
		String predictedWordWithSmoothing = "not found yet";
		int maxP = 0;
		
		
		for(String knownWord : knownWords) {
			
			EditDistance editDistance = DamerauLevenshteinAlgorithm.findTypeAndChars(knownWord, wrongWord);
			
			int numerator, denominator, pw, P;
			String bigram;
			
			pw = SpellingErrorCorrector.wordCounts.get(knownWord) / SpellingErrorCorrector.totalWordCount;
			
			
			switch(editDistance.getType()) {
			case deletion:
				
				if(
						SpellingErrorCorrector.deletionConfusionMatrix.containsKey(editDistance.getCorrectChar()) 
						&& 
						SpellingErrorCorrector.deletionConfusionMatrix.get(editDistance.getCorrectChar()).containsKey(editDistance.getWrongChar())
					) {
					numerator = SpellingErrorCorrector.deletionConfusionMatrix.get(editDistance.getCorrectChar()).get(editDistance.getWrongChar()) ;
				}else {
					numerator = 0;
				}
				
				if(editDistance.getCorrectChar() == ' ') {
					denominator = SpellingErrorCorrector.unigramCounts.containsKey(editDistance.getWrongChar()) ?
							SpellingErrorCorrector.unigramCounts.get(editDistance.getWrongChar()) : 0;
				}else {
					bigram = "" + editDistance.getCorrectChar() + editDistance.getWrongChar();
					denominator = SpellingErrorCorrector.bigramCounts.containsKey(bigram) ? 
							SpellingErrorCorrector.bigramCounts.get(bigram) : 0;
				}
				
				P = (denominator + numerator) == 0 ? 0 : pw * ((numerator +1) / (denominator + numerator));
				if(P >= maxP) {
					maxP = P;
					predictedWordWithSmoothing = knownWord;
				}
				break;
				
			case insertion:
				if(
						SpellingErrorCorrector.insertionConfusionMatrix.containsKey(editDistance.getCorrectChar()) 
						&& 
						SpellingErrorCorrector.insertionConfusionMatrix.get(editDistance.getCorrectChar()).containsKey(editDistance.getWrongChar())
					) {
					numerator = SpellingErrorCorrector.insertionConfusionMatrix.get(editDistance.getCorrectChar()).get(editDistance.getWrongChar()) ;
				}else {
					numerator = 0;
				}
				denominator = SpellingErrorCorrector.unigramCounts.containsKey(editDistance.getCorrectChar()) ?
						SpellingErrorCorrector.unigramCounts.get(editDistance.getCorrectChar()) : 0;
						
				P = (denominator + numerator) == 0 ? 0 : pw * ((numerator +1 )/ (denominator + numerator));
				if(P >= maxP) {
					maxP = P;
					predictedWordWithSmoothing = knownWord;
				}
				break;
				
			case replacement:
				if(
						SpellingErrorCorrector.replacementConfusionMatrix.containsKey(editDistance.getCorrectChar()) 
						&& 
						SpellingErrorCorrector.replacementConfusionMatrix.get(editDistance.getCorrectChar()).containsKey(editDistance.getWrongChar())
					) {
					numerator = SpellingErrorCorrector.replacementConfusionMatrix.get(editDistance.getCorrectChar()).get(editDistance.getWrongChar());
				}else {
					numerator = 0;
				}
				denominator = SpellingErrorCorrector.unigramCounts.containsKey(editDistance.getCorrectChar()) ?
						SpellingErrorCorrector.unigramCounts.get(editDistance.getCorrectChar())  : 0;
						
				P = (denominator + numerator) == 0 ? 0 : pw * ((numerator+1) / (denominator + numerator));
				if(P >= maxP) {
					maxP = P;
					predictedWordWithSmoothing = knownWord;
				}
				break;
				
			case transposition:
				if(
						SpellingErrorCorrector.transpositionConfusionMatrix.containsKey(editDistance.getCorrectChar()) 
						&& 
						SpellingErrorCorrector.transpositionConfusionMatrix.get(editDistance.getCorrectChar()).containsKey(editDistance.getWrongChar())
					) {
					numerator = SpellingErrorCorrector.transpositionConfusionMatrix.get(editDistance.getCorrectChar()).get(editDistance.getWrongChar()) ;
				}else {
					numerator = 0;
				}
				
				
				bigram = "" + editDistance.getCorrectChar() + editDistance.getWrongChar();
				denominator = SpellingErrorCorrector.bigramCounts.containsKey(bigram) ? 
						SpellingErrorCorrector.bigramCounts.get(bigram) : 0;
				
				
				P = (denominator + numerator) == 0 ? 0 : pw * ((numerator +1 )/ (denominator + numerator ));
				if(P >= maxP) {
					maxP = P;
					predictedWordWithSmoothing = knownWord;
				}
				
				break;
			}
			
			//System.out.println(knownWord + " - " + wrongWord + "\n" + editDistance.getType() + "\n" + editDistance.getCorrectChar() + " - " + editDistance.getWrongChar());
			//System.out.println("--------------------");
			
		}
		
		return predictedWordWithSmoothing;
	}

}
