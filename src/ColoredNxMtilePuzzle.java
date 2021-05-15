import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

class ColoredNxMtilePuzzle extends SearchableGame<ColoredNxMPuzzleState>{
	static final double WHITE = 1;
	static final double YELLOW = 0.5;
	static final double RED = 2;
	private double[] costs;

	public ColoredNxMtilePuzzle(double[] costs, ColoredNxMPuzzleState start){
		this.costs = costs;
		setStart(start);
		int n = start.getState().length;
		int m = start.getState()[0].length;
		char[][] goal_mat = new char[n][m];
		for (char i = 0; i < n; i++) {
			for (char j = 0; j < m; j++) {
				goal_mat[i][j] = (char) ((m)*i + j + 1);
			}
		}
		goal_mat[n-1][m-1] = 0;
		setGoal(new ColoredNxMPuzzleState(goal_mat));
	}
	
	@Override
	public Vector<ColoredNxMPuzzleState> operator(ColoredNxMPuzzleState state) {
		int i = -1, j = -1;
		
		for (int i1 = 0; i1 < state.getState().length; i1++) {
			for (int j1 = 0; j1 < state.getState()[0].length; j1++) {
				if(state.getState()[i1][j1]==0){
					i = i1;
					j = j1;
					break;
				}
			}
		}
		
		if(i==-1 || j==-1) return null;
		
		Vector<ColoredNxMPuzzleState> result = new Vector<ColoredNxMPuzzleState>();
		
		if(j<state.getState()[0].length-1){
			ColoredNxMPuzzleState right = state.clone();
			right.sawp(i, j, i, j+1);
			result.add(right);
		}
		
		if(i<state.getState().length-1){
			ColoredNxMPuzzleState down = state.clone();
			down.sawp(i, j, i+1, j);
			result.add(down);
		}
		
		if(j>0){
			ColoredNxMPuzzleState left = state.clone();
			left.sawp(i, j, i, j-1);
			result.add(left);
		}
		
		if(i>0){
			ColoredNxMPuzzleState up = state.clone();
			up.sawp(i, j, i-1, j);
			result.add(up);
		}
		return result;
	}

	@Override
	protected double get_cost_for_step(ColoredNxMPuzzleState t1, ColoredNxMPuzzleState t2) {
		int i = -1, j = -1;
		
		for (int i1 = 0; i1 < t1.getState().length; i1++) {
			for (int j1 = 0; j1 < t1.getState()[0].length; j1++) {
				if(t1.getState()[i1][j1]==0){
					i = i1;
					j = j1;
					break;
				}
			}
		}
		
		if(i==-1 || j==-1) return 1.1;
		
		if(i > 0 && t2.getState()[i-1][j] == 0) return costs[t2.getState()[i][j]];
		if(j > 0 && t2.getState()[i][j-1] == 0) return costs[t2.getState()[i][j]];
		if(i < t1.getState().length-1 && t2.getState()[i+1][j] == 0) return costs[t2.getState()[i][j]];
		if(j < t1.getState()[0].length-1 && t2.getState()[i][j+1] == 0) return costs[t2.getState()[i][j]];
		
		return 0;
	}
	
	@Override
	protected char get_char_for_step(ColoredNxMPuzzleState t1, ColoredNxMPuzzleState t2) {
		int i = -1, j = -1;
		
		for (int i1 = 0; i1 < t1.getState().length; i1++) {
			for (int j1 = 0; j1 < t1.getState()[0].length; j1++) {
				if(t1.getState()[i1][j1]==0){
					i = i1;
					j = j1;
					break;
				}
			}
		}
		
		if(i==-1 || j==-1) return 0;
		
		if(i > 0 && t2.getState()[i-1][j] == 0) return 'U';
		if(j > 0 && t2.getState()[i][j-1] == 0) return 'L';
		if(i < t1.getState().length-1 && t2.getState()[i+1][j] == 0) return 'D';
		if(j < t1.getState()[0].length-1 && t2.getState()[i][j+1] == 0) return 'R';
		
		return 0;
	}

	@Override
	protected double heuristic(ColoredNxMPuzzleState state) {
		double result = 0.0;
		for (int i = 0; i < state.getState().length; i++) {
			for (int j = 0; j < state.getState()[0].length; j++) {
				result += state.manhattanDistance(i, j) * costs[state.getState()[i][j]];
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		String result = get_start().getState().length + "x" + get_start().getState()[0].length+"\n";
		result += "Red: ";
		for (int i = 1; i < costs.length-1; i++) {
			if(costs[i] == 2){
				result += (int)i +",";
			}
		}
		result = result.substring(0, result.length()-1) + "\n";
		result += "Yellow: ";
		for (int i = 0; i < costs.length-1; i++) {
			if(costs[i] == 0.5){
				result += (int)i +",";
			}
		}
		result = result.substring(0, result.length()-1) + "\n";
		for (int i = 0; i < get_start().getState().length; i++) {
			for (int j = 0; j < get_start().getState()[0].length; j++) {
				result += (get_start().getState()[i][j]==0) ? "_," : (int)(get_start().getState()[i][j]) + ",";
			}
			result = result.substring(0, result.length()-1) + "\n";
		}
		return result +"\n";
	}
	
	public boolean hasNextCosts(){
		for (int i = 0; i < costs.length; i++) {
			if(costs[i] != RED){
				return true;
			}
		}
		return false;
	}
	
	public void nextCosts(){
		for (int i = costs.length - 1; i >= 0; i--) {
			if(costs[i] == WHITE){
				costs[i] = YELLOW;
				return;
			}
			if(costs[i] == YELLOW){
				costs[i] = RED;
				return;
			}
			if(costs[i] == RED){
				while(i >= 0 && costs[i] == RED){
					i--;
				}
				i++;
				Arrays.fill(costs, i, costs.length - 1, WHITE);
			}
		}
	}
	
	public void randomCost(){
		for (int i = 0; i < costs.length; i++) {
			double random = Math.random();
			if(random <= 1.0/3) {
				costs[i] = WHITE;
			}
			else if(random <= 2.0/3){
				costs[i] = YELLOW;
			}
			else{
				costs[i] = RED;
			}
		}
	}
	
	public void help_make_all_game_until_depth_d(ColoredNxMPuzzleState parent_state, HashSet<ColoredNxMPuzzleState> hash, FileWriter fw, int d) throws IOException{
		if(d == 0) return;
		setStart(parent_state);
/*		Arrays.fill(costs, WHITE);
		while(hasNextCosts()){
			for (int i = 1; i <= 5; i++) {
				if(i==2 && costs.length>4) continue;
				fw.write(i+"\n"+toString());
				nextCosts();	
			}
		}*/
		for (int i = 0; i <= 3; i++) {
			randomCost();
			for (int j = 1; j <= 5; j++) {
				if((j == 1 || j == 5) && d < 7 ){
					continue;
				}
				if(Math.random() < 0.3){
						fw.write(j+"\n"+toString());
				}
			}
		}
		Vector<ColoredNxMPuzzleState> childs = operator(parent_state);
		childs.removeIf(t->hash.contains(t));
		hash.addAll(childs);
		for (ColoredNxMPuzzleState state : childs) {
			help_make_all_game_until_depth_d(state, hash, fw, d-1);
		}
	}
	
	public void make_all_game_until_depth_d(FileWriter fw, int d) throws IOException{
		help_make_all_game_until_depth_d(get_goal() , new HashSet<ColoredNxMPuzzleState>(), fw, d);
	}
}