package com.benajaminleephoto.ramsey.common;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the cayley graph which is a graph comprised of getNumOfElements() elements.
 * Each elements (also referred to as vertex) is connected to every other element via an edge. The
 * CayleyGraph will be able to initialize itself, check itself for complete subgraphs, mutate itself
 * and write itself to files which can be loaded later.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class CayleyGraph implements java.io.Serializable {

    private static final long serialVersionUID = 2142391538509023095L;
    private int numOfElements;
    private Vertex[] cayleyGraphArray;
    private CliqueCollection cliqueCollection;
    private static final Logger logger = LoggerFactory.getLogger(CayleyGraph.class.getName());


    /**
     * This is the main constructor for the CayleyGraph. It will initialize all global variables
     * given.
     * 
     * @throws Exception
     */
    public CayleyGraph() throws Exception {
        numOfElements = Config.NUM_OF_ELEMENTS;
        cayleyGraphArray = new Vertex[getNumOfElements()];
        cliqueCollection = new CliqueCollection();
        initialize();
    }


    public void rollback(String recoveryFile, CliqueCollectionSnapshot cliqueCollectionSnapshot) throws Exception {
        File file = new File(recoveryFile);
        loadFromString(GraphFileReader.getLoaderStringFromFile(file));
        getCliqueCollection().clear();
        Vertex[] vertices;
        logger.debug("Rolling Back");
        for (int i = 0; i < cliqueCollectionSnapshot.getCliqueCount(); i++) {
            vertices = new Vertex[cliqueCollectionSnapshot.getCliqueByPosition(0).length];
            for (int j = 0; j < cliqueCollectionSnapshot.getCliqueByPosition(i).length; j++) {
                vertices[j] = getVertexById(cliqueCollectionSnapshot.getCliqueByPosition(i)[j]);
            }
            try {
                getCliqueCollection().addClique(new Clique(vertices));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.debug("Rollback Complete");
    }


    /**
     * This will return the number of elements comprising this CayleyGraph. Said another way it will
     * return this.cayleyGraphArray.length.
     * 
     * @return The number of elements comprising this CayleyGraph.
     */
    public int getNumOfElements() {
        return numOfElements;
    }


    /**
     * This will return the Vertex array of all vertices in this CayleyGraph.
     * 
     * @return The Vertex array of all vertices in this CayleyGraph.
     */
    public Vertex[] getCayleyGraphArray() {
        return cayleyGraphArray;
    }


    /**
     * This will initialize the vertices and edges of the CayleyGraph based on the LAUNCH_METHOD
     * specified in the Config class.
     * 
     * @throws Exception
     */
    public void initialize() throws Exception {
        String loaderString = "";

        initializeVertices();

        if (Config.LAUNCH_METHOD == LAUNCH_TYPE.GENERATE_RANDOM) {
            loaderString = generateRandomLoaderString();
        } else if (Config.LAUNCH_METHOD == LAUNCH_TYPE.OPEN_FROM_FILE) {
            loaderString = GraphFileReader.getLoaderStringFromFileInteractive();
        }

        loadFromString(loaderString);
    }


    /**
     * This will Generate a random CayleyGraph where the total number of edges of each color will be
     * equal. This is generally only done once at the beginning of processing after which the
     * initialized graph will simply be mutated.
     */
    public String generateRandomLoaderString() {
        int redCount = getEdgeCount() / 2;
        StringBuilder loaderString = new StringBuilder();

        initializeVertices();

        for (int i = 0; i < getNumOfElements(); i++) {
            for (int j = (i + 1); j < getNumOfElements(); j++) {
                if (redCount > 0) {
                    if (ApplicationContext.getGenerator().nextBoolean() == true) {
                        loaderString.append("1");
                        redCount--;
                    } else {
                        loaderString.append("0");
                    }
                }
            }
        }

        return loaderString.toString();

    }


    /**
     * This will generate and return a String representing the CayleyGraph in a format compatible
     * with Mathematica.
     * 
     * @return String representing the CayleyGraph in a format compatible with Mathematica.
     */
    public String printCayleyGraphMathematica() {
        StringBuilder output = new StringBuilder();
        output.append("GraphPlot[{");

        for (int i = 0; i < getNumOfElements(); i++) {
            output.append("{");
            for (int j = 0; j < getNumOfElements(); j++) {

                if (i != j && cayleyGraphArray[i].getEdge(cayleyGraphArray[j]).getColor() == "RED") {
                    output.append("1, ");
                } else {
                    output.append("0, ");
                }
            }
            output.setLength(output.length() - 2);
            output.append("},");
        }
        output.setLength(output.length() - 1);
        output.append("}, Method -> CircularEmbedding]");
        return output.toString();
    }


    /**
     * This will generate and return a String representing the CayleyGraph in a basic common
     * separated value format.
     * 
     * @return String representing the CayleyGraph in a basic common separated value format.
     */
    public String printCayleyGraphBasic() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < getNumOfElements(); i++) {
            for (int j = 0; j < getNumOfElements(); j++) {

                if (i != j && cayleyGraphArray[i].getEdge(cayleyGraphArray[j]).getColor() == "RED") {
                    output.append("1,");
                } else {
                    output.append("0,");
                }
            }
            output.setLength(output.length() - 1);
            output.append("\n");
        }
        return output.toString();
    }


    /**
     * This will email a representation of the CayleyGraph in a format compatible with Mathematica.
     */
    public void emailCayleyGraph() {
        String[] arguments = new String[2];

        arguments[0] = "Ramsey Solution found to R[" + getNumOfElements() + "," + getNumOfElements() + "]";
        arguments[1] = printCayleyGraphMathematica();

        Email.send(arguments);
    }


    /**
     * This will print the total count of RED and BLUE edges in the CayleyGraph.
     * <p>
     * The formatting of the output will be in the form of:<br>
     * [countRed:countBlue]
     * <p>
     * Note: They should always be equal if algorithms are working as expected.
     * 
     * @return A human readable string string of the total number of Red and Blue Edges in this
     *         CayleyGraph.
     */
    public String printRedBlueCount() {
        int countRed = 0;
        int countBlue = 0;
        for (int i = 0; i < getNumOfElements(); i++) {
            for (int j = i + 1; j < getNumOfElements(); j++) {
                if (cayleyGraphArray[i].getEdge(cayleyGraphArray[j]).getColor().equals("RED")) {
                    countRed++;
                } else {
                    countBlue++;
                }
            }
        }
        return "[RED:" + countRed + "] [BLUE:" + countBlue + "]";
    }


    /**
     * This Will a human readable string representing the color distribution of this CayleyGraph.
     * The color distribution will consist of getNumOfElements() integers each one representing the
     * number of Edges of a given color connected to each Vertex.
     * <p>
     * The formatting of the output will be in the form of:<br>
     * [#,#,#,...,#,#,#]
     * 
     * @param color This is the color of the Edges which will be counted in calculating this
     *        distribution.
     * @return A human readable string representing the color distribution of this CayleyGraph.
     */
    public String printDistribution(String color) {
        int count;
        String output = "[";

        for (int i = 0; i < getNumOfElements(); i++) {
            count = cayleyGraphArray[i].getEdgeCount(color);
            output += count + ",";
        }
        output = output.substring(0, output.length() - 1);
        output += "]";
        return output;
    }


    /**
     * This will return an edge from this CayleyGraph based on the vertex IDs of the two vertices it
     * connects.
     * 
     * @param vertexIdA The ID of a Vertex which is connected to one end point of the desired edge.
     * @param vertexIdB The ID of a Vertex which is connected to the other end point of the desired
     *        edge.
     * @return The Edge object connecting the two vertices having the vertex IDs given as input.
     */
    public Edge getEdgeByVertexIds(int vertexIdA, int vertexIdB) {
        return cayleyGraphArray[vertexIdA].getEdge(vertexIdB);
    }


    /**
     * This will return an edge from this CayleyGraph based on the two vertices it connects.
     * 
     * @param vertexA A Vertex which is connected to one end point of the desired edge.
     * @param vertexB A Vertex which is connected to the other end point of the desired edge.
     * @return The Edge object connecting the two vertices given as input.
     */
    public Edge getEdgeByVertices(Vertex vertexA, Vertex vertexB) {
        return vertexA.getEdge(vertexB);
    }


    /**
     * This will return the Clique identified for this CayleyGraph. This method serves as a wrapper,
     * when no clique to be returned is specified it will return the first Clique in the
     * CliqueCollection.
     * 
     * @return The first Clique for this CayleyGraph.
     */
    public Clique getClique() {
        return getClique(0);
    }


    /**
     * This will return the Clique identified for this CayleyGraph located at the given position in
     * the CliqueCollection.
     * 
     * @param index The position of the Clique in the CliqueCollection to be returned.
     * @return The Clique for this CayleyGraph located at the given position in the
     *         CliqueCollection.
     */
    public Clique getClique(int index) {
        return cliqueCollection.getCliqueByIndex(index);
    }


    /**
     * Standard get method for the CayleyGraphs CliqueCollection object.
     * 
     * @return The CliqueCollection for this CayleyGraph.
     */
    public CliqueCollection getCliqueCollection() {
        return cliqueCollection;
    }


    /**
     * Prints a distribution summary for a given input color. The distribution summary will describe
     * how many edges of a given color are connected to the first half of the elements and compare
     * this to the number of edges of the same color connected to the second half of the elements.
     * 
     * @param color This is the color which will be counted when determining the distribution.
     * @return A human readable String of the form [x:y]<br>
     *         x = number of edges of color "color" in the first half of the Cayley Graph<br>
     *         y = number of edges of color "color" in the second half of the Cayley Graph
     */
    public String printDistributionSummary(String color) {
        int firstHalfCount = 0;
        int secondHalfCount = 0;

        for (int i = 0; i < getNumOfElements() / 2; i++) {
            firstHalfCount += cayleyGraphArray[i].getEdgeCount(color);
        }
        for (int i = getNumOfElements() / 2; i < getNumOfElements(); i++) {
            secondHalfCount += cayleyGraphArray[i].getEdgeCount(color);
        }

        return "[" + firstHalfCount + ":" + secondHalfCount + "]";
    }


    /**
     * Get a random edge object of a given color from the cayleyGraph.
     * 
     * @param color This defines what color edge will be returned at random.
     * @return A random edge of the given color specified by the input.
     */
    public Edge getRandomEdge(String color) {
        int x = 0;
        int y = 0;
        while (x == y || !getEdgeByVertexIds(x, y).getColor().equals(color)) {
            x = ApplicationContext.getGenerator().nextInt(getNumOfElements());
            y = ApplicationContext.getGenerator().nextInt(getNumOfElements());
        }
        return getEdgeByVertexIds(x, y);
    }


    /**
     * This will get a count of the total number of edges in the CayleyGraph.
     * 
     * @return Total number of edges in the CayleyGraph.
     */
    public int getEdgeCount() {
        return ((getNumOfElements()) * (getNumOfElements() - 1)) / 2;
    }


    public void loadFromString(String data) {
        if (data.length() != getEdgeCount()) {
            logger.error("Attempting to load graph with {} edges but only {} are defined.", getEdgeCount(), data.length());
            throw new IllegalArgumentException("Invalid number of edges in String");
        }

        int index = 0;

        for (int i = 0; i < getNumOfElements(); i++) {
            for (int j = (i + 1); j < getNumOfElements(); j++) {
                if (data.charAt(index) == '0') {
                    initializeEdgeBetweenVertices(cayleyGraphArray[i], cayleyGraphArray[j], "BLUE");
                } else {
                    initializeEdgeBetweenVertices(cayleyGraphArray[i], cayleyGraphArray[j], "RED");
                }
                index++;
            }
        }
    }


    /**
     * Initialize all vertices with valid IDs
     */
    void initializeVertices() {
        // TODO have this only create new objects if null
        for (int i = 0; i < getNumOfElements(); i++) {
            cayleyGraphArray[i] = new Vertex(i, getNumOfElements());
        }
    }


    /**
     * Given two input vertices and a color this method will initialize a new edge object and link
     * it to both vertices.
     * 
     * @param vertexA First vertex of the edge.
     * @param vertexB Second vertex of the edge.
     * @param color Color of the edge.
     */
    void initializeEdgeBetweenVertices(Vertex vertexA, Vertex vertexB, String color) {
        if (vertexA.getEdge(vertexB) == null) {
            Edge edge = new Edge(vertexA, vertexB, color);
            vertexA.setEdge(edge);
            vertexB.setEdge(edge);
        } else {
            vertexA.getEdge(vertexB).setColor(color);
        }
    }


    /**
     * This will check if the cayleyGraph is still symmetric as expected. This is to say the Edge
     * connecting Vertex A to Vertex B should be the same Edge which is connecting Vertex B to
     * Vertex A. This will be used for debugging only.
     * 
     * @return This will return a True/False value indicating if the graph is symmetric as expected.
     */
    public boolean isSymmetric() {
        for (int i = 0; i < getNumOfElements(); i++) {
            for (int j = i + 1; j < getNumOfElements(); j++) {
                if (cayleyGraphArray[i].getEdge(j) != cayleyGraphArray[j].getEdge(i)) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * This will clear the cliqueCollection once it is no longer relevant after mutation.
     */
    public void clearClique() {
        getCliqueCollection().clear();
    }


    /**
     * Will return boolean value signifying if at least one clique has been identified for this
     * CayleyGraph.
     * 
     * @return This will return true if at least one clique has been identified, otherwise it will
     *         return false.
     */
    public boolean isCliqueIdentified() {
        if (getCliqueCollection().getCliqueCount() > 0) {
            return true;
        }
        return false;
    }


    /**
     * This will return the Vertex with Vertex ID matching the provided ID.
     * 
     * @param id Vertex ID of the requested Vertex to be returned
     * @return Vertex object of the requested in the CayleyGraph with Vertex ID matching the
     *         provided ID.
     */
    public Vertex getVertexById(int id) {
        return cayleyGraphArray[id];
    }
}
