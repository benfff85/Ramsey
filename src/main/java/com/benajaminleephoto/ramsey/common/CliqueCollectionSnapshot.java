package com.benajaminleephoto.ramsey.common;

import java.util.ArrayList;
import java.util.List;

public class CliqueCollectionSnapshot {

	private List<int[]> cliqueCollectionArray;

	public CliqueCollectionSnapshot() {
		cliqueCollectionArray = new ArrayList<int[]>();
	}

	public void populateSnapshot(CliqueCollection cliqueCollection) {
		for (int i = 0; i < cliqueCollection.getCliqueCount(); i++) {
			addClique(cliqueCollection.getCliqueByIndex(i));
		}
	}

	private void addClique(Clique clique) {
		cliqueCollectionArray.add(clique.getCliqueAsIntArray());

	}

	public void clearCliqueCollectionArray() {
		cliqueCollectionArray.clear();
	}

	public int getCliqueCount() {
		return cliqueCollectionArray.size();
	}

	public int[] getCliqueByPosition(int index) {
		return cliqueCollectionArray.get(index);
	}

}
