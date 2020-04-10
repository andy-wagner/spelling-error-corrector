import java.io.File;
import java.util.List;
import java.util.Scanner;

public class FileRead {
	

	static void readCorpus() {
		try {
			Scanner scanner = new Scanner(new File("corpus.txt"));
			String line;
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				line = line.toLowerCase();
				line = line.replaceAll("--" , " ");
				
				Utils.extractWords(line);
				
			}
			
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	static void readSpellErrors() {
		try {
			Scanner scanner = new Scanner(new File("spell-errors.txt"));
			String line;
			String correctWord;
			String wrongWords [];
			DamerauLevenshteinAlgorithm dla = new DamerauLevenshteinAlgorithm(1, 1, 1, 1);
			EditDistance editDistance; 
			
			while(scanner.hasNextLine()) {
				line = scanner.nextLine().toLowerCase();			
				correctWord = line.split(":")[0] ;
				
				if(SpellingErrorCorrector.wordCounts.containsKey(correctWord)) {
					int value = SpellingErrorCorrector.wordCounts.get(correctWord);
					value++;
					SpellingErrorCorrector.wordCounts.replace(correctWord, value);
				}else {
					SpellingErrorCorrector.wordCounts.put(correctWord, 1);
				}
				
				line = line.substring(line.indexOf(':') + 1, line.length()).trim();
				wrongWords = line.split(", ");
				for(int i = 0; i < wrongWords.length; i++ ) {
					int starIndex = wrongWords[i].indexOf('*');
					String wrongWord;
					int frequency = 1;
					
					if(starIndex == -1) {
						wrongWord = wrongWords[i];
					}else {
						wrongWord = wrongWords[i].substring(0, starIndex);
						frequency = Integer.parseInt(wrongWords[i].substring(starIndex + 1,  wrongWords[i].length()));
					}

				
					editDistance = dla.execute(correctWord, wrongWord);
					
					
					if (editDistance.isEditOne()) {
						
						switch (editDistance.getType()) {
						case deletion: 
							Utils.updateDeletionConfusionMatrix(correctWord, wrongWord, frequency);
							break;
						case insertion:
							Utils.updateInsertionConfusionMatrix(correctWord, wrongWord, frequency);
							break;
						case replacement:
							Utils.updateReplacementConfusionMatrix(correctWord, wrongWord, frequency);
							break;
						case transposition:
							Utils.updateTranspositionConfusionMatrix(correctWord, wrongWord, frequency);
							break;	
						}	
					}						
				}	
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	static void readInputFile() {
		try {
			Scanner scanner = new Scanner(new File("test-words-misspelled.txt"));
			String wrongWord;
			while(scanner.hasNextLine()) {
				wrongWord = scanner.nextLine().trim();
				wrongWord = wrongWord.toLowerCase();
				
				String[] edits1 = Utils.generateEdits1(wrongWord);
				
				List<String> knownWords = Utils.filterUnknownWords(edits1);
				
				
				if(knownWords.size() == 0) SpellingErrorCorrector.output.add("");
				else if ( knownWords.size() == 1) SpellingErrorCorrector.output.add(knownWords.get(0));
				else {
					String predictedWord = Utils.bestPrediction(wrongWord, knownWords);
					SpellingErrorCorrector.output.add(predictedWord);
				}
			}
			
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	static void readCorrectOutput() {
		try {
			Scanner scanner = new Scanner(new File("test-words-correct.txt"));
			String correct;
			int count = 0;
			while(scanner.hasNextLine()) {
				correct = scanner.nextLine().trim();
				
				if(!correct.equals(SpellingErrorCorrector.output.get(count))) {
					System.out.println(correct + " - " + SpellingErrorCorrector.output.get(count) + " - " + (count+1));
				}
				count ++;
			}
			
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
