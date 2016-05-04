package com.benajaminleephoto.ramsey.mutate;

import java.util.ArrayList;
import java.util.Random;

import com.benajaminleephoto.ramsey.common.Vertex;

public class RandomVertexIdentifier {

    private static Random generator = new Random();


    public static VertexPair getRandomVertices(ArrayList<Vertex> vertexListA, ArrayList<Vertex> vertexListB) {
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
    public static Vertex getRandomVertex(ArrayList<Vertex> vertexList) {
        Vertex vertex;
        int index;

        index = generator.nextInt(vertexList.size());
        vertex = vertexList.get(index);
        vertexList.remove(index);
        return vertex;

    }

}
