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
	private Config config = new Config();
	private Vertex[] cayleyGraphArray;
	private String color;
	/**
	 * This is the main constructor for the CliqueChecker class. It will
	 * initialize the necessary global variables. Variables will be sized
	 * correctly depending on the input which dictates the number of elements in
	 * the CayleyGraph and the number of elements in the Clique we will be
	 * searching for.
	 * 
	 * @param cliqueSize The number of elements in the Clique we will be
	 *        searching for.
	 * @param numOfElements The number of elements in the CayleyGraph.
	 */
	public CliqueChecker(int cliqueSize, int numOfElements) {
		this.tree = new Vertex[config.CLIQUE_SIZE][config.NUM_OF_ELEMENTS];
		this.pointerArray = new int[config.CLIQUE_SIZE];
		this.pointerArrayIndex = 0;
		this.cliqueVertexArray = new Vertex[config.CLIQUE_SIZE];
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
	public boolean findClique(CayleyGraph cg, String color) {
		this.cayleyGraphArray = cg.getCayleyGraphArray();
		this.color = color;

		// Fill first level of tree with all vertices
		for (int i = 0; i < config.NUM_OF_ELEMENTS; i++) {
			tree[pointerArrayIndex][i] = cayleyGraphArray[i];
		}

		// Main Algorithm
		while (pointerArray[0] <= ((config.NUM_OF_ELEMENTS - config.CLIQUE_SIZE) + 1)) {
			prevLevelVertex = tree[pointerArrayIndex][pointerArray[pointerArrayIndex]];
			pointerArray[pointerArrayIndex]++;
			pointerArrayIndex++;

			// Fill tree level with all vertices connected to the previous levels vertex with an edge of the specified color
			for (int i = prevLevelVertex.getId() + 1; i < config.NUM_OF_ELEMENTS; i++) {
				//if (cayleyGraphArray[i].getEdge(prevLevelVertex).getColor().equals(color) && isInPrevLevel(cayleyGraphArray[i], tree[pointerArrayIndex - 1]) && isConnected(i)) {
				if (isConnected(i)) {
					tree[pointerArrayIndex][pointerArray[pointerArrayIndex]] = cayleyGraphArray[i];
					pointerArray[pointerArrayIndex]++;
				}
			}

			// If at least (this.cliqueSize-Level) elements, then successful row, reset pointer for row and move to next level
			if (pointerArray[pointerArrayIndex] >= (config.CLIQUE_SIZE - pointerArrayIndex)) {
				if (pointerArrayIndex == (config.CLIQUE_SIZE - 1)) {
					// Store the found clique into the global variable
					for (int x = 0; x < config.CLIQUE_SIZE; x++) {
						cliqueVertexArray[x] = tree[x][pointerArray[x] - 1];
					}
					cg.getClique().updateClique(cliqueVertexArray.clone());
					clearGlobalVars();
					return true;
				}
				pointerArray[pointerArrayIndex] = 0;
			}

			// Otherwise there are not enough elements for a clique, delete row and slide pointer for previous level up one repeat until link-to-be is non-negative one
			else {
				for (int i = 0; i < config.NUM_OF_ELEMENTS && tree[pointerArrayIndex][i] != null; i++) {
					tree[pointerArrayIndex][i] = null;
				}
				pointerArray[pointerArrayIndex] = 0;
				pointerArrayIndex--;

				while (tree[pointerArrayIndex][pointerArray[pointerArrayIndex]] == null) {
					for (int i = 0; i < config.NUM_OF_ELEMENTS && tree[pointerArrayIndex][i] != null; i++) {
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
		for (int i = 0; i < config.CLIQUE_SIZE; i++) {
			pointerArray[i] = 0;
		}
		pointerArrayIndex = 0;
		
		for (int i=0; i<config.CLIQUE_SIZE; i++){
			for (int j=0; j<config.NUM_OF_ELEMENTS; j++){
				tree[i][j] = null;
			}
		}
		
		prevLevelVertex = null;
		
		for (int i=0; i<config.CLIQUE_SIZE; i++){
			cliqueVertexArray[i] = null;
		}
		
		cayleyGraphArray = null;
		
		color = null;
	}

	/**
	 * This is a helper method for the cliqueChecker. This will determine if the
	 * previous level of the tree also contained the element in question which
	 * is provided as input.
	 * 
	 * @param vertex Vertex to be checked to see if it is in the previous level
	 *        of the tree.
	 * @param level The previous level of the tree. This is the level which will
	 *        be searched for the input element.
	 * @return Boolean value which will be true if the previous level does
	 *         contain the input element and false otherwise.
	 */
	private boolean isInPrevLevel(Vertex vertex, Vertex[] level) {
		for (int i = 0; i < tree[0].length && level[i] != null && level[i].getId() <= vertex.getId(); i++) {
			if (level[i] == vertex) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks for a given input vertexID if it is connected to the elements in
	 * all previous levels of the tree with an edge of specified color.
	 * 
	 * @param vertexID Vertex ID to be checked for connectivity.
	 * @return True if connected to all previous elements in the potential
	 *         clique with edges of the specified color, otherwise false.
	 */
	private boolean isConnected(int vertexID) {
		for (int i = 0; i < pointerArrayIndex; i++) {
			if (!cayleyGraphArray[vertexID].getEdge(tree[i][pointerArray[i] - 1]).getColor().equals(this.color)) {
				return false;
			}
		}
		return true;
	}
}
