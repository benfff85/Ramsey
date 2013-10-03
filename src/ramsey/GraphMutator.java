package ramsey;

import java.util.Random;

public class GraphMutator {
	
	private Config config = new Config();
	private CayleyGraph cayleyGraph;
	
	/**
	 * Randomly flip countOfSwaps pairs of edges, one edge of each color per
	 * pair to maintain balance.
	 * 
	 * @return void
	 */
	public void mutateGraph(MUTATION_TYPE mutateType, CayleyGraph cayleyGraph, int countOfSwaps) {
		this.cayleyGraph = cayleyGraph; 
		for (int i = 0; i < countOfSwaps; i++) {
			mutateGraph(mutateType);
		}
		cayleyGraph.clearClique();
	}

	/**
	 * 
	 * @param mutateType
	 */
	public void mutateGraph(MUTATION_TYPE mutateType){
		
		if(mutateType == MUTATION_TYPE.RANDOM){
			mutateGraphRandom();
		} else if(mutateType == MUTATION_TYPE.TARGETED){
			mutateGraphTargeted();
		} else if(mutateType == MUTATION_TYPE.BALANCED){
			mutateGraphBalanced();
		}	
	}
	
	/**
	 * Randomly flip one pair of edges, one edge of each color per
	 * pair to maintain balance.
	 * 
	 * @return void
	 */
	private void mutateGraphRandom() {
		Edge redEdge = cayleyGraph.getRandomEdge("RED");
		Edge blueEdge = cayleyGraph.getRandomEdge("BLUE");

		redEdge.flipColor();
		blueEdge.flipColor();
	}
	
	/**
	 * This will flip the color of one edge from the identified clique along
	 * with one random edge of the opposite color to maintain balance.
	 * 
	 * @return void
	 */
	private void mutateGraphTargeted() {
		Random generator = new Random();
		boolean redSelected = false;
		boolean blueSelected = false;
		Edge redEdge = null;
		Edge blueEdge = null;
		int x, y;

		// Select the edge from clique to swap
		x = 0;
		y = 0;
		while (x == y) {
			x = generator.nextInt(cayleyGraph.getClique().getCliqueSize());
			y = generator.nextInt(cayleyGraph.getClique().getCliqueSize());
		}

		if (cayleyGraph.getClique().getCliqueVertexByPosition(x).getEdge(cayleyGraph.getClique().getCliqueVertexByPosition(y)).getColor() == "RED") {
			redEdge = cayleyGraph.getClique().getCliqueVertexByPosition(x).getEdge(cayleyGraph.getClique().getCliqueVertexByPosition(y));
			redSelected = true;
		}
		else {
			blueEdge = cayleyGraph.getClique().getCliqueVertexByPosition(x).getEdge(cayleyGraph.getClique().getCliqueVertexByPosition(y));
			blueSelected = true;
		}

		// Select the edge of opposite color to swap as well
		if (redSelected) {
			blueEdge = cayleyGraph.getRandomEdge("BLUE");
		}
		else if (blueSelected) {
			redEdge = cayleyGraph.getRandomEdge("RED");
		}

		redEdge.flipColor();
		blueEdge.flipColor();
	}
	
	/**
	 * This will flip the color of one edge from the identified clique along
	 * with one other edge of opposite color logically selected based off of color distribution.
	 * 
	 * @return void
	 */
	private void mutateGraphBalanced() {
		Edge cliqueEdge = null;
		Edge nonCliqueEdge = null;

		// Select the edge from the clique which is attached to the two vertices
		// with the greatest count of edges matching the color of the clique
		String cliqueColor = this.cayleyGraph.getClique().getColor();
		vertexEdgeCountPair vertex1 = new vertexEdgeCountPair(null, 0);
		vertexEdgeCountPair vertex2 = new vertexEdgeCountPair(null, 0);
		for (int i = 0; i < config.CLIQUE_SIZE; i++) {
			if (this.cayleyGraph.getClique().getCliqueVertexByPosition(i).getEdgeCount(cliqueColor) > vertex2.connectedEdges) {
				if (this.cayleyGraph.getClique().getCliqueVertexByPosition(i).getEdgeCount(cliqueColor) > vertex1.connectedEdges) {
					vertex2.connectedEdges = vertex1.connectedEdges;
					vertex2.vertex = vertex1.vertex;
					vertex1.connectedEdges = this.cayleyGraph.getClique().getCliqueVertexByPosition(i).getEdgeCount(cliqueColor);
					vertex1.vertex = this.cayleyGraph.getClique().getCliqueVertexByPosition(i);

				} else {
					vertex2.connectedEdges = this.cayleyGraph.getClique().getCliqueVertexByPosition(i).getEdgeCount(cliqueColor);
					vertex2.vertex = this.cayleyGraph.getClique().getCliqueVertexByPosition(i);
				}
			}
		}

		cliqueEdge = cayleyGraph.getEdgeByVertices(vertex1.vertex, vertex2.vertex);

		// Clear out the vertices
		vertex1.vertex = null;
		vertex1.connectedEdges = 0;
		vertex2.vertex = null;
		vertex2.connectedEdges = 0;

		// Select the edge in the graph with the most edges connected with the
		// color opposite the clique
		String nonCliqueColor;
		if (cliqueColor.equals("BLUE")) {
			nonCliqueColor = "RED";
		} else {
			nonCliqueColor = "BLUE";
		}

		for (int i = 0; i < config.NUM_OF_ELEMENTS; i++) {
			if (this.cayleyGraph.getCayleyGraphArray()[i].getEdgeCount(nonCliqueColor) > vertex1.connectedEdges) {
				vertex1.connectedEdges = this.cayleyGraph.getCayleyGraphArray()[i].getEdgeCount(nonCliqueColor);
				vertex1.vertex = this.cayleyGraph.getCayleyGraphArray()[i];
			}
		}

		for (int i = 0; i < config.NUM_OF_ELEMENTS; i++) {
			if (vertex1.vertex.getEdge(i).getColor().equals(nonCliqueColor) && vertex1.vertex.getEdge(i).getOtherVertex(vertex1.vertex).getEdgeCount(nonCliqueColor) > vertex2.connectedEdges) {
				vertex2.connectedEdges = vertex1.vertex.getEdge(i).getOtherVertex(vertex1.vertex).getEdgeCount(nonCliqueColor);
				vertex2.vertex = vertex1.vertex.getEdge(i).getOtherVertex(vertex1.vertex);
			}
		}
		nonCliqueEdge = cayleyGraph.getEdgeByVertices(vertex1.vertex, vertex2.vertex);
	
		cliqueEdge.flipColor();
		nonCliqueEdge.flipColor();
	}
	
	class vertexEdgeCountPair{
		public Vertex vertex;
		public int connectedEdges;
		
		vertexEdgeCountPair(Vertex vertex, int connectedEdges){
			this.vertex = vertex;
			this.connectedEdges = connectedEdges;
		}
	} 
	
}
