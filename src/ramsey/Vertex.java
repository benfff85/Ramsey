package ramsey;

/**
 * This Class represents a vertex within a CayleyGraph.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class Vertex implements java.io.Serializable {

	private static final long serialVersionUID = -8018799200612785856L;
	int vertexId;
	Edge[] edges;

	/**
	 * This is the constructor class. If will set the ID of the vertex based off
	 * of the input as well as initialize an empty array of edges of the size
	 * numOfElements.
	 * 
	 * @param ID The new ID to be set for this vertex
	 * @param numOfElements The number of elements in the CayleyGraph including
	 *        this one. This is used to initialize the Edge array.
	 * @return void
	 */
	public Vertex(int ID, int numOfElements) {
		updateId(ID);
		this.edges = new Edge[numOfElements];
	}

	/**
	 * This will update the numeric ID of this vertex [0...numOfElements-1]
	 * 
	 * @param ID The new ID to be set for this vertex
	 * @return void
	 */
	public void updateId(int ID) {
		this.vertexId = ID;
	}

	/**
	 * This will return the numeric ID of this vertex [0...numOfElements-1]
	 * 
	 * @return The numeric ID of this vertex
	 */
	public int getId() {
		return this.vertexId;
	}

	/**
	 * This will add an input Edge to the local array of edges. For now, since
	 * this makes the assumption that the Vertex of edges[X] will be X it is
	 * advised that this only be used during initialization.
	 * 
	 * @param edge This is the edge to add to the local array of edges.
	 * @return void
	 */
	public void setEdge(Edge edge) {

		if (edge.getVertexA() == this) {
			this.edges[edge.getVertexB().getId()] = edge;
		} else if (edge.getVertexB() == this) {
			this.edges[edge.getVertexA().getId()] = edge;
		}
	}

	/**
	 * This will change the color of an edge between this vertex and another
	 * remote vertex defined by the input.
	 *  
	 * @param linkedVertex This is the remote vertex used to identify the edge
	 *        to be updated
	 * @param color This is the color to update the edge to
	 * @return void
	 */
	public void updateEdgeColor(Vertex linkedVertex, String color) {
		this.getEdge(linkedVertex).setColor(color);
	}

	/**
	 * This will return the edge which connects this vertex with another remote
	 * vertex defined by the input.
	 * 
	 * @param linkedVertex This is the remote vertex used to identify the edge
	 *        to be returned
	 * @return The edge linking this vertex with the remote vertex defined by
	 *         the input.
	 */
	public Edge getEdge(Vertex linkedVertex) {
		// We check this first since this is usually the case
		if (this.edges[linkedVertex.getId()] != null && (this.edges[linkedVertex.getId()].getVertexA() == linkedVertex || this.edges[linkedVertex.getId()].getVertexB() == linkedVertex)) {
			return this.edges[linkedVertex.getId()];
		}

		for (int i = 0; i < this.edges.length; i++) {
			if (this.edges[i] != null && (this.edges[i].getVertexA() == linkedVertex || this.edges[i].getVertexB() == linkedVertex)) {
				return this.edges[i];
			}
		}
		return null;
	}
	
	/**
	 * This will return the edge which connects this vertex with another remote
	 * vertex defined by the input.
	 * 
	 * @param linkedVertexID This is the remote vertex ID used to identify the edge
	 *        to be returned
	 * @return The edge linking this vertex with the remote vertex defined by
	 *         the input.
	 */
	public Edge getEdge(int linkedVertexID) {
		// We check this first since this is usually the case
		if (this.edges[linkedVertexID] != null && (this.edges[linkedVertexID].getVertexA().getId() == linkedVertexID || this.edges[linkedVertexID].getVertexB().getId() == linkedVertexID)) {
			return this.edges[linkedVertexID];
		}

		for (int i = 0; i < this.edges.length; i++) {
			if (this.edges[i] != null && (this.edges[i].getVertexA().getId() == linkedVertexID || this.edges[i].getVertexB().getId() == linkedVertexID)) {
				return this.edges[i];
			}
		}
		return null;
	}

	/**
	 * This method will return the total number of edges connected to this
	 * vertex of a given color.
	 * 
	 * @param color This is the color of the edges to be counted
	 * @return The total number of connected edges of a given color defined by
	 *         the input
	 */
	public int getEdgeCount(String color) {
		int count = 0;
		for (int i = 0; i < this.edges.length; i++) {
			if (i != this.vertexId && this.getEdge(i).getColor().equals(color)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * This method will rotate all Edges in this vertex's Edge array to the left
	 * one position. Also moving the left most Edge to the right most position.
	 * 
	 * @return void
	 */
	public void rotateEdgesLeft() {
		Edge swap;
		
		swap = this.edges[0];
		System.arraycopy(this.edges, 1, this.edges, 0, this.edges.length - 1);
		this.edges[this.edges.length - 1] = swap;
	}

	/**
	 * This method will rotate all Edges in this vertex's Edge array to the right
	 * one position. Also moving the right most Edge to the left most position.
	 * 
	 * @return void
	 */
	public void rotateEdgesRight() {
		Edge swap;
		
		swap = this.edges[this.edges.length - 1];
		System.arraycopy(this.edges, 0, this.edges, 1, this.edges.length - 1);
		this.edges[0] = swap;
	}


	/**
	 * This will return true if all of the edges in the edge array have
	 * this.vertexID as one of their vertices. Otherwise it will return false.
	 * This will be used for debugging only.
	 * 
	 * @return This will return a True/False value indicating if all the edges
	 *         are connected to this vertex as expected.
	 */
	public boolean areAllEdgesAttached() {
		for (int i = 0; i < this.edges.length; i++) {
			if (i != this.getId() && this.edges[i].getVertexA() != this && this.edges[i].getVertexB() != this) {
				return false;
			}
		}
		return true;
	}
}
