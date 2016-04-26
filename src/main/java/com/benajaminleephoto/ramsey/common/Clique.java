package com.benajaminleephoto.ramsey.common;

import java.util.ArrayList;

/**
 * This graph represents a clique or complete subgraph identified within a given CayleyGraph. This complete subgraph indicates we cannot raise the lower bound for the Ramsey Number we are searching for. Said another way the given CayleyGraph is not a valid counter example.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class Clique implements java.io.Serializable {

	private static final long		serialVersionUID			= -98883802433600863L;
	private static final Exception	CliqueNotCompleteSubgraph	= null;
	private static final Exception	InvalidCliqueSize			= null;
	private Vertex[]				cliqueVertexArray;
	private String					color;

	/**
	 * Class constructor for the Clique. It will initialize the vertex array representing the clique with the Vertex array received as input.
	 * 
	 * @param vertices
	 *            The array of vertices representing the clique.
	 * @return void
	 * @throws Exception
	 */
	public Clique(Vertex[] vertices) throws Exception {
		try {
			if (vertices.length != Config.CLIQUE_SIZE) {
				throw InvalidCliqueSize;
			}
			cliqueVertexArray = vertices;
			setColor();
			if (validateClique() == false) {
				throw CliqueNotCompleteSubgraph;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Class constructor for the Clique. It will initialize the vertex array representing the clique with the Vertex ArrayList received as input.
	 * 
	 * @param vertices
	 *            The array of vertices representing the clique.
	 * @return void
	 * @throws Exception
	 */
	public Clique(ArrayList<Vertex> vertices) throws Exception {
		try {
			if (vertices.size() != Config.CLIQUE_SIZE) {
				throw InvalidCliqueSize;
			}
			cliqueVertexArray = new Vertex[vertices.size()];
			for (int i = 0; i < getCliqueSize(); i++) {
				cliqueVertexArray[i] = vertices.get(i);
			}
			setColor();
			if (validateClique() == false) {
				Debug.write("Creating a clique that is not a complete subgraph!");
				throw CliqueNotCompleteSubgraph;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * This will validate if the clique is a complete subgraph or now.
	 * 
	 * @return True is the Clique is a complete subgraph otherwise false.
	 */
	public boolean validateClique() {
		for (int i = 0; i < getCliqueSize(); i++) {
			for (int j = i + 1; j < getCliqueSize(); j++) {
				if (getCliqueVertexByPosition(i).getEdge(getCliqueVertexByPosition(j)).getColor() != getColor()) {
					return false;
				}
			}
		}
		return true;
	}

	public void setColor() {
		color = this.cliqueVertexArray[0].getEdge(this.cliqueVertexArray[1]).getColor();
	}

	/**
	 * This will update Clique objects Vertex array to represent the identified subgraph.
	 * 
	 * @param vertices
	 *            This is the Vertex array of all vertices in the identified complete subgraph.
	 * @return void
	 */
	public void updateClique(Vertex[] vertices) {
		cliqueVertexArray = vertices;
	}

	/**
	 * This will return the color of the Clique.
	 * 
	 * @return A String value representing the color of this Clique.
	 */
	public String getColor() {
		return color;
	}

	/**
	 * This will return the size of the Clique.
	 * 
	 * @return Integer representing the number of elements in this Clique.
	 */
	public int getCliqueSize() {
		return cliqueVertexArray.length;
	}

	/**
	 * This will return a vertex from within the Clique based on its position in the Vertex array.
	 * 
	 * @param vertexArrayPosition
	 *            Integer value of the position of the Vertex within the Clique's Vertex array.
	 * @return Vertex from the local Vertex array at the position defined by input to this method.
	 */
	public Vertex getCliqueVertexByPosition(int vertexArrayPosition) {
		return cliqueVertexArray[vertexArrayPosition];
	}

	/**
	 * This will return a vertex from within the Clique based on the ID of the Vertex.
	 * 
	 * @param Integer
	 *            value of the ID expected on the Vertex in the Clique to be returned.
	 * @return Vertex from the Clique with the Vertex ID specified in the input. *
	 */
	public Vertex getCliqueVertexById(int vertexId) {
		for (int i = 0; i < this.getCliqueSize(); i++) {
			if (this.getCliqueVertexByPosition(i).getId() == vertexId) {
				return this.cliqueVertexArray[i];
			}
		}
		return null;
	}

	/**
	 * This will output a String representing this Clique in human readable form.
	 * 
	 * @return String representing this Clique in human readable form.
	 */
	public String printClique() {
		String output = "[";
		for (int i = 0; i < this.getCliqueSize(); i++) {
			output += this.cliqueVertexArray[i].getId() + "/";
		}
		output = output.substring(0, output.length() - 1);
		output += "]";

		return output;
	}

	/**
	 * This will output a String representing an edge level breakdown of the clique in human readable form.
	 * 
	 * @return Edge level representation of this Clique in human readable form.
	 */
	public String printDetailedClique() {
		String output = "\n";
		String color;
		for (int a = 0; a < this.getCliqueSize(); a++) {
			for (int b = 0; b < this.getCliqueSize(); b++) {
				if (a != b) {
					if (cliqueVertexArray[a].getEdge(cliqueVertexArray[b]).getColor().equals("RED")) {
						color = "RED ";
					} else {
						color = "BLUE";
					}
					output += String.format("%03d", cliqueVertexArray[a].getId()) + "," + String.format("%03d", cliqueVertexArray[b].getId()) + " " + color + " ";
				} else {
					output += String.format("%03d", cliqueVertexArray[a].getId()) + "," + String.format("%03d", cliqueVertexArray[b].getId()) + " N/A  ";
				}
			}
			output += "\n";
		}
		return output;
	}

	/**
	 * This will return the given clique as an integer array.
	 * 
	 * @return This clique as an integer array.
	 */
	public int[] getCliqueAsIntArray() {
		int[] intArray = new int[getCliqueSize()];
		for (int i = 0; i < getCliqueSize(); i++) {
			intArray[i] = getCliqueVertexByPosition(i).getId();
		}
		return intArray;
	}
}
