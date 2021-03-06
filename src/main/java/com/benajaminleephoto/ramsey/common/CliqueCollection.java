package com.benajaminleephoto.ramsey.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multisets;

public class CliqueCollection implements java.io.Serializable {

    private static final long serialVersionUID = -1195229985562894286L;
    private List<Clique> cliqueList;


    /**
     * Primary constructor for the CliqueCollection.
     */
    public CliqueCollection() {
        cliqueList = Collections.synchronizedList(new ArrayList<Clique>());
    }


    public List<Clique> getCliqueList() {
        return cliqueList;
    }


    /**
     * This will add one Clique object to the CliqueCollection.
     * 
     * @param clique Clique to be added to the CliqueCollection.
     */
    public void addClique(Clique clique) {
        cliqueList.add(clique);
    }


    /**
     * Remove all cliques from the CliqueCollection.
     */
    public void clear() {
        cliqueList.clear();
    }


    /**
     * Returns the number of Clique objects in the CliqueCollection.
     * 
     * @return The number of Cliques in this CliqueCollection.
     */
    public int getCliqueCount() {
        return cliqueList.size();
    }


    public int getCliqueCount(String color) {
        int count = 0;
        for (Clique clique : cliqueList) {
            if (clique.getColor().equals(color)) {
                count++;
            }
        }
        return count;
    }


    /**
     * Return the Clique object in the CliqueCollection found at a given index.
     * 
     * @param index The index of the Clique object to be returned.
     * @return The Clique object found at the provided index.
     */
    public Clique getCliqueByIndex(int index) {
        return cliqueList.get(index);
    }


    /**
     * This will randomly return one of the cliques contained within the CliqueCollection.
     * 
     * @return A random clique from within the CliqueCollection.
     */
    public Clique getRandomClique() {
        return getCliqueByIndex(ApplicationContext.getGenerator().nextInt(getCliqueCount()));
    }


    public HashMultiset<Edge> getAllCliqueCollectionEdges(String color) {

        HashMultiset<Edge> edges = HashMultiset.create();

        for (Clique clique : cliqueList) {
            if (clique.getColor().equals(color)) {
                for (Edge edge : clique.getAllCliqueEdges()) {
                    edges.add(edge);
                }
            }
        }
        return edges;
    }


    public HashMultiset<Edge> getAllCliqueCollectionEdges() {
        HashMultiset<Edge> edges = HashMultiset.create();
        edges.addAll(getAllCliqueCollectionEdges("RED"));
        edges.addAll(getAllCliqueCollectionEdges("BLUE"));
        return edges;
    }


    public void printAllCliqueCollectionEdges() {
        HashMultiset<Edge> edges = getAllCliqueCollectionEdges();
        int count = 0;
        for (Edge edge : Multisets.copyHighestCountFirst(getAllCliqueCollectionEdges()).elementSet()) {
            if (count < 100) {
                System.out.println(edge + ":" + edges.count(edge));
            }
            count++;
        }
    }


    public Edge getMostCommonEdge(String color, int range) {
        int randomIndex = ApplicationContext.getGenerator().nextInt(range);
        int count = 0;
        Edge returnEdge = null;

        for (Edge edge : Multisets.copyHighestCountFirst(getAllCliqueCollectionEdges(color)).elementSet()) {
            if (count == randomIndex) {
                returnEdge = edge;
                break;
            }
            count++;
        }

        return returnEdge;

    }

}
