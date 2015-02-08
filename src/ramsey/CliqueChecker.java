package ramsey;

/**
 * This class will search a given CayleyGraph for complete subgraphs (cliques) of a given color.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class CliqueChecker {

	private Vertex tree[][];
	private int pointerArray[];
	private int pointerArrayIndex;
	private Vertex prevLevelVertex;
	private Vertex[] cliqueVertexArray;
	private CayleyGraph cayleyGraph;

	
	/**
	 * This is the main constructor for the CliqueChecker class. It will
	 * initialize the necessary global variables. Variables will be sized
	 * correctly depending on the input which dictates the number of elements in
	 * the CayleyGraph and the number of elements in the Clique we will be
	 * searching for.
	 * 
	 * @param cayleyGraph The CayleyGraph object we will be looking for cliques
	 *        in.
	 * @param numOfElements The number of elements in the CayleyGraph.
	 */
	public CliqueChecker(CayleyGraph cayleyGraph) {
		initCliqueChecker(Config.CLIQUE_SIZE, Config.NUM_OF_ELEMENTS, cayleyGraph);
	}

    public void initCliqueChecker(int cliqueSize, int numOfElements, CayleyGraph cayleyGraph) {
    	this.tree = new Vertex[cliqueSize][numOfElements];
		this.pointerArray = new int[cliqueSize];
		this.pointerArrayIndex = 0;
		this.cliqueVertexArray = new Vertex[cliqueSize];
		this.cayleyGraph = cayleyGraph;
    }

	/**
	 * This will analyze the CayleyGraph and see if it is a example of a graph
	 * of order this.numOfElements that does have a complete subgraph of order k
	 * (this.clique.getCliqueSize()). In this case k will most often be equal to
	 * 8 for R(8,8).
	 * 
	 * @param color This color defines the color of the Clique this method will
	 *        be searching for.
	 * @return This method will return true if the CayleyGraph has a complete
	 *         subgraph otherwise it will return false.
	 */
	public boolean findClique(String color) {
		// Fill first level of tree with all vertices
		for (int i = 0; i < Config.NUM_OF_ELEMENTS; i++) {
			tree[pointerArrayIndex][i] = cayleyGraph.getCayleyGraphArray()[i];
		}

		// Main Algorithm
		while (pointerArray[0] <= ((Config.NUM_OF_ELEMENTS - Config.CLIQUE_SIZE) + 1)) {
			prevLevelVertex = tree[pointerArrayIndex][pointerArray[pointerArrayIndex]];
			pointerArray[pointerArrayIndex]++;
			pointerArrayIndex++;

			// Fill tree level with all vertices connected to the previous levels vertex with an edge of the specified color
			for (int i = prevLevelVertex.getId() + 1; i < Config.NUM_OF_ELEMENTS; i++) {
				if (isConnected(i,color)) {
					tree[pointerArrayIndex][pointerArray[pointerArrayIndex]] = cayleyGraph.getCayleyGraphArray()[i];
					pointerArray[pointerArrayIndex]++;
				}
			}

			// If at least (this.cliqueSize-Level) elements, then successful row, reset pointer for row and move to next level
			if (pointerArray[pointerArrayIndex] >= (Config.CLIQUE_SIZE - pointerArrayIndex)) {
				if (pointerArrayIndex == (Config.CLIQUE_SIZE - 1)) {
					// Store the found clique into the global variable
					for (int x = 0; x < Config.CLIQUE_SIZE; x++) {
						cliqueVertexArray[x] = tree[x][pointerArray[x] - 1];
					}
					cayleyGraph.getClique().updateClique(cliqueVertexArray.clone());
					clearGlobalVars();
					return true;
				}
				pointerArray[pointerArrayIndex] = 0;
			}

			// Otherwise there are not enough elements for a clique, delete row and slide pointer for previous level up one repeat until link-to-be is non-negative one
			else {
				for (int i = 0; i < Config.NUM_OF_ELEMENTS && tree[pointerArrayIndex][i] != null; i++) {
					tree[pointerArrayIndex][i] = null;
				}
				pointerArray[pointerArrayIndex] = 0;
				pointerArrayIndex--;

				while (tree[pointerArrayIndex][pointerArray[pointerArrayIndex]] == null) {
					for (int i = 0; i < Config.NUM_OF_ELEMENTS && tree[pointerArrayIndex][i] != null; i++) {
						tree[pointerArrayIndex][i] = null;
					}
					pointerArray[pointerArrayIndex] = 0;
					pointerArrayIndex--;
				}
			}
		}
		clearGlobalVars();
		return false;
	}

	/**
	 * This will initialize the pointers back to 0 to ensure the next time
	 * findClique is called it starts at the beginning of the CayleyGraph.
	 * 
	 * @return void
	 */
	private void clearGlobalVars() {
		for (int i = 0; i < Config.CLIQUE_SIZE; i++) {
			pointerArray[i] = 0;
		}
		pointerArrayIndex = 0;
		
		for (int i=0; i<Config.CLIQUE_SIZE; i++){
			for (int j=0; j<Config.NUM_OF_ELEMENTS; j++){
				tree[i][j] = null;
			}
		}
		
		prevLevelVertex = null;
		
		for (int i=0; i<Config.CLIQUE_SIZE; i++){
			cliqueVertexArray[i] = null;
		}
	}
	
	/**
	 * Checks for a given input vertexID if it is connected to the elements in
	 * all previous levels of the tree with an edge of specified color.
	 * 
	 * @param vertexID Vertex ID to be checked for connectivity.
	 * @return True if connected to all previous elements in the potential
	 *         clique with edges of the specified color, otherwise false.
	 */
	private boolean isConnected(int vertexID, String color) {
		for (int i = 0; i < pointerArrayIndex; i++) {
			if (!cayleyGraph.getCayleyGraphArray()[vertexID].getEdge(tree[i][pointerArray[i] - 1]).getColor().equals(color)) {
				return false;
			}
		}
		return true;
	}
}
