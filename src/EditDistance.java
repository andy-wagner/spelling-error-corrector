enum ConfusionType {
	insertion, deletion, replacement, transposition;
}
public class EditDistance {
	private boolean isEditOne; 
	private ConfusionType type;
	
	private Character wrongChar;
	private Character correctChar; 
	
	EditDistance( ConfusionType type, Character correctChar, Character wrongChar){
		this.type = type;
		this.correctChar = correctChar;
		this.wrongChar = wrongChar;
	}
	
	EditDistance (int editDistance, ConfusionType type){
		if(editDistance == 1) {
			this.isEditOne = true;
			this.type = type;
		}
		else this.isEditOne = false;
			
	}

	public boolean isEditOne() {
		return isEditOne;
	}

	public ConfusionType getType() {
		return type;
	}

	public Character getWrongChar() {
		return wrongChar;
	}

	public Character getCorrectChar() {
		return correctChar;
	}
	
	
	
	
	
	
}
