package ramsey;

import java.util.ArrayList;

/**
 * This class will search a given CayleyGraph for complete subgraphs (cliques) of a given color.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class CliqueChecker {

	private CayleyGraph cayleyGraph;
	private int cliqueSize;

	
	/**
	 * This is the main constructor for the CliqueChecker class. It will
	 * initialize the necessary global variables. Variables will be sized
	 * correctly depending on the the static Config class which dictates the
	 * number of elements in the CayleyGraph and the number of elements in the
	 * Clique we will be searching for.
	 * 
	 * @param cayleyGraph The CayleyGraph object we will be looking for cliques
	 *        in.
	 */
	public CliqueChecker(CayleyGraph cayleyGraph, int cliqueSize) {
		this.cayleyGraph = cayleyGraph;
		this.cliqueSize = cliqueSize;
	}

	
	public void findCliqueParallel(int threadCount, String color){
		FindCliqueThread[] threads = new FindCliqueThread[threadCount];
		
		// Initialize Threads
		for (int i = 0; i < threadCount; i++) {
			threads[i] = new FindCliqueThread(i,threadCount,color);
		}

		// Sync up threads
		for (int i = 0; i < threadCount; i++) {
			try {
				threads[i].join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class FindCliqueThread extends Thread{
		int threadId;
		int maxThreads;
		String color;
		
		FindCliqueThread(int threadId, int maxThreads, String color){
			this.threadId = threadId;
			this.maxThreads = maxThreads;
			this.color = color;
			start();
		}
		
		public void run() {
			for (int i = threadId; i < cayleyGraph.getNumOfElements() - cliqueSize; i += maxThreads) {
				try {
					findCliqueRecursive(i,color);
				} 
				// TODO why cant I just declare throws exception here?
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
    
	public void findClique(String color) throws Exception {
		Debug.write("Beginning findClique with color " + color);
		for (int i = 0; i <= (cayleyGraph.getNumOfElements() - cliqueSize); i++) {
			findCliqueRecursive(i, color);
			if (Config.CLIQUE_SEARCH_STRATAGY ==  CLIQUE_SEARCH_TYPE.FIRST && cayleyGraph.isCliqueIdentified()) {
				Debug.write("Returning from return point I in findClique color " + color);		
				return;
			}
		}
		Debug.write("Returning from return point II in findClique color " + color);
		return;
	}


	private void findCliqueRecursive(int StartingVertexID, String color) throws Exception {
		Debug.write("Beginning findCliqueRecursive with Vertex ID " + StartingVertexID + " and color " + color);
		ArrayList<Vertex> connectedVertices = new ArrayList<Vertex>();
		connectedVertices.add(cayleyGraph.getVertexById(StartingVertexID));
		findCliqueRecursive(connectedVertices,color);
		Debug.write("Completed findCliqueRecursive with Vertex ID " + StartingVertexID + " and color " + color);
		connectedVertices.clear();
	}
	
	
	private void findCliqueRecursive(ArrayList<Vertex> connectedVertices, String color) throws Exception {
		Debug.write("Beginning findCliqueRecursive with vertex string: " + connectedVertices.size());
		// Check if cliqueSearch style if "First" and we already found a clique
		if (Config.CLIQUE_SEARCH_STRATAGY == CLIQUE_SEARCH_TYPE.FIRST && cayleyGraph.isCliqueIdentified()){
			Debug.write("Returning since clique already found");
			return;
		}
		
		// Loop through all vertices starting with the one after the last vertex in the chain
		for(int i=connectedVertices.get(connectedVertices.size()-1).getId()+1; i < cayleyGraph.getNumOfElements(); i++) {
			// If the vertex being considered is connected
			if (isConnected(connectedVertices,cayleyGraph.getVertexById(i),color)) {
				connectedVertices.add(cayleyGraph.getVertexById(i));
				// If this and makes a completed clique add it to the clique collection
				if (connectedVertices.size() == cliqueSize){
					cayleyGraph.getCliqueCollection().addClique(new Clique(connectedVertices));
				}
				// Otherwise if there are enough possible options left to form a clique proceed with search
				// TODO optimize by adding second condition above.
				else {
					findCliqueRecursive(connectedVertices,color);
				}
				// Remove this vertex from the chain and try the next at this level
				connectedVertices.remove(cayleyGraph.getVertexById(i));
			}
		}	
			
		// Once all have been tried at this level return
		return;		
	}
		
	private boolean isConnected(ArrayList<Vertex> connectedVertices, Vertex vertex, String color) {
		for (int i = 0; i < connectedVertices.size(); i++) {
			if (!connectedVertices.get(i).getEdge(vertex).getColor().equals(color)){	
				return false;
			}
		}
		return true;
	}		
}
