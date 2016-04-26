package com.benajaminleephoto.ramsey.common;

/**
 * This Class represents an edge within a CayleyGraph connecting two vertices.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class Edge implements java.io.Serializable {

	private static final long	serialVersionUID	= -6733335734314818893L;
	private Vertex				vertexA;
	private Vertex				vertexB;
	private String				color;

	/**
	 * This is the main constructor for the Edge Class. It will set the vertices the edge is connected to as well as well as initialize the color of the Edge.
	 * 
	 * @param A
	 *            This is the first Vertex the Edge is connected too. Best practice is to make this the Vertex with the lesser ID.
	 * @param B
	 *            This is the second Vertex the Edge is connected too. Best practice is to make this the Vertex with the greater ID.
	 * @param color
	 *            This is the color which the Edge will be initialized to.
	 */
	public Edge(Vertex A, Vertex B, String color) {
		this.vertexA = A;
		this.vertexB = B;
		this.color = color;

	}

	/**
	 * This method will set the color of the edge.
	 * 
	 * @param color
	 *            This is the color the edge will be set to.
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * This will return the Vertex object VertexA.
	 * 
	 * @return VertexA.
	 */
	public Vertex getVertexA() {
		return this.vertexA;
	}

	/**
	 * This will return the Vertex object VertexB.
	 * 
	 * @return VertexB.
	 */
	public Vertex getVertexB() {
		return this.vertexB;
	}

	/**
	 * This will return the String value of the edge's current color.
	 * 
	 * @return This is the String value of the color of the edge.
	 */
	public String getColor() {
		return this.color;
	}

	/**
	 * This will construct and return a human readable string representing the edge. This will be used for debugging only.
	 * 
	 * @return A human readable string representing the Edge.
	 */
	public String printEdge() {
		return "[" + this.vertexA.getId() + ":" + this.vertexB.getId() + ":" + this.color + "]";
	}

	public Vertex getOtherVertex(Vertex vertex) {
		if (this.vertexA == vertex) {
			return this.vertexB;
		} else {
			return this.vertexA;
		}
	}

	/**
	 * This will flip the color of the edge.
	 * 
	 * @return void
	 */
	public void flipColor() {
		if (getColor().equals("RED")) {
			setColor("BLUE");
		} else
			if (getColor().equals("BLUE")) {
				setColor("RED");
			}
	}

}
