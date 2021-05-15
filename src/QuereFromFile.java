import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

class QuereFromFile{
	static final double RED_COST = 2;
	static final double YELLOW_COST = 0.5;
	static final int BFS = 1;
	static final int DFID = 2;
	static final int ASTAR = 3;
	static final int IDASTAR = 4;
	static final int BFBnB = 5;

	Scanner scan;
	Vector<String> algo_codes;
	
	public QuereFromFile(String filename) throws FileNotFoundException {
		this.scan = new Scanner(new File(filename));
		String[] algo_codes_arr = {"1", "2", "3", "4", "5"};
		this.algo_codes = new Vector<String>(Arrays.asList(algo_codes_arr));
	}
	
	public boolean hasNextQuery(){
		return scan.hasNext();
	}
	
	public String NextQuery(){
		if(!hasNextQuery()) return "";
		String algo_code = scan.nextLine();
		while(!algo_codes.contains(algo_code)){
			algo_code = scan.nextLine();
		}
		StringTokenizer Dimensions = new StringTokenizer(scan.nextLine(), " ,.x");
		int n = Integer.parseInt(Dimensions.nextToken());
		int m = Integer.parseInt(Dimensions.nextToken());
		double costs[] = new double[n*m + 1];
		Arrays.fill(costs, 1, costs.length - 1, 1.0);
		StringTokenizer reds = new StringTokenizer(scan.nextLine(), ", .");
		reds.nextToken();
		while(reds.hasMoreTokens()){
			costs[Integer.parseInt(reds.nextToken())] = RED_COST;
		}
		StringTokenizer yellows = new StringTokenizer(scan.nextLine(), ", .");
		yellows.nextToken();
		while(yellows.hasMoreTokens()){
			costs[Integer.parseInt(yellows.nextToken())] = YELLOW_COST;
		}
		char[][] source = new char[n][m];
		for (int i = 0; i < source.length; i++) {
			StringTokenizer line = new StringTokenizer(scan.nextLine(), ", .");
			for (int j = 0; j < source[0].length; j++) {
				String chr = line.nextToken();
				source[i][j] = (chr.equals("_")) ? 0 : (char)(Integer.parseInt(chr));
			}
		}
		ColoredNxMtilePuzzle game = new ColoredNxMtilePuzzle(costs, new ColoredNxMPuzzleState(source));
		Result result = null;		
		switch (Integer.parseInt(algo_code)) {
		case BFS:
			result = game.BFS();
			break;
		case DFID:
			result = game.DFID();
			break;
		case ASTAR:
			result = game.ASTAR();
			break;
		case IDASTAR:
			result = game.IDASTAR();
			break;
		case BFBnB:
			result = game.DFBnB();
			break;
		default:
			break;
		}
		return (result == null) ? "no path" : result.toString();
	}
	public void close(){
		scan.close();
	}

}