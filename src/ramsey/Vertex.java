package ramsey;

/**
 * This Class represents a vertex within a CayleyGraph. 
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class Vertex {

	int vertexId;
	Edge[] edges;
	
	
	/**
	 * This is the constructor class. If will set the ID of the vertex based off of the input as well as initialize an empty array of edges of the size numOfElements.
	 * 
	 * @param ID The new ID to be set for this vertex
	 * @param numOfElements The number of elements in the CayleyGraph including this one. This is used to initialize the Edge array.
	 * @return void
	 */
	public Vertex(int ID, int numOfElements){
		updateId(ID);
		this.edges = new Edge[numOfElements];
	}
	
	
	/**
	 * This will update the numeric ID of this vertex [0...numOfElements-1]
	 * 
	 * @param ID The new ID to be set for this vertex
	 * @return void
	 */
	public void updateId(int ID){
		this.vertexId = ID;
	}
	
	
	/**
	 * This will return the numeric ID of this vertex [0...numOfElements-1]
	 * 
	 * @return The numberic ID of this vertex
	 */
	public int getId(){
		return this.vertexId;
	}
	
	
	/**
	 * This will add an input Edge to the local array of edges.
	 * 
	 * @param edge This is the edge to add to the local array of edges.
	 * @return void
	 */
	public void setEdge(Edge edge){
		
		if (edge.getVertexA() == this){
			this.edges[edge.vertexB.vertexId] = edge;
		}else if (edge.getVertexB() == this) {
			this.edges[edge.vertexA.vertexId] = edge;
		} 
	}
	
	
	/**
	 * This will change the color of an edge between this vertex and another remote vertex defined by the input.
	 * 
	 * @param linkedVertex This is the remote vertex used to identify the edge to be updated
	 * @param color This is the color to update the edge to
	 * @return void
	 */
	public void updateEdgeColor(Vertex linkedVertex, String color){
		this.edges[linkedVertex.getId()].setColor(color);
	}
	
	
	/**
	 * This will return the edge which connects this vertex with another remote vertex defined by the input.
	 * 
	 * @param linkedVertex This is the remote vertex used to identify the edge to be returned
	 * @return The edge linking this vertex with the remote vertex defined by the input.
	 */
	public Edge getEdge(Vertex linkedVertex){
		return this.edges[linkedVertex.getId()];
	}

	
	
	/**
	 * This method will return the total number of edges connected to this vertex of a given color.
	 * 
	 * @param color This is the color of the edges to be counted
	 * @return The total number of connected edges of a given color defined by the input
	 */
	public int getEdgeCount(String color){
		int count=0;
		for (int x=0; x < this.edges.length; x++){
			if (x != this.vertexId && this.edges[x].getColor()==color){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * This method will rotate all Edges in this vertex's Edge array to the left one position. Also moving the left most Edge to the right most position.
	 * 
	 * @return void
	 */
	public void rotateEdges(){
		Edge swap;
		
		swap = this.edges[0];
		for(int i=0; i<this.edges.length-1; i++){
			this.edges[i] = this.edges[i+1];
		}
		this.edges[this.edges.length-1] = swap;
	}
	
	/**
	 * This will return true if all of the edges in the edge array have this.vertexID as one of their vertices. Otherwise it will return false.
	 * This will be used for debugging only.
	 * 
	 * @return This will return a True/False value indicating if all the edges are connected to this vertex as expected.
	 */
	public boolean areAllEdgesAttached(){
		for(int i=0; i<this.edges.length; i++){
			if(i!=getId() && this.edges[i].getVertexA()!=this && this.edges[i].getVertexB()!=this){
				return false;
			}
		}
		return true;
	}
}
