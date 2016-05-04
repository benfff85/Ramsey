package com.benajaminleephoto.ramsey.mutate;

import com.benajaminleephoto.ramsey.common.CayleyGraph;
import com.benajaminleephoto.ramsey.common.Edge;

public class GraphMutatorRandom implements GraphMutator {

    private CayleyGraph cayleyGraph;
    Edge redEdge;
    Edge blueEdge;


    public GraphMutatorRandom(CayleyGraph cayleyGraph) {
        this.cayleyGraph = cayleyGraph;
    }


    public void mutateGraph() {
        System.out.println("Mutating Random");
        mutateGraphRandom();
    }


    /**
     * Randomly flip one pair of edges, one edge of each color per pair to maintain balance.
     */
    private void mutateGraphRandom() {
        redEdge = cayleyGraph.getRandomEdge("RED");
        blueEdge = cayleyGraph.getRandomEdge("BLUE");

        redEdge.flipColor();
        blueEdge.flipColor();

        cayleyGraph.getCliqueCollection().clear();
    }

}
