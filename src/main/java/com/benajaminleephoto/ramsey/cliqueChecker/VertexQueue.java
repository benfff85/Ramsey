package com.benajaminleephoto.ramsey.cliqueChecker;

public class VertexQueue {

    private static int vertexId;


    public static synchronized int getVertexId() {
        return vertexId++;
    }


    public static synchronized void resetVertexId() {
        vertexId = 0;
    }

}
