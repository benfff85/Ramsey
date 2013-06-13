package oOImplementation;

public class Vertex {

	int vertexId;
	int connectedEdgeCount;
	
	public Vertex(int ID){
		this.vertexId = ID;
		this.connectedEdgeCount = 0;
	}
	
	public int getId(){
		return this.vertexId;
	}
	
	public int getEdgeCount(){
		return this.connectedEdgeCount;
	}
	
	public void incrementEdgeCount(){
		this.connectedEdgeCount++;
	}
	
	public void decrementEdgeCount(){
		this.connectedEdgeCount--;
	}
	
	
	
}
