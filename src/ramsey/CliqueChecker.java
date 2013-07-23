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
		this.tree = new Vertex[cliqueSize][numOfElements];
		this.pointerArray = new int[cliqueSize];
		this.pointerArrayIndex = 0;
		this.cliqueVertexArray = new Vertex[cliqueSize];
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
		Vertex[] cayleyGraphArray = cg.getCayleyGraphArray();

		// Fill first level of tree with all vertices
		for (int i = 0; i < cg.getNumOfElements(); i++) {
			tree[pointerArrayIndex][i] = cayleyGraphArray[i];
		}

		// Main Algorithm
		while (pointerArray[0] <= ((cg.getNumOfElements() - cg.getClique().getCliqueSize()) + 1)) {
			prevLevelVertex = tree[pointerArrayIndex][pointerArray[pointerArrayIndex]];
			pointerArray[pointerArrayIndex]++;
			pointerArrayIndex++;

			for (int i = prevLevelVertex.getId() + 1; i < cg.getNumOfElements(); i++) {
				if (cayleyGraphArray[i].getEdge(prevLevelVertex).getColor() == color && isInPrevLevel(cayleyGraphArray[i], tree[pointerArrayIndex - 1])) {
					tree[pointerArrayIndex][pointerArray[pointerArrayIndex]] = cayleyGraphArray[i];
					pointerArray[pointerArrayIndex]++;
				}
			}

			// If at least (this.cliqueSize-Level) elements, then successful row, reset pointer for row and move to next level
			if (pointerArray[pointerArrayIndex] >= (cg.getClique().getCliqueSize() - pointerArrayIndex)) {
				if (pointerArrayIndex == (cg.getClique().getCliqueSize() - 1)) {
					// Store the found clique into the global variable
					for (int x = 0; x < cg.getClique().getCliqueSize(); x++) {
						cliqueVertexArray[x] = tree[x][pointerArray[x] - 1];
					}
					cg.getClique().updateClique(cliqueVertexArray);
					clearGlobalVars();
					return true;
				}
				pointerArray[pointerArrayIndex] = 0;
			}

			// Otherwise there are not enough elements for a clique, delete row and slide pointer for previous level up one repeat until link-to-be is non-negative one
			else {
				for (int i = 0; i < cg.getNumOfElements() && tree[pointerArrayIndex][i] != null; i++) {
					tree[pointerArrayIndex][i] = null;
				}
				pointerArray[pointerArrayIndex] = 0;
				pointerArrayIndex--;

				while (tree[pointerArrayIndex][pointerArray[pointerArrayIndex]] == null) {
					for (int i = 0; i < cg.getNumOfElements() && tree[pointerArrayIndex][i] != null; i++) {
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
		for (int i = 0; i < pointerArray.length; i++) {
			pointerArray[i] = 0;
		}
		pointerArrayIndex = 0;
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
}
