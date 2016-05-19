package com.benajaminleephoto.ramsey.mutate;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benajaminleephoto.ramsey.common.CayleyGraph;
import com.benajaminleephoto.ramsey.common.Clique;
import com.benajaminleephoto.ramsey.common.Config;
import com.benajaminleephoto.ramsey.common.Edge;
import com.benajaminleephoto.ramsey.common.Vertex;

public class EdgeRanker {

    private static final Logger logger = LoggerFactory.getLogger(EdgeRanker.class.getName());


    @SuppressWarnings("unchecked")
    protected static Edge getCliqueEdgeOfHighestRank(Clique clique) {
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

        vertexPair = RandomVertexIdentifier.getRandomVertices(vertexListA, vertexListB);
        cliqueEdge = vertexPair.vertexA.getEdge(vertexPair.vertexB);
        logger.trace("Clique edge selected [" + cliqueEdge.getVertexA().getId() + "," + cliqueEdge.getVertexB().getId() + "," + cliqueEdge.getColor() + "].");

        vertexListA.clear();
        vertexListB.clear();

        return cliqueEdge;
    }


    protected static Edge getCayleyGraphEdgeOfHighestRank(CayleyGraph cayleyGraph, String color) {
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

        vertexA = RandomVertexIdentifier.getRandomVertex(vertexListA);

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
        vertexB = RandomVertexIdentifier.getRandomVertex(vertexListB);

        nonCliqueEdge = cayleyGraph.getEdgeByVertices(vertexA, vertexB);
        logger.trace("Non-clique edge selected [" + nonCliqueEdge.getVertexA().getId() + "," + nonCliqueEdge.getVertexB().getId() + "," + nonCliqueEdge.getColor() + "].");

        vertexListA.clear();
        vertexListB.clear();

        return nonCliqueEdge;
    }

}
