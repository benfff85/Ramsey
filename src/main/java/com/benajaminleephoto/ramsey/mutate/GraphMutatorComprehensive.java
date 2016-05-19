package com.benajaminleephoto.ramsey.mutate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benajaminleephoto.ramsey.common.ApplicationContext;
import com.benajaminleephoto.ramsey.common.CayleyGraph;
import com.benajaminleephoto.ramsey.common.Config;
import com.benajaminleephoto.ramsey.common.Edge;

public class GraphMutatorComprehensive implements GraphMutator {

    private CayleyGraph cayleyGraph;
    private Edge redEdge;
    private Edge blueEdge;
    private static final Logger logger = LoggerFactory.getLogger(GraphMutatorComprehensive.class.getName());


    public GraphMutatorComprehensive() {
        logger.info("Initializing GraphMutatorComprehensive");
        this.cayleyGraph = ApplicationContext.getCayleyGraph();
    }


    public void mutateGraph() {
        logger.info("Mutating Comprehensive");
        mutateGraphComprehensive();
    }


    /**
     * This will flip the color of one edge from the identified clique along with one other edge of
     * opposite color logically selected based off of color distribution.
     * 
     * @param clique The clique to be mutated.
     */
    private void mutateGraphComprehensive() {

        logger.debug("Beginning mutateGraphComprehensive method");

        redEdge = cayleyGraph.getCliqueCollection().getMostCommonEdge("RED", Config.MUTATE_COMPREHENSIVE_EDGE_RANGE);
        blueEdge = cayleyGraph.getCliqueCollection().getMostCommonEdge("BLUE", Config.MUTATE_COMPREHENSIVE_EDGE_RANGE);

        logger.debug("Red Edge is : {}", redEdge.printEdge());
        logger.debug("Blue Edge is : {}", blueEdge.printEdge());

        logger.debug("Flipping edge colors");
        redEdge.flipColor();
        blueEdge.flipColor();

        logger.debug("Clearing clique collection post mutation.");
        cayleyGraph.getCliqueCollection().clear();

        logger.debug("Edges Flipped, exiting mutateGraphComprehensive");

    }

}
