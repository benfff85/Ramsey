package ramsey;

public class Vertex {

	int vertexId;
	Edge[] edges;
	
	public Vertex(int ID, int numOfElements){
		this.vertexId = ID;
		this.edges = new Edge[numOfElements];
	}
	
	public void updateId(int ID){
		this.vertexId = ID;
	}
	
	public int getId(){
		return this.vertexId;
	}
	
	public void setEdge(Edge edge){
		if (edge.getVertexA() == this){
			this.edges[edge.vertexB.vertexId] = edge;
		}else{
			this.edges[edge.vertexA.vertexId] = edge;
		}
		
	}
	
	public void updateEdgeColor(Vertex linkedVertex, String color){
		this.edges[linkedVertex.getId()].setColor(color);
	}
	
	public Edge getEdge(Vertex linkedVertex){
		return this.edges[linkedVertex.getId()];
	}
	
	public int getEdgeCount(String color){
		int count=0;
		for (int x=0; x < this.edges.length; x++){
			if (x != this.vertexId && this.edges[x].getColor()==color){
				count++;
			}
		}
		return count;
	}
	
	public void rotateEdges(){
		Edge swap;

		swap = this.edges[0];
		for(int i=0; i<this.edges.length-1; i++){
			this.edges[i] = this.edges[i+1];
		}
		this.edges[this.edges.length-1] = swap;
	}
	
}
