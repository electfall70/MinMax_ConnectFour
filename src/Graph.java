//@author Pol Lozano Llorens - polo0976
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
	//Adjacency map for the graph
	private Map<String, Set<String>> adjMap = new HashMap<>();
	
	/**
	 * Builds a graph from specified text file.
	 * @param filepath specified path to read in graph from
	 */
	public Graph(String filepath) {
		//BufferedReader will auto-close because of try-with-resources 
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String currentActor = new String();
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				String name = currentLine.substring(3);
				if (currentLine.startsWith("<a>")) currentActor = name;
				connect(currentActor, name);
			}
		} catch (FileNotFoundException e) {
			System.out.println("No file found in specified path.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error reading file. File corrupted or formatted wrong.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a node to the graph if it doesn't already exist
	 * @param newNode node to be added to graph
	 */
	public void add(String newNode) {
		adjMap.putIfAbsent(newNode, new HashSet<String>());
	}

	/**
	 * Connects two nodes together, if the nodes don't already exist they will be added, then connected.
	 * @param source which node will be connected
	 * @param target node it will connect to
	 */
	public void connect(String source, String target) {
		if (!adjMap.containsKey(source)) add(source);
		if (!adjMap.containsKey(target)) add(target);
		adjMap.get(source).add(target);
		adjMap.get(target).add(source);
	}

	/**
	 * Returns an iterable of all edges from a specified node
	 * @param source the node that will be checked for edges and returned
	 * @return iterable of edges from source node
	 */
	public Iterable<String> getEdges(String source) {
		return adjMap.get(source);
	}
}
