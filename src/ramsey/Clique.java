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
	
	public Vertex getCliqueVertexByPosition(int vertexArrayPosition){
		return this.cliqueVertexArray[vertexArrayPosition];
	}
	 
	public String printClique(){
		String output = "[";
		for(int i=0; i<this.getCliqueSize(); i++){
			output += this.cliqueVertexArray[i].getId() + "/";
		}
		output = output.substring(0,output.length()-1);
		output += "]";
		
		return output;
	} 
	
}
