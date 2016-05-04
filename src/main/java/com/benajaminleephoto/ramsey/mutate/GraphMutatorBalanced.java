package com.benajaminleephoto.ramsey.mutate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benajaminleephoto.ramsey.common.CayleyGraph;
import com.benajaminleephoto.ramsey.common.Clique;
import com.benajaminleephoto.ramsey.common.Edge;

public class GraphMutatorBalanced implements GraphMutator {

    private CayleyGraph cayleyGraph;
    private Edge cliqueEdge;
    private Edge nonCliqueEdge;
    private String nonCliqueColor;
    private static final Logger logger = LoggerFactory.getLogger(GraphMutatorBalanced.class.getName());


    public GraphMutatorBalanced(CayleyGraph cayleyGraph) {
        logger.info("Initializing GraphMutatorBalanced");
        this.cayleyGraph = cayleyGraph;
    }


    public void mutateGraph() {
        logger.info("Mutating Balanced");
        mutateGraphBalanced(cayleyGraph.getCliqueCollection().getRandomClique());
    }


    public void mutateGraphAll() {

        for (Clique clique : cayleyGraph.getCliqueCollection().getCliqueList()) {
            if (clique.validateClique()) {
                mutateGraphBalanced(clique);
            }

        }
    }


    /**
     * This will flip the color of one edge from the identified clique along with one other edge of
     * opposite color logically selected based off of color distribution.
     * 
     * @param clique The clique to be mutated.
     */
    private void mutateGraphBalanced(Clique clique) {

        logger.debug("Beginning mutateGraphBalanced method. Clique Color is {}", clique.getColor());

        if (clique.getColor().equals("BLUE")) {
            nonCliqueColor = "RED";
        } else {
            nonCliqueColor = "BLUE";
        }
        cliqueEdge = EdgeRanker.getCliqueEdgeOfHighestRank(clique);
        logger.debug("Clique Edge is : {}", cliqueEdge.printEdge());
        // nonCliqueEdge = EdgeRanker.getCayleyGraphEdgeOfHighestRank(cayleyGraph, nonCliqueColor);
        nonCliqueEdge = cayleyGraph.getRandomEdge(nonCliqueColor);
        logger.debug("Non-Clique Edge is : {}", nonCliqueEdge.printEdge());

        logger.debug("Flipping edge colors");
        cliqueEdge.flipColor();
        nonCliqueEdge.flipColor();

        logger.debug("Clearing clique collection post mutation.");
        cayleyGraph.getCliqueCollection().clear();

        logger.debug("Edges Flipped, exiting mutateGraphBalanced");

    }

}
