	import java.util.HashMap;
	import java.util.Map;

	public class DamerauLevenshteinAlgorithm {
	  private final int deleteCost, insertCost, replaceCost, swapCost;

	 
	  public DamerauLevenshteinAlgorithm(int deleteCost, int insertCost,
	                                     int replaceCost, int swapCost) {
	    /*
	     * Required to facilitate the premise to the algorithm that two swaps of the
	     * same character are never required for optimality.
	     */
	    if (2 * swapCost < insertCost + deleteCost) {
	      throw new IllegalArgumentException("Unsupported cost assignment");
	    }
	    this.deleteCost = deleteCost;
	    this.insertCost = insertCost;
	    this.replaceCost = replaceCost;
	    this.swapCost = swapCost;
	  }

	  /**
	   * Compute the Damerau-Levenshtein distance between the specified source
	   * string and the specified target string.
	   */
	  public EditDistance execute(String source, String target) {
		
		ConfusionType type = ConfusionType.deletion;
		 
		  
	    if (source.length() == 0) {
	    	EditDistance editDistance = new EditDistance(target.length() * insertCost, ConfusionType.insertion);
	    	return editDistance;
	    }
	    if (target.length() == 0) {
	    	EditDistance editDistance = new EditDistance( source.length() * deleteCost, ConfusionType.deletion);
	    	return editDistance;
	    }
	    
	    int deleteDistance = 0;
	    int insertDistance = 0;
	    int matchDistance = 0;
	    
	    int[][] table = new int[source.length()][target.length()];
	    Map<Character, Integer> sourceIndexByCharacter = new HashMap<Character, Integer>();
	    
	    if (target.length()> source.length() && source.charAt(0) != target.charAt(0) ) {
	    	table[0][0] = Math.min(replaceCost, deleteCost + insertCost);
	    	type = ConfusionType.insertion;
	    } else if(target.length() ==  source.length() && source.charAt(0) != target.charAt(0)) {
		      table[0][0] = Math.min(replaceCost, deleteCost + insertCost);
		      type = ConfusionType.replacement;
	    }else if (target.length() < source.length() && source.charAt(0) != target.charAt(0)) {
		      table[0][0] = Math.min(replaceCost, deleteCost + insertCost);
		      type = ConfusionType.deletion;
	    }
	    
	    /*if (source.charAt(0) != target.charAt(0)) {
	     table[0][0] = Math.min(replaceCost, deleteCost + insertCost); 
	  	}else if (target.length()> source.length()) type = ConfusionType.insertion; 
	   */
	   
	    sourceIndexByCharacter.put(source.charAt(0), 0);
	    for (int i = 1; i < source.length(); i++) {
	      deleteDistance = table[i - 1][0] + deleteCost;
	      insertDistance = (i + 1) * deleteCost + insertCost;
	      matchDistance = i * deleteCost
	          + (source.charAt(i) == target.charAt(0) ? 0 : replaceCost);
	      table[i][0] = Math.min(Math.min(deleteDistance, insertDistance),
	                             matchDistance);
	    }
	    for (int j = 1; j < target.length(); j++) {
	      deleteDistance = (j + 1) * insertCost + deleteCost;
	      insertDistance = table[0][j - 1] + insertCost;
	      matchDistance = j * insertCost
	          + (source.charAt(0) == target.charAt(j) ? 0 : replaceCost);
	      table[0][j] = Math.min(Math.min(deleteDistance, insertDistance),
	                             matchDistance);
	    }
	    

	    int swapDistance = 0;
	    for (int i = 1; i < source.length(); i++) {
	      int maxSourceLetterMatchIndex = source.charAt(i) == target.charAt(0) ? 0
	          : -1;
	      for (int j = 1; j < target.length(); j++) {
	        Integer candidateSwapIndex = sourceIndexByCharacter.get(target
	            .charAt(j));
	        int jSwap = maxSourceLetterMatchIndex;
	        deleteDistance = table[i - 1][j] + deleteCost;
	        insertDistance = table[i][j - 1] + insertCost;
	        matchDistance = table[i - 1][j - 1];
	        if (source.charAt(i) != target.charAt(j)) {
	          matchDistance += replaceCost;
	        } else {
	          maxSourceLetterMatchIndex = j;
	        }
	        
	        if (candidateSwapIndex != null && jSwap != -1) {
	          int iSwap = candidateSwapIndex;
	          int preSwapCost;
	          if (iSwap == 0 && jSwap == 0) {
	            preSwapCost = 0;
	          } else {
	            preSwapCost = table[Math.max(0, iSwap - 1)][Math.max(0, jSwap - 1)];
	          }
	          swapDistance = preSwapCost + (i - iSwap - 1) * deleteCost
	              + (j - jSwap - 1) * insertCost + swapCost;
	        } else {
	          swapDistance = Integer.MAX_VALUE;
	        }
	        table[i][j] = Math.min(Math.min(Math
	            .min(deleteDistance, insertDistance), matchDistance), swapDistance);
	        
	      }
	      
	      if ( target.length() < source.length() && deleteDistance <= insertDistance  ) type = ConfusionType.deletion;
	      else if ( target.length() > source.length() && insertDistance <= matchDistance ) type = ConfusionType.insertion;
	      else if (matchDistance < swapDistance ) type = ConfusionType.replacement;
	      else type = ConfusionType.transposition;

	      
	      sourceIndexByCharacter.put(source.charAt(i), i);
	    }
	    
	    EditDistance editDistance = new EditDistance(table[source.length() - 1][target.length() - 1], type);
	    
	    return editDistance;
	  }	  
	  
	  static EditDistance findTypeAndChars( String correctWord, String wrongWord ){
		  EditDistance editDistance = null;
		  if(correctWord.length() > wrongWord.length() ) {
			  for (int i = 0; i < correctWord.length(); i++ ) {
				  if( i == wrongWord.length() || correctWord.charAt(i) != wrongWord.charAt(i)) {
					  if(i > 0) {
						  editDistance = new EditDistance(ConfusionType.deletion, correctWord.charAt(i-1), correctWord.charAt(i));
					  }else {
						  editDistance = new EditDistance(ConfusionType.deletion, ' ', correctWord.charAt(i));
					  }
					  break;
				  }
			  }
		  }else if (correctWord.length() < wrongWord.length()) {
			  for (int i = 0; i < wrongWord.length(); i++ ) {
				  if( i == correctWord.length() || correctWord.charAt(i) != wrongWord.charAt(i)) {
					  if(i > 0) {
						  editDistance = new EditDistance(ConfusionType.insertion, correctWord.charAt(i-1), wrongWord.charAt(i));
					  }else {
						  editDistance = new EditDistance(ConfusionType.insertion, ' ', wrongWord.charAt(i));
					  }
					  break;
				  }
			  }
		  }else {
			  for (int i = 0; i<correctWord.length() ; i++ ) {
				  if(correctWord.charAt(i) != wrongWord.charAt(i)) {
					  if(i != correctWord.length() - 1 &&
							  correctWord.charAt(i) == wrongWord.charAt(i+1) && 
							  correctWord.charAt(i+1) == wrongWord.charAt(i)) {
						  editDistance = new EditDistance(ConfusionType.transposition, correctWord.charAt(i), wrongWord.charAt(i));
					  }else {
						  editDistance = new EditDistance(ConfusionType.replacement, correctWord.charAt(i), wrongWord.charAt(i));
					  }
					  break;
				  }
			  }
		  }
		  
		  
		  
		  return editDistance;
	  }
	  
	}

