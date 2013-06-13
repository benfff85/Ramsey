package oOImplementation;

public class Edge {

	Vertex vertexIdA;
	Vertex vertexIdB;
	String color;
	
	
	public Edge(Vertex A, Vertex B, String color) {
		this.vertexIdA = A;
		this.vertexIdB = B;
		this.color = color;
		
	}
	
	public Vertex getVertexIdA(){
		return this.vertexIdA;
	}
	
	public Vertex getVertexIdB(){
		return this.vertexIdB;
	}
	
	public boolean isRed(){
		if(this.color == "RED"){
			return true;
		}
		else{
			return false;
		}
	}

}
