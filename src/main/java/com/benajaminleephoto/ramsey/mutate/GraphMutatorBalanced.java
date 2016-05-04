package com.benajaminleephoto.ramsey.mutate;

import com.benajaminleephoto.ramsey.common.CayleyGraph;
import com.benajaminleephoto.ramsey.common.Clique;
import com.benajaminleephoto.ramsey.common.Debug;
import com.benajaminleephoto.ramsey.common.Edge;

public class GraphMutatorBalanced implements GraphMutator {

    private CayleyGraph cayleyGraph;
    Edge cliqueEdge;
    Edge nonCliqueEdge;
    String nonCliqueColor;


    public GraphMutatorBalanced(CayleyGraph cayleyGraph) {
        this.cayleyGraph = cayleyGraph;
    }


    public void mutateGraph() {
        System.out.println("Mutating Balanced");
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

        Debug.write("Beginning mutateGraphBalanced method. Clique Color is " + clique.getColor());

        if (clique.getColor().equals("BLUE")) {
            nonCliqueColor = "RED";
        } else {
            nonCliqueColor = "BLUE";
        }
        cliqueEdge = EdgeRanker.getCliqueEdgeOfHighestRank(clique);
        // nonCliqueEdge = EdgeRanker.getCayleyGraphEdgeOfHighestRank(cayleyGraph, nonCliqueColor);
        nonCliqueEdge = cayleyGraph.getRandomEdge(nonCliqueColor);

        cliqueEdge.flipColor();
        nonCliqueEdge.flipColor();

        cayleyGraph.getCliqueCollection().clear();

        Debug.write("Edges Flipped, exiting mutateGraphBalanced");

    }

}
