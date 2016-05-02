package com.benajaminleephoto.ramsey.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CliqueCollection implements java.io.Serializable {

    private static final long serialVersionUID = -1195229985562894286L;
    private List<Clique> cliqueList;


    /**
     * Primary constructor for the CliqueCollection.
     */
    public CliqueCollection() {
        cliqueList = Collections.synchronizedList(new ArrayList<Clique>());
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
        Random generator = new Random();
        return getCliqueByIndex(generator.nextInt(getCliqueCount()));
    }

}
