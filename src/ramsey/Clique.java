package ramsey;

public class Clique {

	Vertex[] cliqueVertexArray;
	Edge[] cliqueEdgeArray;
	String color;
	
	public Clique(Vertex[] vertices, Edge[] edges){
		this.cliqueVertexArray = vertices;
		this.cliqueEdgeArray = edges;
		this.color = this.cliqueEdgeArray[0].getColor();
	}  
	
	public void setColor(String color){
		this.color = color;
	}
	
	public String getColor(){
		return this.color;
	}
	
	
	
}
