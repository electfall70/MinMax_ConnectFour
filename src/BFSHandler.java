//@author Pol Lozano Llorens - polo0976
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BFSHandler {
	private Map<String, String> previousMap = new HashMap<>();
	private Map<String, Byte> distanceMap = new HashMap<>();
	
	/**
	 * Performs a breadth first search on the graph starting from the start node until all nodes have been visited.
	 * First it will check the validity of the parameters it took. 
	 * If they are valid a queue will be used to perform a BFS traversal of the graph. Start node will be added to the queue and marked as visited.
	 * It will iteratively visit each adjacent node that has not been already visited, mark them as visited and store their distance and previous node.
	 * Then add them to the queue, to be polled and have their adjacent nodes visited.
	 * Queue will ensure that all children from distance 1 are visited, then 2 and so on...
	 * After BFS is successfully completed distanceMap will contain distance from start for every node in graph.
	 * previousMap will contain the parent of each node, which can be backtracked to rebuild the shortest path towards each node.
	 * 
	 * @param graph graph BFS will be performed on
	 * @param start starting node for BFS, for bacon game that will be his name.
	 * 
	 * @throws IllegalArgumentException if graph is null or start node is not in graph.
	 */
	public BFSHandler(Graph graph, String start) {
		if(graph == null || graph.getEdges(start) != null) 
			throw new IllegalArgumentException();	
		Queue<String> queue = new ArrayDeque<>(); //Faster than linked list queue
		queue.add(start);
		distanceMap.put(start, (byte) 0);
		
		while (!queue.isEmpty()) {
			String current = queue.poll();
			for (String adjNode : graph.getEdges(current)) {
				//If node has not already been visited
				if (!distanceMap.containsKey(adjNode)) {
					queue.add(adjNode);
					previousMap.put(adjNode, current);
					distanceMap.put(adjNode, (byte) (distanceMap.get(current) + 1));				
				}
			}
		}
	}
	
	/**
	 * Checks if there is a path to target node from start node
	 * @param target node to check if there is a path towards
	 * @return if there is a path leading to target node from start node.
	 */
	public boolean hasPathTo(String target) {
		return distanceMap.containsKey(target);
	}

	/**
	 * Backtracks path it took from start node to arrive to target node. Returns path from start node to target.
	 * @param target the node it will get the path towards
	 * @return Iterable of nodes that make the path towards from start to target node.
	 */
	public Iterable<String> pathTo(String target) {
		List<String> path = new LinkedList<>();
		for (String current = target; distanceMap.containsKey(current); current = previousMap.get(current)) {
			path.add(current);
		}
		Collections.reverse(path);
		return path;
	}
	
	/**
	 * Returns distance towards target node, divided by two because for bacon game we only care about actors, movies act as edges between actors.
	 * @param target node to check for distance towards to.
	 * @return if a path is found towards target node from start, distance towards node / 2, otherwise returns Byte.MAX_VALUE;
	 */
	public byte distanceTo(String target) {
		return (byte) (hasPathTo(target) ? distanceMap.get(target) / 2 : Byte.MAX_VALUE);
	}
}
