import java.io.FileWriter;
import java.io.IOException;


public class Ex1 {
		
	public static void main(String[] args) throws IOException {	
		QuereFromFile input = new QuereFromFile("input.txt");
		FileWriter fw = new FileWriter("output.txt");
		while(input.hasNextQuery()){
			fw.write(input.NextQuery()+"\r\n");
		}
		input.close();
		fw.close();
	}
	
}
