package com.benajaminleephoto.ramsey.mutate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benajaminleephoto.ramsey.common.ApplicationContext;
import com.benajaminleephoto.ramsey.common.CayleyGraph;
import com.benajaminleephoto.ramsey.common.Edge;

public class GraphMutatorRandom implements GraphMutator {

    private CayleyGraph cayleyGraph;
    private Edge redEdge;
    private Edge blueEdge;
    private static final Logger logger = LoggerFactory.getLogger(GraphMutatorRandom.class.getName());


    public GraphMutatorRandom() {
        logger.info("Initializing GraphMutatorRandom");
        this.cayleyGraph = ApplicationContext.getCayleyGraph();
    }


    public void mutateGraph() {
        logger.info("Mutating Random");
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
