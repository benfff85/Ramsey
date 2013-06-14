package ramsey;

public class Edge {

	Vertex vertexA;
	Vertex vertexB;
	String color;
	
	
	public Edge(Vertex A, Vertex B, String color) {
		this.vertexA = A;
		this.vertexB = B;
		this.color = color;
		
	}
	
	public void setColor(String color){
		this.color = color;
	}
	
	public Vertex getVertexA(){
		return this.vertexA;
	}
	
	public Vertex getVertexB(){
		return this.vertexB;
	}
	
	public String getColor(){
		return this.color;
	}

}
