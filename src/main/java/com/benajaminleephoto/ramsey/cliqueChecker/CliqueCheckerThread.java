package com.benajaminleephoto.ramsey.cliqueChecker;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benajaminleephoto.ramsey.common.CayleyGraph;
import com.benajaminleephoto.ramsey.common.Clique;
import com.benajaminleephoto.ramsey.common.Config;
import com.benajaminleephoto.ramsey.common.Vertex;

class CliqueCheckerThread implements Callable<Integer> {

    private int threadId;
    public static CayleyGraph cayleyGraph;
    public static String color;
    private static final Logger logger = LoggerFactory.getLogger(CliqueCheckerThread.class.getName());


    CliqueCheckerThread(int threadId) {
        this.threadId = threadId;
    }


    public Integer call() {
        logger.trace("Starting FindCliqueThread {} ", threadId);
        int i = VertexQueue.getVertexId();
        while (i < cayleyGraph.getNumOfElements() - Config.CLIQUE_SIZE) {

            try {
                findCliqueRecursive(i, color);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.trace("Finished checking vertex {} for cliques", i);
            i = VertexQueue.getVertexId();

        }
        logger.trace("Finished FindCliqueThread {}", threadId);
        return null;

    }


    private void findCliqueRecursive(int StartingVertexID, String color) throws Exception {
        ArrayList<Vertex> connectedVertices = new ArrayList<Vertex>();
        connectedVertices.add(cayleyGraph.getVertexById(StartingVertexID));
        findCliqueRecursive(connectedVertices, color);
        connectedVertices.clear();
    }


    private void findCliqueRecursive(ArrayList<Vertex> connectedVertices, String color) throws Exception {
        // Check if cliqueSearch style if "First" and we already found a clique
        if (Config.CLIQUE_SEARCH_STRATAGY == CLIQUE_SEARCH_TYPE.FIRST && cayleyGraph.isCliqueIdentified()) {
            return;
        }

        // Loop through all vertices starting with the one after the last vertex in the chain
        for (int i = connectedVertices.get(connectedVertices.size() - 1).getId() + 1; i < cayleyGraph.getNumOfElements(); i++) {
            // If the vertex being considered is connected
            if (isConnected(connectedVertices, cayleyGraph.getVertexById(i), color)) {
                connectedVertices.add(cayleyGraph.getVertexById(i));
                // If this and makes a completed clique add it to the clique collection
                if (connectedVertices.size() == Config.CLIQUE_SIZE) {
                    cayleyGraph.getCliqueCollection().addClique(new Clique(connectedVertices));
                }
                // Otherwise if there are enough possible options left to form a clique proceed with
                // search
                // TODO optimize by adding second condition above.
                else {
                    findCliqueRecursive(connectedVertices, color);
                }
                // Remove this vertex from the chain and try the next at this level
                connectedVertices.remove(cayleyGraph.getVertexById(i));
            }
        }

        // Once all have been tried at this level return
        return;
    }


    private static boolean isConnected(ArrayList<Vertex> connectedVertices, Vertex vertex, String color) {
        for (int i = 0; i < connectedVertices.size(); i++) {
            if (!connectedVertices.get(i).getEdge(vertex).getColor().equals(color)) {
                // logger.trace("Vertex {} is not connected to Vertex {}",
                // connectedVertices.get(i).getId(), vertex.getId());
                return false;
            }
            // logger.trace("Vertex {} is connected to Vertex {}", connectedVertices.get(i).getId(),
            // vertex.getId());
        }
        // logger.trace("Vertex {} is fully connected to prior connected vertices", vertex.getId());
        return true;
    }

}
