import java.io.*;
import java.util.*;
import javax.swing.JFileChooser;
import java.util.Random;

public class CayleyGraphOld {
	int[][] cayleyGraphArray;
	int numOfElements;
	int cliqueSize;
	int[] clique;
	int[] distributionArray;
	int minVertexId;
	int cliqueOneOrZero;

	public CayleyGraphOld(int numOfElements, int cliqueSize){
		this.numOfElements = numOfElements;
		this.cliqueSize = cliqueSize;
		this.clique = new int[this.cliqueSize];
		this.distributionArray = new int[this.numOfElements];
		generateRandomGraph();
		this.cliqueOneOrZero = 0;
		this.minVertexId = 0;
		
	}
	
	public void mutateGraphRandom(int countOfSwaps){
		int oneEdgeX = -1;
		int oneEdgeY = -1;
		int zeroEdgeX = -1;
		int zeroEdgeY = -1;
		boolean zeroSelected = false;
		boolean oneSelected = false;
		int x,y;
		
		Random generator = new Random();
		for(int i=0;i<countOfSwaps;i++){
			while(!zeroSelected || !oneSelected){
				x = generator.nextInt(this.numOfElements);
				y = generator.nextInt(this.numOfElements);
				if(x!=y && this.cayleyGraphArray[x][y]==0 && !zeroSelected){
					if(!oneSelected || ((oneEdgeX!=x || oneEdgeY!=y) && (oneEdgeX!=y || oneEdgeY!=x))){
						zeroEdgeX = x;
						zeroEdgeY = y;
						zeroSelected = true;
					}									
				}else if(x!=y && this.cayleyGraphArray[x][y]==1 && !oneSelected){
					if(!zeroSelected || ((zeroEdgeX!=x || zeroEdgeY!=y) && (zeroEdgeX!=y || zeroEdgeY!=x))){
						oneEdgeX = x;
						oneEdgeY = y;
						oneSelected = true;
					}									
				}
			}
			this.cayleyGraphArray[oneEdgeX][oneEdgeY] = this.cayleyGraphArray[oneEdgeY][oneEdgeX] = 0;
			this.cayleyGraphArray[zeroEdgeX][zeroEdgeY] = this.cayleyGraphArray[zeroEdgeY][zeroEdgeX] = 1;
			zeroSelected = oneSelected = false;
			zeroEdgeX = zeroEdgeY = oneEdgeX = oneEdgeY = -1;
		}	
	}
	
	public void mutateGraphTargeted(){
		int cliqueEdgeX = -1;
		int cliqueEdgeY = -1;
		int nonCliqueEdgeX = -1;
		int nonCliqueEdgeY = -1;
		int x,y,swap;
		
		Random generator = new Random();
		
		//Select edge in clique to flip
		x = y = 0;
		while(x==y){
			y = generator.nextInt(this.cliqueSize);
		}
		cliqueEdgeX = this.clique[x];
		cliqueEdgeY = this.clique[y];
		this.minVertexId = Math.min(cliqueEdgeX,cliqueEdgeY);
		swap = this.cayleyGraphArray[cliqueEdgeX][cliqueEdgeY];
		
		
		//Select other edge to flip to maintain balance
		x = y = 0;
		while(x==y || this.cayleyGraphArray[x][y]==swap){
			x = generator.nextInt(this.numOfElements);
			y = generator.nextInt(this.numOfElements);
		}
		nonCliqueEdgeX = x;
		nonCliqueEdgeY = y;		

		
		this.cayleyGraphArray[cliqueEdgeX][cliqueEdgeY] = this.cayleyGraphArray[cliqueEdgeY][cliqueEdgeX] = this.cayleyGraphArray[nonCliqueEdgeX][nonCliqueEdgeY];
		this.cayleyGraphArray[nonCliqueEdgeX][nonCliqueEdgeY] = this.cayleyGraphArray[nonCliqueEdgeY][nonCliqueEdgeX] = swap;

	}	
	
	public void mutateGraphTargetedDistribution(){
		int cliqueEdgeX = -1;
		int cliqueEdgeY = -1;
		int nonCliqueEdgeX = -1;
		int nonCliqueEdgeY = -1;
		int x,y,swap;
		
		Random generator = new Random();
		
		//Select edge in clique to flip
		x = y = 0;
		while(x==y){
			y = generator.nextInt(this.cliqueSize);
		}
		cliqueEdgeX = this.clique[x];
		cliqueEdgeY = this.clique[y];
		swap = this.cayleyGraphArray[cliqueEdgeX][cliqueEdgeY];
		
		//Select other edge to flip to maintain balance
		x = y = 0;
		//GetMaxHere
		while(x==y || this.cayleyGraphArray[x][y]==swap){
			x = generator.nextInt(this.numOfElements);
			y = generator.nextInt(this.numOfElements);
		}
		nonCliqueEdgeX = x;
		nonCliqueEdgeY = y;
		
		this.cayleyGraphArray[cliqueEdgeX][cliqueEdgeY] = this.cayleyGraphArray[cliqueEdgeY][cliqueEdgeX] = this.cayleyGraphArray[nonCliqueEdgeX][nonCliqueEdgeY];
		this.cayleyGraphArray[nonCliqueEdgeX][nonCliqueEdgeY] = this.cayleyGraphArray[nonCliqueEdgeY][nonCliqueEdgeX] = swap;

	}	
	
	
	public void printZeroOneCount(){
		int countZero = 0;
		int countOne = 0;
		for(int i=0;i<this.numOfElements;i++){
			for(int j=i+1;j<this.numOfElements;j++){
				if(this.cayleyGraphArray[i][j]==0){
					countZero++;
				}else{
					countOne++;
				}
			}
		}
		System.out.println("Zeros: " + countZero + " Ones: " + countOne);
	}
	
	/*
	 * Will output the cayley graph counterexample in a format compatible with mathematica
	 */
	public void printCayleyGraph(){
		int cayleyGraph[][] = this.cayleyGraphArray;
		String output = "";
		String[] arguments = new String[3];
		output += "GraphPlot[{";
		
		for(int i=0;i<this.numOfElements;i++){
			output += "{";
			for(int j=0;j<this.numOfElements;j++){
			    output += cayleyGraph[i][j] + ", ";
			}
			output = output.substring(0,output.length()-2);
			output += "},";
		}
		output = output.substring(0,output.length()-1);
		output += "}, Method -> CircularEmbedding]";
		System.out.println(output);
		
		arguments[0]=this.numOfElements + "";
		arguments[1]=this.cliqueSize + "";
		arguments[2]=output;
		//@SuppressWarnings("unused")
		//SendEmail email = new SendEmail(arguments);
	}
	
	/*
	 * Will output global clique variable.
	 */
	public void printClique(){
		int cliqueToPrint[] = this.clique;
		String output = "[";
		for(int i=0;i<this.cliqueSize;i++){
			output += cliqueToPrint[i] + "/";
		}
		
		output = output.substring(0,output.length()-1);
		output += "]";
		System.out.println(output);
	}
	
	/*
	 * Will output global distribution variable.
	 */
	public void printDistribution(){
		String output = "[";
		for(int i=0;i<this.numOfElements;i++){
			output += this.distributionArray[i] + "/";
		}
		
		output = output.substring(0,output.length()-1);
		output += "]";
		System.out.println(output);
	}
	
	/*
	 * Used only for debugging, will open cayleyGraph file to check specific examples
	 */
	public void openCayleyGraph(){
		String inputLine;
		StringTokenizer st;
		int x,y;
		int[][] array = new int[this.numOfElements][this.numOfElements];
		try{
			JFileChooser chooser = new JFileChooser();	
			int retval = chooser.showOpenDialog(null);
			if (retval == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				BufferedReader buffRead = new BufferedReader(new FileReader(file));
				y=0;
				while((inputLine = buffRead.readLine())!=null){
					st=new StringTokenizer(inputLine);
					x=0;
					while(st.hasMoreTokens()) {
				        array[y][x]=(Integer.parseInt(st.nextToken()));
				        x++;
					}
					y++;
				}
				buffRead.close();
			}
		}
		catch (IOException e) {
			System.out.println("IO Error, returning null");
		}
		this.cayleyGraphArray = array;
	}
	
	/*
	 * Generates Cayley graph given cayley table of the group and the coloring configuration
	 * Cayley graph will be expressed as an this.numOfElements*this.numOfElements adjecency matrix
	 * The values in the matrix are as follows:
	 *  0: no color for given edge
	 *  1: color for given edge
	 */
	public void generateCayleyGraph(int[][] cayleyTable, int[] coloring){
		int cayleyGraph[][] = new int[this.numOfElements][this.numOfElements];
		
		for(int colorIndex=0; colorIndex<this.numOfElements; colorIndex++){
			if(coloring[colorIndex]==1){
				for(int vertexIndex=0; vertexIndex<this.numOfElements; vertexIndex++){
					cayleyGraph[vertexIndex][cayleyTable[vertexIndex][colorIndex]]=1;
					cayleyGraph[cayleyTable[vertexIndex][colorIndex]][vertexIndex]=1;
				}
			}
		}
		this.cayleyGraphArray = cayleyGraph;
	}
	
	/*
	 * This will analyze the cayley graph and see if it is a example of a graph of order this.numOfElements that does have a complete subgraph of order k (this.cliqueSize)
	 * in this case k will be equal to 8 for R(8,8)
	 * If it has a complete subgraph it will return true, else it will return false
	 */
	public boolean cliqueChecker(int lookingFor){
		int cayleyGraph[][] = this.cayleyGraphArray;
		int tree[][] = new int[this.cliqueSize][this.numOfElements];				
		int pointerArray[] = new int[this.cliqueSize];					
		int pointerArrayIndex=0;
		int link;

		//Fill first level of tree with all verticies
		for(int vertex=0; vertex<this.numOfElements; vertex++){
		      tree[pointerArrayIndex][vertex]=vertex;
		}
		
		//Main Algorithm
		while(pointerArray[0]<=((this.numOfElements-this.cliqueSize)+1)){
		    link=tree[pointerArrayIndex][pointerArray[pointerArrayIndex]];
		    pointerArray[pointerArrayIndex]++;
		    pointerArrayIndex++;

		    for(int i=0; i<this.numOfElements; i++){
		        if(cayleyGraph[link][i]==lookingFor && i>link && prevLevelContains(i,tree[pointerArrayIndex-1])){
			        tree[pointerArrayIndex][pointerArray[pointerArrayIndex]]=i;	
			        pointerArray[pointerArrayIndex]++;
			    }	    
		    }
		    
		   		    
		    //If at least (this.cliqueSize-Level) elements, then successful row, reset pointer for row and move to next level
		    if(pointerArray[pointerArrayIndex]>=(this.cliqueSize-pointerArrayIndex)){
		    	if(pointerArrayIndex==(this.cliqueSize-1)){
		    		//Store the found clique into the global var
		    		for(int x=0; x<this.cliqueSize; x++){
		    			this.clique[x] = tree[x][pointerArray[x]-1];

		    		}
		    		return true;
		        }
		    	pointerArray[pointerArrayIndex]=0; 
		    }
		    //Otherwise there are not enough elements for a clique, delete row and slide pointer for previous level up one repeat until link-to-be is nonzero
		    else{
		    	//printTree(tree);
		    	for(int i=0; i<this.numOfElements && tree[pointerArrayIndex][i]!=0; i++){
		        	tree[pointerArrayIndex][i]=0;
		        }
		        pointerArray[pointerArrayIndex]=0;	
		        pointerArrayIndex--;
		        
		        while(tree[pointerArrayIndex][pointerArray[pointerArrayIndex]]==0){
		        	for(int i=0; i<this.numOfElements && tree[pointerArrayIndex][i]!=0; i++){
			        	tree[pointerArrayIndex][i]=0;
			        }
			        pointerArray[pointerArrayIndex]=0;	
			        pointerArrayIndex--;
		        }
		    }
		}
		return false;
	}
	
	
	/*
	 * This will analyze the cayley graph and see if it is a example of a graph of order this.numOfElements that does have a complete subgraph of order k (this.cliqueSize)
	 * in this case k will be equal to 8 for R(8,8)
	 * If it has a complete subgraph it will return true, else it will return false
	 */
	public boolean cliqueChecker2(int lookingFor){
		int cayleyGraph[][] = this.cayleyGraphArray;
		int tree[][] = new int[this.cliqueSize][this.numOfElements];				
		int pointerArray[] = new int[this.cliqueSize];					
		int pointerArrayIndex=0;
		int link;

		//Fill first level of tree with all verticies
		for(int vertex=0; vertex<this.numOfElements; vertex++){
		    tree[pointerArrayIndex][vertex]=vertex;
		}
		
		
		if(this.cliqueOneOrZero == lookingFor){
			pointerArray[0]=this.minVertexId;	
		}
		
		//Main Algorithm
		while(pointerArray[0]<=((this.numOfElements-this.cliqueSize)+1)){
		    link=tree[pointerArrayIndex][pointerArray[pointerArrayIndex]];
		    pointerArray[pointerArrayIndex]++;
		    pointerArrayIndex++;

		    for(int i=0; i<this.numOfElements; i++){
		        if(cayleyGraph[link][i]==lookingFor && i>link && prevLevelContains(i,tree[pointerArrayIndex-1])){
			        tree[pointerArrayIndex][pointerArray[pointerArrayIndex]]=i;	
			        pointerArray[pointerArrayIndex]++;
			    }	    
		    }
		    
		   		    
		    //If at least (this.cliqueSize-Level) elements, then successful row, reset pointer for row and move to next level
		    if(pointerArray[pointerArrayIndex]>=(this.cliqueSize-pointerArrayIndex)){
		    	if(pointerArrayIndex==(this.cliqueSize-1)){
		    		//Store the found clique into the global var
		    		for(int x=0; x<this.cliqueSize; x++){
		    			this.clique[x] = tree[x][pointerArray[x]-1];

		    		}
		    		this.cliqueOneOrZero = lookingFor;
		    		return true;
		        }
		    	pointerArray[pointerArrayIndex]=0;
		    }
		    //Otherwise there are not enough elements for a clique, delete row and slide pointer for previous level up one repeat until link-to-be is nonzero
		    else{
		    	//printTree(tree);
		    	for(int i=0; i<this.numOfElements && tree[pointerArrayIndex][i]!=0; i++){
		        	tree[pointerArrayIndex][i]=0;
		        }
		        pointerArray[pointerArrayIndex]=0;	
		        pointerArrayIndex--;
		        
		        while(tree[pointerArrayIndex][pointerArray[pointerArrayIndex]]==0){
		        	for(int i=0; i<this.numOfElements && tree[pointerArrayIndex][i]!=0; i++){
			        	tree[pointerArrayIndex][i]=0;
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
	public boolean prevLevelContains(int element, int[] level){
		for(int i=0;i<this.numOfElements && level[i]<=element;i++){
			if(level[i]==element){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Will Generate a random Cayley Graph and assign it to the object
	 * The total number of edged of each color will be equal.
	 */
	public void generateRandomGraph(){
		int graph[][] = new int[this.numOfElements][this.numOfElements];
		int count0 = ((this.numOfElements)*(this.numOfElements-1))/4;
		int count1 = count0;
		Random generator = new Random();
		for(int i=0;i<this.numOfElements;i++){
			for(int j=(i+1);j<this.numOfElements;j++){
				if (count0 == 0){
					graph[j][i]=graph[i][j]=1;
				}
				else if (count1 == 0){
					graph[j][i]=graph[i][j]=0;
				}
				else{
					if(generator.nextBoolean() == false){
					    graph[j][i]=graph[i][j]=0;
					    count0--;
				    } else {
					    graph[j][i]=graph[i][j]=1;
					    count1--;
				    }
				} 
			}
		}
		this.cayleyGraphArray = graph;
	}
	
	
	/*
	 * Will Generate a random Cayley Graph and assign it to the object
	 * The total number of edged of each color will be equal.
	 * The number of edges connected to each vertex will be equal.
	 * 
	 * Not working due to no predection
	 */
	public void generateRandomGraphBalanced(){
		int graph[][] = new int[this.numOfElements][this.numOfElements];
		int maxEdgePerVertex = this.numOfElements/2;
		int potentialVertex;
		Random generator = new Random();
		
		for(int i=0;i<this.numOfElements;i++){
			while(this.distributionArray[i] < maxEdgePerVertex){
				potentialVertex = generator.nextInt(this.numOfElements);
				if(i!=potentialVertex && this.distributionArray[potentialVertex]<maxEdgePerVertex){
					graph[i][potentialVertex]=graph[potentialVertex][i]=1;
					this.distributionArray[i]++;
					this.distributionArray[potentialVertex]++;
				}
			}
			printDistribution();
		}
		this.cayleyGraphArray = graph;
	}
	
	
	public void calculateDistribution(){
		int edgeCount=0;
		
		for(int i=0;i<this.numOfElements;i++){
			for(int j=0;j<this.numOfElements;j++){
			    edgeCount += this.cayleyGraphArray[i][j];
			}
			this.distributionArray[i]=edgeCount;
			edgeCount=0;
		}
	}
	/*
	public int[] selectVertexPair(String option){
		if(option=="maxDistNonClique"){
			int maxValue = this.distributionArray[0];
			
			
			
			
		}
	}*/
	
	
	
	
}