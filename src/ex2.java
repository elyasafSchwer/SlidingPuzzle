import java.io.IOException;
import java.util.Arrays;


public class ex2 {
		
	public static void main(String[] args) throws IOException {

		
		char[][] g = {	{1, 2, 3, 4},
						{9, 5, 11, 7},
						{13, 15, 8, 0},
						{10, 6, 14, 12}
					};
		ColoredNxMPuzzleState state8 = new ColoredNxMPuzzleState(g);
		double costs[] = new double[16];
		Arrays.fill(costs, 1);
		ColoredNxMtilePuzzle game = new ColoredNxMtilePuzzle(costs, state8);
		System.out.println(game.ASTAR());
		System.out.println(game.IDASTAR());
		System.out.println(game.DFBnB());
		
		char[][] g1 = {	{4, 3, 0},
						{2, 1, 5}
					};
		ColoredNxMPuzzleState state9 = new ColoredNxMPuzzleState(g);
		double costs1[] = new double[12];
		Arrays.fill(costs, 1);
		ColoredNxMtilePuzzle game1 = new ColoredNxMtilePuzzle(costs1, state9);
		
		System.out.println(game1.hasNextCosts());
		while(game1.hasNextCosts()){
			game1.nextCosts();
			System.out.println(game1);
		}

		
		char[][] g3 = {	{6,1,2,3,4},
						{11,7,8,9,5},
						{16,12,13,14,10},
						{0,17,18,19,15},
						{21,22,23,24,20}
						};
		ColoredNxMPuzzleState state10 = new ColoredNxMPuzzleState(g);
		double costs2[] = new double[25];
		Arrays.fill(costs2, 1);
		costs[0] = 2;
		costs[1] = 2;
		costs[6] = 0.5;
		ColoredNxMtilePuzzle game2 = new ColoredNxMtilePuzzle(costs2, state10);
		System.out.println(game.BFS()); 
		System.out.println(game.DFID());
		System.out.println(game.ASTAR());
		System.out.println(game.IDASTAR());
		System.out.println(game.DFBnB());
/**		
		QuereFromFile input = new QuereFromFile("input.txt");
		FileWriter fw = new FileWriter("output.txt");
		while(input.hasNextQuery()){
			fw.write(input.NextQuery()+"\n");
		}
		fw.close();
		
		FileWriter fw = new FileWriter("input1.txt");
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 5; j++) {
				char[][] mat = new char[i][j];
				double[] costs = new double[i*j];
				ColoredNxMtilePuzzle game = new ColoredNxMtilePuzzle(costs, new ColoredNxMPuzzleState(mat));
				game.make_all_game_until_depth_d(fw, 12);
			}
		}
		fw.close();*/
	}
	
}
