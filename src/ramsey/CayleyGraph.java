package ramsey;

import java.util.Random;
 
public class CayleyGraph {

	int numOfElements;
	int cliqueSize;
	Vertex[] cayleyGraphArray;
	Clique[] clique;
	
	public CayleyGraph(int numOfElements, int cliqueSize){
		this.numOfElements = numOfElements;
		this.cliqueSize = cliqueSize;
		this.cayleyGraphArray = new Vertex[numOfElements];
		this.clique = new Clique[cliqueSize];
	}
		
	/*
	 * Will Generate a random Cayley Graph and assign it to the object
	 * The total number of edges of each color will be equal.
	 */
	public void generateRandomGraph(){
		int redCount = ((this.numOfElements)*(this.numOfElements-1))/4;
		int blueCount = redCount;
		
		// Initialize all vertices with valid IDs
		for (int i=0;i<this.numOfElements;i++){
			this.cayleyGraphArray[i] = new Vertex(i,this.numOfElements);
		}
		
		Random generator = new Random();		
		
		for(int i=0;i<this.numOfElements;i++){
			for(int j=(i+1);j<this.numOfElements;j++){
				if (redCount == 0){
					Edge edge = new Edge(this.cayleyGraphArray[i],this.cayleyGraphArray[j],"BLUE");
					this.cayleyGraphArray[i].setEdge(edge);
					this.cayleyGraphArray[j].setEdge(edge); 
				}
				else if (blueCount == 0){
					Edge edge = new Edge(this.cayleyGraphArray[i],this.cayleyGraphArray[j],"RED");
					this.cayleyGraphArray[i].setEdge(edge);
					this.cayleyGraphArray[j].setEdge(edge); 				}
				else{
					if(generator.nextBoolean() == false){
						Edge edge = new Edge(this.cayleyGraphArray[i],this.cayleyGraphArray[j],"BLUE");
						this.cayleyGraphArray[i].setEdge(edge);
						this.cayleyGraphArray[j].setEdge(edge); 
						blueCount--;
				    } else {
						Edge edge = new Edge(this.cayleyGraphArray[i],this.cayleyGraphArray[j],"RED");
						this.cayleyGraphArray[i].setEdge(edge);
						this.cayleyGraphArray[j].setEdge(edge); 
						redCount--;
				    }
				} 
			}
		}
	}
	
	
	/*
	 * Will output the Cayley Graph in a format compatible with Mathematica
	 */
	public void printCayleyGraph(){
		
		String output = "";
		String[] arguments = new String[3];
		output += "GraphPlot[{";
		
		for(int i=0;i<this.numOfElements;i++){
			output += "{";
			for(int j=0;j<this.numOfElements;j++){
				
				if(i != j && this.cayleyGraphArray[i].getEdge(this.cayleyGraphArray[j]).getColor() == "RED"){
					output += "1, ";
				}else{
					output += "0, ";
				}
			}
			output = output.substring(0,output.length()-2);
			output += "},";
		}
		output = output.substring(0,output.length()-1);
		output += "}, Method -> SpiralEmbedding]";
		System.out.println(output);
		
		arguments[0]=this.numOfElements + "";
		arguments[1]=this.cliqueSize + "";
		arguments[2]=output;
		//@SuppressWarnings("unused")
		//SendEmail email = new SendEmail(arguments);
	}
	
	/*
	 * This will print the total count of RED and BLUE edges in the Cayley Graph
	 * Note: They should always be equal if algorithms are working as expected.
	 */
	public void printRedBlueCount(){
		int countRed = 0;
		int countBlue = 0;
		for(int i=0;i<this.numOfElements;i++){
			for(int j=i+1;j<this.numOfElements;j++){
				if(this.cayleyGraphArray[i].getEdge(this.cayleyGraphArray[j]).getColor() == "RED"){
					countRed++;
				}else{
					countBlue++;
				}
			}
		}
		System.out.println("RED:" + countRed + " BLUE:" + countBlue);
	}
	
	/*
	 * Will output distribution array for a given color
	 */
	public void printDistribution(String color){
		int count;
		String output = "[";
		
		for(int i=0;i<this.numOfElements;i++){
			count = this.cayleyGraphArray[i].getEdgeCount(color);
			output += count + ",";
		}
		output = output.substring(0,output.length()-1);
		System.out.println(output);
	}
	
	public boolean cliqueChecker(){
		
		
		return false;
	}
	
	public void mutateGraphRandom(){
		
	}
	
	public void mutateGraphTargeted(){
		
	}
	
	
	
	
	
	
}
