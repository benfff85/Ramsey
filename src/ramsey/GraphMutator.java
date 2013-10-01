package ramsey;

import java.util.Random;

public class GraphMutator {
	
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

		redEdge.setColor("BLUE");
		blueEdge.setColor("RED");
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

		redEdge.setColor("BLUE");
		blueEdge.setColor("RED");

	}
}
