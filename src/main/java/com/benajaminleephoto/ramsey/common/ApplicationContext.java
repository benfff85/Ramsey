package com.benajaminleephoto.ramsey.common;

public class ApplicationContext {

    private static CayleyGraph cayleyGraph;


    public static CayleyGraph getCayleyGraph() {
        return cayleyGraph;
    }


    public static void setCayleyGraph(CayleyGraph cayleyGraph) {
        ApplicationContext.cayleyGraph = cayleyGraph;
    }

}
