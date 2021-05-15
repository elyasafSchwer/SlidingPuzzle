import java.util.Arrays;

class ColoredNxMPuzzleState extends State<char[][]>{

	public ColoredNxMPuzzleState(char[][] state){
		super(state);
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof ColoredNxMPuzzleState)) return false;
	    if((getState().length != ((ColoredNxMPuzzleState) other).getState().length) ||
	    	(getState()[0].length != ((ColoredNxMPuzzleState) other).getState()[0].length)){
			return false;
		}
		for (int i = 0; i < getState().length; i++) {
			for (int j = 0; j < getState()[0].length; j++) {
				if(getState()[i][j] != ((ColoredNxMPuzzleState) other).getState()[i][j]){
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Arrays.deepHashCode(getState());
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < getState().length; i++) {
			result += "{";
			for (char j = 0; j < getState()[0].length; j++) {
				result +=	(getState()[i][j] == 0) ? "_, " : ((int)getState()[i][j] + ", ");
			}
			result +="}\n";
		}
		return result+"\n";
	}
	
	@Override
	protected ColoredNxMPuzzleState clone() {
		if(getState() == null) return null;
		char[][] result = new char[getState().length][getState()[0].length];
		for (int i = 0; i < result.length; i++) {
			result[i] = getState()[i].clone();
		}
		return new ColoredNxMPuzzleState(result);
	}
	
	public void sawp(int i, int j, int i1, int j1) {
		if(i < 0 || i >= getState().length || j < 0 || j >=getState()[0].length) return;
		if(i1 < 0 || i1 >= getState().length || j1 < 0 || j1 >=getState()[0].length) return;
		char temp = getState()[i][j];
		getState()[i][j] = getState()[i1][j1];
		getState()[i1][j1] = temp;
	}
	
	public int manhattanDistance(int i, int j){
		int target_i = (getState()[i][j] - 1) / getState()[0].length;
		int target_j = getState()[i][j] - target_i*getState()[0].length - 1;
		if(getState()[i][j] == 0) {
			target_i = getState().length-1;
			target_j = getState()[0].length-1;
		}
		return Math.abs(target_i-i) + Math.abs(target_j-j);
	}
}