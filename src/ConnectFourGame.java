//Pol Lozano Llorens - polo0976
public class ConnectFourGame {
    private InputHandler input;
	private ConnectFourBoard board;
	
	public static void main(String[] args) {
		ConnectFourGame game = new ConnectFourGame();
		game.start();
	}

	private void start() {
		initialize();
		runGameLoop();
		exit();
	}
	
	private void initialize() {
		input = new InputHandler();
		board = new ConnectFourBoard();
		System.out.println("Welcome to connect four!\n" + board);		
	}
	
	private void runGameLoop() {
		boolean gameOver = false;
		while (!gameOver) {
			if (board.getNextPlayer() == ConnectFourBoard.HUMAN) {
				System.out.println("Pick a column 0-6.");
				while (!board.dropPiece(input.readInt(-1), ConnectFourBoard.HUMAN))
					System.out.println("Invalid column or full please pick again.");
			} else {
				long start = System.currentTimeMillis();
				board.minMax(5, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
				board.dropPiece(board.getNextCPUMove(), ConnectFourBoard.CPU);
				System.out.printf("CPU Plays %d in %d ms.\n", board.getNextCPUMove(), System.currentTimeMillis() - start);
			}

			switch (board.checkWin()) {
			case 0: 
				System.out.println(board);
				break;
			case -1:
				System.out.println("Its a tie!");
				gameOver = true;
				break;
			case 1:
				System.out.println("You win!");
				gameOver = true;
				break;
			case 2:
				System.out.println("You lost!");
				gameOver = true;
				break;
			}
		}
	}

	private void exit() {
		input.close();
		System.out.println("Thank you for playing!");
	}
}
