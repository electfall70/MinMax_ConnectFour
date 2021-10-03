//@author Pol Lozano Llorens - polo0976
import java.util.Scanner;

public class BaconGame {
	private Graph graph;
	private BFSHandler finder;

	public static void main(String[] args) {
		BaconGame program = new BaconGame();
		program.start();
	}
	
	private void start() {
		initialize();	
		runCommandLoop();
		exit();
	}

	private void initialize() {
		long totalTime = System.currentTimeMillis();
		long start = System.currentTimeMillis();

		graph = new Graph("C:\\Users\\Pol Lozano Llorens\\eclipse-workspace\\AldaBaconGame\\src\\moviedata.txt");
		System.out.printf("Graph loaded in %d ms.\n", System.currentTimeMillis() - start);

		start = System.currentTimeMillis();

		finder = new BFSHandler(graph, "Bacon, Kevin (I)");
		System.out.printf("Breadth first search of graph completed in %d ms.\n", System.currentTimeMillis() - start);
		
		System.out.printf("Bacon game loaded in %d ms.\n", System.currentTimeMillis() - totalTime);
		System.out.println("\nInput the name of the actor you want to find the bacon number and path for!");
		System.out.println("Input 'exit' to exit the game.");
	}

	private void runCommandLoop() {
		Scanner input = new Scanner(System.in);
		String command;
		do {
			System.out.println("\nInput?> ");
			command = input.nextLine();
			if (!command.equals("exit"))
				processQuery(command);
		} while (!command.equals("exit"));
		input.close();
	}
	
	private void processQuery(String name) {
		if (finder.hasPathTo(name)) {
			byte baconNr = finder.distanceTo(name);
			if (baconNr != Byte.MAX_VALUE) {
				System.out.printf("Bacon, Kevin (I) is %d steps away from %s\nThe path is:\n", baconNr, name);
				finder.pathTo(name).forEach(step -> System.out.println(step));
			} else System.out.println("Not connected");
		} else System.out.println("Not in database");
	}
	
	private void exit() {
		System.out.println("Thank you for playing!");		
	}
}
