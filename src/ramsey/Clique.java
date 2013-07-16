package ramsey;

/**
 * This graph represents a clique or complete subgraph identified within a given
 * CayleyGraph. This complete subgraph indicates we cannot raise the lower bound
 * for the Ramsey Number we are searching for. Said another way the given
 * CayleyGraph is not a valid counter example.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class Clique {

	Vertex[] cliqueVertexArray;

	/**
	 * This is the main constructor class for the Clique. It will initialize the
	 * vertex array representing the clique with the size being defined by the
	 * input.
	 * 
	 * @param cliqueSize This is the size of the clique.
	 * @return void
	 */
	public Clique(int cliqueSize) {
		this.cliqueVertexArray = new Vertex[cliqueSize];
	}

	/**
	 * This will update Clique objects Vertex array to represent the identified
	 * subgraph.
	 * 
	 * @param vertices This is the Vertex array of all vertices in the
	 *        identified complete subgraph.
	 * @return void
	 */
	public void updateClique(Vertex[] vertices) {
		this.cliqueVertexArray = vertices;
	}

	/**
	 * This will return the color of the Clique.
	 * 
	 * @return A String value representing the color of this Clique.
	 */
	public String getColor() {
		return this.cliqueVertexArray[0].getEdge(this.cliqueVertexArray[1]).getColor();
	}

	/**
	 * This will return the size of the Clique.
	 * 
	 * @return Integer representing the number of elements in this Clique.
	 */
	public int getCliqueSize() {
		return this.cliqueVertexArray.length;
	}

	/**
	 * This will return a vertex from within the Clique based on its position in
	 * the Vertex array.
	 * 
	 * @param vertexArrayPosition Integer value of the position of the Vertex
	 *        within the Clique's Vertex array.
	 * @return Vertex from the local Vertex array at the position defined by
	 *         input to this method.
	 */
	public Vertex getCliqueVertexByPosition(int vertexArrayPosition) {
		return this.cliqueVertexArray[vertexArrayPosition];
	}

	/**
	 * This will return a vertex from within the Clique based on the ID of the
	 * Vertex.
	 * 
	 * @param Integer value of the ID expected on the Vertex in the Clique to be
	 *        returned.
	 * @return Vertex from the Clique with the Vertex ID specified in the input.
	 *         *
	 */
	public Vertex getCliqueVertexById(int vertexId) {
		for (int i = 0; i < this.getCliqueSize(); i++) {
			if (this.getCliqueVertexByPosition(i).getId() == vertexId) {
				return this.cliqueVertexArray[i];
			}
		}
		return null;
	}

	/**
	 * This will output a String representing this Clique in human readable
	 * form.
	 * 
	 * @return String representing this Clique in human readable form.
	 */
	public String printClique() {
		String output = "[";
		for (int i = 0; i < this.getCliqueSize(); i++) {
			output += this.cliqueVertexArray[i].getId() + "/";
		}
		output = output.substring(0, output.length() - 1);
		output += "]";

		return output;
	}

}
