import java.util.ArrayList;
import java.util.List;

public class ConnectFourBoard {
	public static final int HUMAN = 1;
	public static final int CPU = 2;
	
	final int width = 7;
	final int height = 6;
	final int maxMoves = width * height;
	
	int previousPlayer = CPU;
	int totalMoves;
	int nextCPUMove;

	int[] board = new int[maxMoves];
		
	/**
	 * Checks board for piece while checking for bounds.
	 * @param x x coordinate in board for piece
	 * @param y y coordinate in board for piece
	 * @return players number if found, 0 if empty, -1 if out of bounds
	 */
	private int getPiece(int x, int y) {
		return (x < 0 || x >= width || y < 0 || y >= height) ? -1 : board[y * width + x];
	}

	/**
	 * Tries to drop a piece into the board and returns if it succeeded.
	 * @param column what column the piece will be dropped into
	 * @param player for what player the piece should be
	 * @return true if move succeeded, false otherwise.
	 */
	public boolean dropPiece(int column, int player) {
		for (int y = height - 1; y >= 0; y--) {
			if (getPiece(column, y) == 0) {
				board[y * width + column] = player;
				totalMoves++;
				return true;
			}
		}
		return false;
	}	
	/**
	 * Removes the top piece from a column the board.
	 * @param colum what column the piece will be removed from
	 * @return if removal was successful.
	 */
	private boolean removePiece(int column) {
		for (int y = 0; y < height; y++) {
			if (getPiece(column, y) != 0) {
				board[y * width + column] = 0;
				totalMoves--;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Toggles which player is next depending on previous player
	 * @return the next player which turn it is.
	 */
	public int getNextPlayer() {
		return previousPlayer = (previousPlayer == HUMAN) ? CPU : HUMAN;
	}
	
	/**
	 * @return the computers next move
	 */
	public int getNextCPUMove() {
		return nextCPUMove;
	}
	/**
	 * Check if game is won or is over.
	 * @returns 1 if HUMAN player won, 2 if CPU player won, -1 if tied, 0 if not game over
	 */
	public int checkWin() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = getPiece(x, y);
				//Check rows
				if (p != 0 && p == getPiece(x+1,y) && p == getPiece(x+2,y) && p == getPiece(x+3,y)) return p;	
				//Check columns 
				if (p != 0 && p == getPiece(x,y+1) && p == getPiece(x,y+2) && p == getPiece(x,y+3)) return p;
				//Check diagonals
				for (int d = -1; d <= 1; d += 2)
					if (p != 0 && p == getPiece(x+1*d,y+1) && p == getPiece(x+2*d,y+2) && p == getPiece(x+3*d,y+3)) return p;
			}
		}
		return (totalMoves == maxMoves) ? -1 : 0;
	}
	
	private int evaluateScore() {
		return getScore(CPU) - getScore(HUMAN);
	}
	
	/**
	 * Calculates the current score for a player based on how many lines it has and how long they are.
	 * @param player what player the score is calculated for
	 * @return the sum of the total score for a current game state
	 */
	private int getScore(int player) {
		int score = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = getPiece(x, y);
				//Only get score for player selected
				if (p == player) {
					//Check rows
					if (p != 0 && p == getPiece(x+1,y)) score += 5;	
					if (p != 0 && p == getPiece(x+1,y) && p == getPiece(x+2,y)) score += 10;	
					if (p != 0 && p == getPiece(x+1,y) && p == getPiece(x+2,y) && p == getPiece(x+3,y)) score += 1000;
					//Check columns 
					if (p != 0 && p == getPiece(x,y+1)) score += 5;
					if (p != 0 && p == getPiece(x,y+1) && p == getPiece(x,y+2)) score += 10;
					if (p != 0 && p == getPiece(x,y+1) && p == getPiece(x,y+2) && p == getPiece(x,y+3)) score += 1000;
					//Check diagonals				
					for (int d = -1; d <= 1; d += 2) {
						if (p != 0 && p == getPiece(x+1*d,y+1)) score += 5;
						if (p != 0 && p == getPiece(x+1*d,y+1) && p == getPiece(x+2*d,y+2)) score += 10;
						if (p != 0 && p == getPiece(x+1*d,y+1) && p == getPiece(x+2*d,y+2) && p == getPiece(x+3*d,y+3)) score += 1000;
					}
				}
			}
		}	
		return score;	
	}
	
	/**
	 * Tries to drop a piece in every column of the board and stores and returns all moves which were successful.
	 * @return list of possible legal moves
	 */
	private List<Integer> getPossibleMoves() {
		List<Integer> possibleMoves = new ArrayList<Integer>();
		for (int i = 0; i < width; i++) {
			if (!dropPiece(i, CPU)) continue;
			possibleMoves.add(i);
			removePiece(i);
		}
		return possibleMoves;
	}
	
	/**
	 * Allows the program to look ahead on future possible outcomes in a game of connect four to decide which is the optimal next move in the current position.
	 * Uses a minmax strategy with alpha-beta pruning optimizations.
	 * It will recursively generate a tree of possible outcomes until depth is 0, or game is over at current position
	 * First getting all possible future moves by "testing" to drop a piece in all columns on the board and removing it and storing the columns in which it succeeded to a list.
	 * Then it will return an evaluation of that move and compare it to the other possible moves by traversing the tree and either minimizing or maximizing the evaluation.
	 * Moves will be evaluated and pruned (not consider future moves from that branch) if there was already a better possible move previously found for the current players turn.
	 * Pruning is not guaranteed, it depends on the order of the moves are in, ideally the possible moves should be ordered from best to worse for the player its current turn it is.
	 * After it is run the best possible move will be stored and the score for that move will be returned.
	 * 
	 * @param depth how far ahead it will look for future possible outcomes.
	 * @param alpha the highest evaluation currently possible assuming best play for the opponent, should be initialized to lowest possible number.
	 * @param beta the lowest evaluation currently possible assuming best play for the opponent, should be initialized to highest possible number.
	 * @param maximizingPlayer if current call to the method is for the maximizer or the minimizer, if its either the players turn or computers turn for the current step of evaluation.
	 * 
	 * @returns A static evaluation of the current state of the game when depth is 0 or a position results in a win, loss or tie. 
	 * The static evaluation will sum up the total value of the computers lines and subtract the sum of the value of the players lines, (the more and longer the lines the better the score). 
	 * Computer will be trying to maximize the evaluation and human will try to minimize it when trying to get the best possible move for the Computer.
	 * 
	 * @throws IllegalArgumentException if depth is under 0, initial call should have a positive depth and recursion will stop when it reaches depth 0.
	 */
	public int minMax(int depth, int alpha, int beta, boolean maximizingPlayer) {	
		if(depth < 0) throw new IllegalArgumentException();
		//Terminal move check, checks for depth or if game is over at the current position.
		if (depth == 0 || checkWin() != 0) return evaluateScore();
		
		//Get all possible moves for current play
		List<Integer> possibleMoves = getPossibleMoves();		
		int bestMove = possibleMoves.get(0);
		if(maximizingPlayer) {
			for (int m : possibleMoves) {
				//Drop piece and evaluate the outcome then remove it.
				dropPiece(m, CPU);
				int eval = minMax(depth - 1, alpha, beta, false);
				removePiece(m);	
				//Set alpha (highest current evaluation) to evaluation if its bigger than alpha and store what move leads to that evaluation.
				if(eval > alpha) {
					alpha = eval;
					bestMove = m;
				}
				//Prune branch that can't affect the outcome due to the fact that one side will prevent that branch from being reached as they have a better option earlier in the tree.
				if(beta <= alpha) break;
			}
			//Store best move
			nextCPUMove = bestMove;
			return alpha;
		} else {
			for (int m : possibleMoves) {
				dropPiece(m, HUMAN);
				int eval = minMax(depth - 1, alpha, beta, true);
				removePiece(m);
				//Set beta to minimum evaluation and store what move leads to that evaluation.
				if(eval < beta) {
					beta = eval;
					bestMove = m;
				}
				if(beta <= alpha) break;
			}
			nextCPUMove = bestMove;
			return beta;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();			
		result.append("- - - - - - -\n");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				result.append(getPiece(x, y) + " ");
			}
			result.append("\n");
		}
		result.append("- - - - - - -\n");
		result.append("0 1 2 3 4 5 6\n");

		return result.toString();
	}
}
