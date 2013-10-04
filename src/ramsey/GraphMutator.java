package ramsey;

import java.util.ArrayList;
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
	 * with one other edge of opposite color logically selected based off of
	 * color distribution.
	 * 
	 * @return void
	 */
	private void mutateGraphBalanced() {
		Edge cliqueEdge = null;
		Edge nonCliqueEdge = null;
		String cliqueColor = this.cayleyGraph.getClique().getColor();
		String nonCliqueColor;
		ArrayList<Vertex> vertexListA = new ArrayList<Vertex>();
		ArrayList<Vertex> vertexListB = new ArrayList<Vertex>();
		int connectedEdgeCountA = 0;
		int connectedEdgeCountB = 0;
		VertexPair vertexPair;
		Vertex vertexA = null;
		Vertex vertexB = null;
		
		// Select the edge from the clique which is attached to the two vertices
		// with the greatest count of edges matching the color of the clique
		for (int i = 0; i < config.CLIQUE_SIZE; i++) {
			if (this.cayleyGraph.getClique().getCliqueVertexByPosition(i).getEdgeCount(cliqueColor) > connectedEdgeCountA) {
				vertexListB.clear();
				vertexListB = (ArrayList<Vertex>) vertexListA.clone();
				connectedEdgeCountA = this.cayleyGraph.getClique().getCliqueVertexByPosition(i).getEdgeCount(cliqueColor);
				vertexListA.clear();
				vertexListA.add(this.cayleyGraph.getClique().getCliqueVertexByPosition(i));
			} else if (this.cayleyGraph.getClique().getCliqueVertexByPosition(i).getEdgeCount(cliqueColor) > connectedEdgeCountB) {
				vertexListB.clear();
				connectedEdgeCountB = this.cayleyGraph.getClique().getCliqueVertexByPosition(i).getEdgeCount(cliqueColor);
				vertexListB.add(this.cayleyGraph.getClique().getCliqueVertexByPosition(i));
			} else if (this.cayleyGraph.getClique().getCliqueVertexByPosition(i).getEdgeCount(cliqueColor) == connectedEdgeCountB) {
				vertexListB.add(this.cayleyGraph.getClique().getCliqueVertexByPosition(i));
			}
		}

		
		vertexPair = getRandomVertices(vertexListA, vertexListB);
		cliqueEdge = cayleyGraph.getEdgeByVertices(vertexPair.vertexA, vertexPair.vertexB);

		// Clear variables
		vertexListA.clear();
		vertexListB.clear();
		connectedEdgeCountA = 0;
		connectedEdgeCountB = 0;

		// Select the edge in the graph with the most edges connected with the
		// color opposite the clique
		if (cliqueColor.equals("BLUE")) {
			nonCliqueColor = "RED";
		} else {
			nonCliqueColor = "BLUE";
		}

		for (int i = 0; i < config.NUM_OF_ELEMENTS; i++) {
			if (this.cayleyGraph.getCayleyGraphArray()[i].getEdgeCount(nonCliqueColor) > connectedEdgeCountA) {
				vertexListA.clear();
				connectedEdgeCountA = this.cayleyGraph.getCayleyGraphArray()[i].getEdgeCount(nonCliqueColor);
				vertexListA.add(this.cayleyGraph.getCayleyGraphArray()[i]);
			} else if (this.cayleyGraph.getCayleyGraphArray()[i].getEdgeCount(nonCliqueColor) >= connectedEdgeCountA){
				vertexListA.add(this.cayleyGraph.getCayleyGraphArray()[i]);
			}
		}
		
		vertexA = getRandomVertex(vertexListA);

		for (int i = 0; i < config.NUM_OF_ELEMENTS; i++) {
			if (vertexA.getEdge(i).getColor().equals(nonCliqueColor)) {
				if (vertexA.getEdge(i).getOtherVertex(vertexA).getEdgeCount(nonCliqueColor) > connectedEdgeCountB) {
					vertexListB.clear();
					connectedEdgeCountB = vertexA.getEdge(i).getOtherVertex(vertexA).getEdgeCount(nonCliqueColor);
					vertexListB.add(vertexA.getEdge(i).getOtherVertex(vertexA));
				} else if (vertexA.getEdge(i).getOtherVertex(vertexA).getEdgeCount(nonCliqueColor) == connectedEdgeCountB) {
					vertexListB.add(vertexA.getEdge(i).getOtherVertex(vertexA));
				}
			}
		}
			
		vertexB = getRandomVertex(vertexListB);

		nonCliqueEdge = cayleyGraph.getEdgeByVertices(vertexA, vertexB);

		cliqueEdge.flipColor();
		nonCliqueEdge.flipColor();
	}
	
		
		
	private VertexPair getRandomVertices(ArrayList<Vertex> vertexListA, ArrayList<Vertex> vertexListB){
		Vertex vertexA;
		Vertex vertexB;		
		
		vertexA = getRandomVertex(vertexListA);
		if (vertexListA.size() > 1){
			vertexB = getRandomVertex(vertexListA);
		} else {
			vertexB = getRandomVertex(vertexListB);
		}
		
		return new VertexPair(vertexA,vertexB); 
	}
	
	private Vertex getRandomVertex(ArrayList<Vertex> vertexList){
		Random generator = new Random();
		Vertex vertex;
		int index;
		
		index = generator.nextInt(vertexList.size());
		vertex = vertexList.get(index);
		vertexList.remove(index);
		return vertex; 
	}
	
	
	class VertexPair{
		public Vertex vertexA;
		public Vertex vertexB;
				
		VertexPair(Vertex vertexA, Vertex vertexB){
			this.vertexA = vertexA;
			this.vertexB = vertexB;
		}
	} 
}
