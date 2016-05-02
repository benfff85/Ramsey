package com.benajaminleephoto.ramsey.common;

import java.util.ArrayList;
import java.util.Random;

public class GraphMutator {

    private CayleyGraph cayleyGraph;
    int count = 0;


    /**
     * This is the publicly exposed mutation method.
     * 
     * @param cayleyGraph The CayleyGraph object to be mutated.
     */
    public void mutateGraph(CayleyGraph cayleyGraph) {
        this.cayleyGraph = cayleyGraph;
        for (int i = 0; i < Config.MUTATE_COUNT; i++) {
            mutateGraph(selectMutationType());
        }
        cayleyGraph.getCliqueCollection().clear();
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


    /**
     * This will call the appropriate mutation method based on which MUTATION_TYPE is needed for
     * this particular mutation.
     * 
     * @param mutateType The type of mutation to be used this mutation.
     */
    private void mutateGraph(MUTATION_TYPE mutateType) {
        if (mutateType == MUTATION_TYPE.RANDOM) {
            mutateGraphRandom();
        } else if (mutateType == MUTATION_TYPE.TARGETED) {
            mutateGraphTargeted();
        } else if (mutateType == MUTATION_TYPE.BALANCED) {
            mutateGraphBalancedAll();
        }
    }


    /**
     * Randomly flip one pair of edges, one edge of each color per pair to maintain balance.
     */
    private void mutateGraphRandom() {
        Edge redEdge = cayleyGraph.getRandomEdge("RED");
        Edge blueEdge = cayleyGraph.getRandomEdge("BLUE");

        redEdge.flipColor();
        blueEdge.flipColor();
    }


    /**
     * This will flip the color of one edge from the identified clique along with one random edge of
     * the opposite color to maintain balance.
     */
    private void mutateGraphTargeted() {
        Random generator = new Random();
        boolean redSelected = false;
        boolean blueSelected = false;
        Edge redEdge = null;
        Edge blueEdge = null;
        int x, y;

        Debug.write("Beginning mutateGraphTargeted method. Clique Color is " + cayleyGraph.getClique().getColor());

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
            Debug.write("Clique edge selected [" + redEdge.getVertexA().getId() + "," + redEdge.getVertexB().getId() + ",RED].");
        } else {
            blueEdge = cayleyGraph.getClique().getCliqueVertexByPosition(x).getEdge(cayleyGraph.getClique().getCliqueVertexByPosition(y));
            blueSelected = true;
            Debug.write("Clique edge selected [" + blueEdge.getVertexA().getId() + "," + blueEdge.getVertexB().getId() + ",BLUE].");
        }

        // Select the edge of opposite color to swap as well
        if (redSelected) {
            blueEdge = cayleyGraph.getRandomEdge("BLUE");
            Debug.write("Non-clique edge selected [" + blueEdge.getVertexA().getId() + "," + blueEdge.getVertexB().getId() + ",BLUE].");

        } else if (blueSelected) {
            redEdge = cayleyGraph.getRandomEdge("RED");
            Debug.write("Non-clique edge selected [" + redEdge.getVertexA().getId() + "," + redEdge.getVertexB().getId() + ",RED].");

        }

        redEdge.flipColor();
        blueEdge.flipColor();
        Debug.write("Edges Flipped, exiting mutateGraphTargeted");

    }


    private void mutateGraphBalanced() {
        mutateGraphBalanced(cayleyGraph.getCliqueCollection().getRandomClique());
    }
    /*
     * private void mutateGraphBalancedAll(){ for (int i = 0; i <
     * cayleyGraph.getCliqueCollection().getCliqueCount(); i++){ if
     * (cayleyGraph.getCliqueCollection().getCliqueByIndex(i).validateClique()){
     * mutateGraphBalanced(cayleyGraph.getCliqueCollection().getCliqueByIndex(i)); } } }
     */


    private void mutateGraphBalancedAll() {
        int mutateCnt = 1;

        while (mutateCnt != 0) {
            mutateCnt = 0;
            for (int i = 0; i < cayleyGraph.getCliqueCollection().getCliqueCount(); i++) {
                if (cayleyGraph.getCliqueCollection().getCliqueByIndex(i).validateClique()) {
                    mutateGraphBalanced(cayleyGraph.getCliqueCollection().getCliqueByIndex(i));
                }
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

        Edge cliqueEdge = null;
        Edge nonCliqueEdge = null;
        String cliqueColor = clique.getColor();
        String nonCliqueColor;

        Debug.write("Beginning mutateGraphBalanced method. Clique Color is " + cayleyGraph.getClique().getColor());

        if (cliqueColor.equals("BLUE")) {
            nonCliqueColor = "RED";
        } else {
            nonCliqueColor = "BLUE";
        }
        cliqueEdge = getCliqueEdgeOfHighestRank(clique);
        // nonCliqueEdge = getCayleyGraphEdgeOfHighestRank(cayleyGraph, nonCliqueColor);
        nonCliqueEdge = cayleyGraph.getRandomEdge(nonCliqueColor);

        cliqueEdge.flipColor();
        nonCliqueEdge.flipColor();
        Debug.write("Edges Flipped, exiting mutateGraphBalanced");

    }


    @SuppressWarnings("unchecked")
    private Edge getCliqueEdgeOfHighestRank(Clique clique) {
        ArrayList<Vertex> vertexListA = new ArrayList<Vertex>();
        ArrayList<Vertex> vertexListB = new ArrayList<Vertex>();
        int connectedEdgeCountA = 0;
        int connectedEdgeCountB = 0;
        VertexPair vertexPair;
        Edge cliqueEdge;
        String cliqueColor;

        cliqueColor = clique.getColor();

        // Select the edge from the clique which is attached to the two vertices
        // with the greatest count of edges matching the color of the clique
        for (int i = 0; i < clique.getCliqueSize(); i++) {
            if (clique.getCliqueVertexByPosition(i).getEdgeCount(cliqueColor) > connectedEdgeCountA) {
                vertexListB.clear();
                vertexListB = (ArrayList<Vertex>) vertexListA.clone();
                connectedEdgeCountA = clique.getCliqueVertexByPosition(i).getEdgeCount(cliqueColor);
                vertexListA.clear();
                vertexListA.add(clique.getCliqueVertexByPosition(i));
            } else if (clique.getCliqueVertexByPosition(i).getEdgeCount(cliqueColor) > connectedEdgeCountB) {
                vertexListB.clear();
                connectedEdgeCountB = clique.getCliqueVertexByPosition(i).getEdgeCount(cliqueColor);
                vertexListB.add(clique.getCliqueVertexByPosition(i));
            } else if (clique.getCliqueVertexByPosition(i).getEdgeCount(cliqueColor) == connectedEdgeCountB) {
                vertexListB.add(clique.getCliqueVertexByPosition(i));
            }
        }

        vertexPair = getRandomVertices(vertexListA, vertexListB);
        cliqueEdge = cayleyGraph.getEdgeByVertices(vertexPair.vertexA, vertexPair.vertexB);
        Debug.write("Clique edge selected [" + cliqueEdge.getVertexA().getId() + "," + cliqueEdge.getVertexB().getId() + "," + cliqueEdge.getColor() + "].");

        vertexListA.clear();
        vertexListB.clear();

        return cliqueEdge;
    }


    @SuppressWarnings("unused")
    private Edge getCayleyGraphEdgeOfHighestRank(CayleyGraph cayleyGraph, String color) {
        ArrayList<Vertex> vertexListA = new ArrayList<Vertex>();
        ArrayList<Vertex> vertexListB = new ArrayList<Vertex>();
        int connectedEdgeCountA = 0;
        int connectedEdgeCountB = 0;
        Vertex vertexA = null;
        Vertex vertexB = null;
        Edge nonCliqueEdge;

        for (int i = 0; i < Config.NUM_OF_ELEMENTS; i++) {
            if (cayleyGraph.getVertexById(i).getEdgeCount(color) > connectedEdgeCountA) {
                vertexListA.clear();
                connectedEdgeCountA = cayleyGraph.getVertexById(i).getEdgeCount(color);
                vertexListA.add(cayleyGraph.getVertexById(i));
            } else if (cayleyGraph.getVertexById(i).getEdgeCount(color) >= connectedEdgeCountA) {
                vertexListA.add(cayleyGraph.getVertexById(i));
            }
        }

        vertexA = getRandomVertex(vertexListA);

        for (int i = 0; i < Config.NUM_OF_ELEMENTS; i++) {
            if (vertexA.getEdge(i).getColor().equals(color)) {
                if (vertexA.getEdge(i).getOtherVertex(vertexA).getEdgeCount(color) > connectedEdgeCountB) {
                    vertexListB.clear();
                    connectedEdgeCountB = vertexA.getEdge(i).getOtherVertex(vertexA).getEdgeCount(color);
                    vertexListB.add(vertexA.getEdge(i).getOtherVertex(vertexA));
                } else if (vertexA.getEdge(i).getOtherVertex(vertexA).getEdgeCount(color) == connectedEdgeCountB) {
                    vertexListB.add(vertexA.getEdge(i).getOtherVertex(vertexA));
                }
            }
        }
        vertexB = getRandomVertex(vertexListB);

        nonCliqueEdge = cayleyGraph.getEdgeByVertices(vertexA, vertexB);
        Debug.write("Non-clique edge selected [" + nonCliqueEdge.getVertexA().getId() + "," + nonCliqueEdge.getVertexB().getId() + "," + nonCliqueEdge.getColor() + "].");

        vertexListA.clear();
        vertexListB.clear();

        return nonCliqueEdge;
    }


    private VertexPair getRandomVertices(ArrayList<Vertex> vertexListA, ArrayList<Vertex> vertexListB) {
        Vertex vertexA;
        Vertex vertexB;

        vertexA = getRandomVertex(vertexListA);
        if (vertexListA.size() > 1) {
            vertexB = getRandomVertex(vertexListA);
        } else {
            vertexB = getRandomVertex(vertexListB);
        }

        return new VertexPair(vertexA, vertexB);
    }


    /**
     * This function uses a random number generator to return a random vertex from an input Vertex
     * ArrayList.
     * 
     * @param vertexList Vertex ArrayList from which a vertex will be returned at random.
     * @return A random Vertex from the input Vertex ArrayList.
     */
    private Vertex getRandomVertex(ArrayList<Vertex> vertexList) {
        Random generator = new Random();
        Vertex vertex;
        int index;

        index = generator.nextInt(vertexList.size());
        vertex = vertexList.get(index);
        vertexList.remove(index);
        return vertex;
    }

    class VertexPair {
        public Vertex vertexA;
        public Vertex vertexB;


        VertexPair(Vertex vertexA, Vertex vertexB) {
            this.vertexA = vertexA;
            this.vertexB = vertexB;
        }
    }
}
