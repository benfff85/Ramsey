package com.benajaminleephoto.ramsey.mutate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benajaminleephoto.ramsey.common.CayleyGraph;
import com.benajaminleephoto.ramsey.common.Config;

public class GraphMutatorFactory {

    private GraphMutatorBalanced graphMutatorBalanced;
    private GraphMutatorTargeted graphMutatorTargeted;
    private GraphMutatorRandom graphMutatorRandom;
    private GraphMutator graphMutator;
    private static final Logger logger = LoggerFactory.getLogger(GraphMutatorFactory.class.getName());

    private int count;


    public GraphMutatorFactory(CayleyGraph cayleyGraph) {
        logger.info("Beginning GraphMutatorFactory initialization");
        graphMutatorBalanced = new GraphMutatorBalanced(cayleyGraph);
        graphMutatorTargeted = new GraphMutatorTargeted(cayleyGraph);
        graphMutatorRandom = new GraphMutatorRandom(cayleyGraph);
        logger.info("GraphMutatorFactory initialization successful.");
    }


    /**
     * This will return the appropriate GraphMutator based on which MUTATION_TYPE is needed for this
     * particular mutation.
     * 
     * @return GraphMutator The GraphMutator to be used.
     */
    public GraphMutator getGraphMutator() {
        MUTATION_TYPE mutateType = selectMutationType();
        if (mutateType == MUTATION_TYPE.RANDOM) {
            graphMutator = graphMutatorRandom;
        } else if (mutateType == MUTATION_TYPE.TARGETED) {
            graphMutator = graphMutatorTargeted;
        } else if (mutateType == MUTATION_TYPE.BALANCED) {
            graphMutator = graphMutatorBalanced;
        }
        return graphMutator;
    }


    /**
     * This will return the MUTATION_TYPE for this particular mutation. This allows both primary and
     * secondary mutation types to be leveraged.
     * 
     * @return The MUTATION_TYPE to use for this mutation.
     */
    private MUTATION_TYPE selectMutationType() {
        count++;
        if (count % (Config.MUTATE_INTERVAL + 1) == 0) {
            count = 0;
            return Config.MUTATE_METHOD_SECONDARY;
        }
        return Config.MUTATE_METHOD_PRIMARY;
    }

}