package ramsey;

import java.util.Random;
 
public class CayleyGraph {

	int numOfElements;
	Vertex[] cayleyGraphArray;
	Clique clique;
	
	public CayleyGraph(int numOfElements, int cliqueSize){
		this.numOfElements = numOfElements;
		this.cayleyGraphArray = new Vertex[numOfElements];
		this.clique = new Clique(cliqueSize);
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
	public String printCayleyGraph(){
		
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
		output += "}, Method -> CircularEmbedding]";
		
		arguments[0]=this.numOfElements + "";
		arguments[1]=this.clique.getCliqueSize() + "";
		arguments[2]=output;
		//@SuppressWarnings("unused")
		//SendEmail email = new SendEmail(arguments);
		
		return output;
	}
	
	/*
	 * This will print the total count of RED and BLUE edges in the Cayley Graph
	 * Note: They should always be equal if algorithms are working as expected.
	 */
	public String printRedBlueCount(){
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
		return "[RED:" + countRed + "] [BLUE:" + countBlue + "]";
	}
	
	/*
	 * Will output distribution array for a given color
	 */
	public String printDistribution(String color){
		int count;
		String output = "[";
		
		for(int i=0;i<this.numOfElements;i++){
			count = this.cayleyGraphArray[i].getEdgeCount(color);
			output += count + ",";
		}
		output = output.substring(0,output.length()-1);
		output += "]";
		return output;
	}
	
	
	/*
	 * This will analyze the cayley graph and see if it is a example of a graph of order this.numOfElements that does have a complete subgraph of order k (this.cliqueSize)
	 * in this case k will be equal to 8 for R(8,8)
	 * If it has a complete subgraph it will return true, else it will return false
	 */
	public boolean cliqueChecker(String color){
		Vertex tree[][] = new Vertex[this.clique.getCliqueSize()][this.numOfElements];
		int pointerArray[] = new int[this.clique.getCliqueSize()];
		int pointerArrayIndex=0;
		Vertex prevLevelVertex;
		
		
		// Fill first level of tree with all vertices
		for(int i=0; i<this.numOfElements; i++){
		      tree[pointerArrayIndex][i] = this.cayleyGraphArray[i];
		}
		
		// Fill the rest of the tree with dummy vertices with ID of -1
		for(int i=1; i<this.clique.getCliqueSize(); i++){
			for(int j=0; j<this.numOfElements; j++){
				tree[i][j] = new Vertex(-1,1);
			}
		}
		
		
		
		//Main Algorithm
		while(pointerArray[0]<=((this.numOfElements-this.clique.getCliqueSize())+1)){
			prevLevelVertex = tree[pointerArrayIndex][pointerArray[pointerArrayIndex]];
		    pointerArray[pointerArrayIndex]++;
		    pointerArrayIndex++;

		    for(int i=prevLevelVertex.getId()+1; i<this.numOfElements; i++){
		    	if(this.cayleyGraphArray[i].getEdge(prevLevelVertex).getColor()==color && isInPrevLevel(this.cayleyGraphArray[i],tree[pointerArrayIndex-1])){
			        tree[pointerArrayIndex][pointerArray[pointerArrayIndex]] = this.cayleyGraphArray[i];	
			        pointerArray[pointerArrayIndex]++;
			    }	    
		    }	
		    
		  //If at least (this.cliqueSize-Level) elements, then successful row, reset pointer for row and move to next level
		    if(pointerArray[pointerArrayIndex]>=(this.clique.getCliqueSize()-pointerArrayIndex)){
		    	if(pointerArrayIndex==(this.clique.getCliqueSize()-1)){
		    		//Store the found clique into the global var
		    		Vertex[] cliqueVertexArray = new Vertex[this.clique.getCliqueSize()];
		    		for(int x=0; x<this.clique.getCliqueSize(); x++){
		    			cliqueVertexArray[x] = tree[x][pointerArray[x]-1];
		    		}
		    		this.clique.updateClique(cliqueVertexArray);
		    		return true;
		        }
		    	pointerArray[pointerArrayIndex]=0; 
		    }
		    
		    //Otherwise there are not enough elements for a clique, delete row and slide pointer for previous level up one repeat until link-to-be is non-negative one
		    else{
		    	for(int i=0; i<this.numOfElements && tree[pointerArrayIndex][i].getId()!=-1; i++){
		        	tree[pointerArrayIndex][i]= new Vertex(-1,1);
		        }
		        pointerArray[pointerArrayIndex]=0;	
		        pointerArrayIndex--;
		        
		        while(tree[pointerArrayIndex][pointerArray[pointerArrayIndex]].getId()==-1){
		        	for(int i=0; i<this.numOfElements && tree[pointerArrayIndex][i].getId()!=-1; i++){
			        	tree[pointerArrayIndex][i]= new Vertex(-1,1);
			        }
			        pointerArray[pointerArrayIndex]=0;	
			        pointerArrayIndex--;
		        }
		    }
		}
		return false;
	}
		    
	
	
	/*
	 * This will determine if the previous level of the tree also contained the element in question
	 */
	private boolean isInPrevLevel(Vertex vertex, Vertex[] level){
		for(int i=0;i<this.numOfElements && level[i].getId()<=vertex.getId();i++){
			if(level[i] == vertex){
				return true;
			}
		}
		return false;
	}
	
	
	
	public void mutateGraphRandom(int countOfSwaps){
		Random generator = new Random();
		boolean redSelected = false;
		boolean blueSelected = false;
		Edge redEdge = null;
		Edge blueEdge = null;
		int x,y;
		
		for(int i=0;i<countOfSwaps;i++){
			while(!redSelected || !blueSelected){
				x = generator.nextInt(this.numOfElements);
				y = generator.nextInt(this.numOfElements);
				
				if(x!=y && !redSelected && this.cayleyGraphArray[x].getEdge(this.cayleyGraphArray[y]).getColor()=="RED"){
					redEdge = this.cayleyGraphArray[x].getEdge(this.cayleyGraphArray[y]);
					redSelected = true;
				}
				else if(x!=y && !blueSelected && this.cayleyGraphArray[x].getEdge(this.cayleyGraphArray[y]).getColor()=="BLUE"){
					blueEdge = this.cayleyGraphArray[x].getEdge(this.cayleyGraphArray[y]);
					blueSelected = true;				
				}
			}
			redEdge.setColor("BLUE");
			blueEdge.setColor("RED");
			redSelected = false;
			blueSelected = false;
		}	
	}

	
	public void mutateGraphTargeted(){
		Random generator = new Random();
		boolean redSelected = false;
		boolean blueSelected = false;
		Edge redEdge = null;
		Edge blueEdge = null;
		int x,y;
		
		// Select the edge from clique to swap
		x = 0;
		y = 0;
		while(x == y){
			y = generator.nextInt(this.clique.getCliqueSize());	
		}
		
		if(this.clique.getCliqueVertexByPosition(x).getEdge(this.clique.getCliqueVertexByPosition(y)).getColor() == "RED"){	
			redEdge = this.clique.getCliqueVertexByPosition(x).getEdge(this.clique.getCliqueVertexByPosition(y));
			redSelected = true;
		}
		else{
			blueEdge = this.clique.getCliqueVertexByPosition(x).getEdge(this.clique.getCliqueVertexByPosition(y));
			blueSelected = true;
		}
		
		// Select the edge of opposite color to swap as well
		x = 0;
		y = 0;
		while(!blueSelected || !redSelected){
			x = generator.nextInt(this.numOfElements);
			y = generator.nextInt(this.numOfElements);
			if(x!=y){
				if(!blueSelected && this.cayleyGraphArray[x].getEdge(this.cayleyGraphArray[y]).getColor()=="BLUE"){
					blueEdge = this.cayleyGraphArray[x].getEdge(this.cayleyGraphArray[y]);
					blueSelected = true;
				}
				else if(!redSelected && this.cayleyGraphArray[x].getEdge(this.cayleyGraphArray[y]).getColor()=="RED"){
					redEdge = this.cayleyGraphArray[x].getEdge(this.cayleyGraphArray[y]);
					redSelected = true;
				}
			}
		}		
		redEdge.setColor("BLUE");
		blueEdge.setColor("RED");
	}
	
	public Edge getEdgeByVertexIds(int vertexIdA, int vertexIdB){
		return this.cayleyGraphArray[vertexIdA].getEdge(this.cayleyGraphArray[vertexIdB]);
	}
	
	public Edge getEdgeByVertices(Vertex vertexA, Vertex vertexB){
		return vertexA.getEdge(vertexB);
	}
	
	public Clique getClique(){
		return this.clique;
	}
	
	
	
}
