package ramsey;

public class Clique {

	Vertex[] cliqueVertexArray;
	
	public Clique(int cliqueSize){
		this.cliqueVertexArray = new Vertex[cliqueSize];
	}  
	
	public void updateClique(Vertex[] vertices){
		this.cliqueVertexArray = vertices;		
	}
	
	public String getColor(){
		return this.cliqueVertexArray[0].getEdge(this.cliqueVertexArray[1]).getColor();
	}
	
	public int getCliqueSize(){
		return this.cliqueVertexArray.length;
	}
	 
	
	
}
