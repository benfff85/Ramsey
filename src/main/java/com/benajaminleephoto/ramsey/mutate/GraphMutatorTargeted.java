package com.benajaminleephoto.ramsey.mutate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benajaminleephoto.ramsey.common.ApplicationContext;
import com.benajaminleephoto.ramsey.common.CayleyGraph;
import com.benajaminleephoto.ramsey.common.Config;
import com.benajaminleephoto.ramsey.common.Debug;
import com.benajaminleephoto.ramsey.common.Edge;
import com.benajaminleephoto.ramsey.common.Vertex;

public class GraphMutatorTargeted implements GraphMutator {

    private CayleyGraph cayleyGraph;
    int vertexIdA, vertexIdB;
    private Vertex vertexA;
    private Vertex vertexB;
    private Edge cliqueEdge;
    private Edge nonCliqueEdge;
    private static final Logger logger = LoggerFactory.getLogger(GraphMutatorTargeted.class.getName());


    public GraphMutatorTargeted() {
        logger.info("Initializing GraphMutatorTargeted");
        this.cayleyGraph = ApplicationContext.getCayleyGraph();
    }


    public void mutateGraph() {
        logger.info("Mutating Targeted");
        mutateGraphTargeted();
    }


    /**
     * This will flip the color of one random edge from the identified clique along with one random
     * edge of the opposite color to maintain balance.
     */
    private void mutateGraphTargeted() {

        Debug.write("Beginning mutateGraphTargeted method. Clique Color is " + cayleyGraph.getClique().getColor());

        // Select the edge from clique to swap
        vertexIdA = 0;
        vertexIdB = 0;

        while (vertexIdA == vertexIdB) {
            vertexIdA = ApplicationContext.getGenerator().nextInt(Config.CLIQUE_SIZE);
            vertexIdB = ApplicationContext.getGenerator().nextInt(Config.CLIQUE_SIZE);
        }

        vertexA = cayleyGraph.getClique().getCliqueVertexByPosition(vertexIdA);
        vertexB = cayleyGraph.getClique().getCliqueVertexByPosition(vertexIdB);
        cliqueEdge = vertexA.getEdge(vertexB);

        if (cliqueEdge.getColor() == "RED") {
            nonCliqueEdge = cayleyGraph.getRandomEdge("BLUE");
        } else {
            nonCliqueEdge = cayleyGraph.getRandomEdge("RED");
        }

        cliqueEdge.flipColor();
        nonCliqueEdge.flipColor();

        cayleyGraph.getCliqueCollection().clear();

        Debug.write("Edges Flipped, exiting mutateGraphTargeted");

    }

}
