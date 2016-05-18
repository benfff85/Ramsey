package com.benajaminleephoto.ramsey.common;

import java.util.Random;

public class ApplicationContext {

    private static CayleyGraph cayleyGraph;
    private static Random generator;


    public static CayleyGraph getCayleyGraph() {
        return cayleyGraph;
    }


    public static void setCayleyGraph(CayleyGraph cayleyGraph) {
        ApplicationContext.cayleyGraph = cayleyGraph;
    }


    public static Random getGenerator() {
        return generator;
    }


    public static void setGenerator(Random generator) {
        ApplicationContext.generator = generator;
    }

}
