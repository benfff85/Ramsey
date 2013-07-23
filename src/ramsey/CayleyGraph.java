package ramsey;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
 
/**
 * This class represents the cayley graph which is a graph comprised of
 * numOfElements elements. Each elements (also referred to as vertex) is
 * connected to every other element via an edge. The CayleyGraph will be able to
 * initialize itself, check itself for complete subgraphs, mutate itself and
 * write itself to files which can be loaded later.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class CayleyGraph {

	private int numOfElements;
	private Vertex[] cayleyGraphArray;
	private Clique clique;
	private CliqueChecker cliqueChecker;
	
	/**
	 * This is the main constructor for the CayleyGraph. It will initialize all
	 * global variables given.
	 * 
	 * @param numOfElements The total number of elements (vertices) comprising
	 *        the CayleyGraph.
	 * @param cliqueSize The number of elements (vertices) in the complete
	 *        subgraph (clique) we will be searching for.
	 */
	public CayleyGraph(int numOfElements, int cliqueSize) {
		this.numOfElements = numOfElements;
		this.cayleyGraphArray = new Vertex[numOfElements];
		this.clique = new Clique(cliqueSize);
		this.cliqueChecker = new CliqueChecker(cliqueSize, numOfElements);
	}
	
	/**
	 * This will return the number of elements comprising this CayleyGraph. Said
	 * another way it will return this.cayleyGraphArray.length.
	 * 
	 * @return The number of elements comprising this CayleyGraph.
	 */
	public int getNumOfElements() {
		return this.numOfElements;
	}
	
	/**
	 * This will return the Vertex array of all vertices in this CayleyGraph.
	 * 
	 * @return The Vertex array of all vertices in this CayleyGraph.
	 */
	public Vertex[] getCayleyGraphArray() {
		return this.cayleyGraphArray;
	}
	
	/**
	 * This will leverage the cliqueChecker object to check this CayleyGraph for
	 * a complete subgraph of a given color. Clique size is determined by
	 * this.clique which was created during the initialization of this
	 * CayleyGraph.
	 * 
	 * @param color This is the color of the subgraph cliqueChecker will be
	 *        looking for.
	 * @return This method will return true if a complete subgraph of the input
	 *         color was found. Otherwise it will return false.
	 */
	public boolean checkForClique(String color) {
		return cliqueChecker.findClique(this, color);
	}

	/**
	 * This will Generate a random CayleyGraph where the total number of edges
	 * of each color will be equal. This is generally only done once at the
	 * beginning of processing after which the initialized graph will simply be
	 * mutated.
	 * 
	 * @return void
	 */
	public void generateRandomGraph() {
		int redCount = ((this.numOfElements) * (this.numOfElements - 1)) / 4;
		int blueCount = redCount;

		// Initialize all vertices with valid IDs
		for (int i = 0; i < this.numOfElements; i++) {
			this.cayleyGraphArray[i] = new Vertex(i, this.numOfElements);
		}

		Random generator = new Random();
		for (int i = 0; i < this.numOfElements; i++) {
			for (int j = (i + 1); j < this.numOfElements; j++) {
				if (redCount == 0) {
					Edge edge = new Edge(this.cayleyGraphArray[i], this.cayleyGraphArray[j], "BLUE");
					this.cayleyGraphArray[i].setEdge(edge);
					this.cayleyGraphArray[j].setEdge(edge);
				}
				else if (blueCount == 0) {
					Edge edge = new Edge(this.cayleyGraphArray[i], this.cayleyGraphArray[j], "RED");
					this.cayleyGraphArray[i].setEdge(edge);
					this.cayleyGraphArray[j].setEdge(edge);
				} else {
					if (generator.nextBoolean() == false) {
						Edge edge = new Edge(this.cayleyGraphArray[i], this.cayleyGraphArray[j], "BLUE");
						this.cayleyGraphArray[i].setEdge(edge);
						this.cayleyGraphArray[j].setEdge(edge);
						blueCount--;
					} else {
						Edge edge = new Edge(this.cayleyGraphArray[i], this.cayleyGraphArray[j], "RED");
						this.cayleyGraphArray[i].setEdge(edge);
						this.cayleyGraphArray[j].setEdge(edge);
						redCount--;
					}
				}
			}
		}
	}
	
	
	/**
	 * This will generate and return a String representing the CayleyGraph in a
	 * format compatible with Mathematica.
	 * 
	 * @return String representing the CayleyGraph in a format compatible with
	 *         Mathematica.
	 */
	public String printCayleyGraphMathematica() {
		String output = "";
		output += "GraphPlot[{";

		for (int i = 0; i < this.numOfElements; i++) {
			output += "{";
			for (int j = 0; j < this.numOfElements; j++) {

				if (i != j && this.cayleyGraphArray[i].getEdge(this.cayleyGraphArray[j]).getColor() == "RED") {
					output += "1, ";
				} else {
					output += "0, ";
				}
			}
			output = output.substring(0, output.length() - 2);
			output += "},";
		}
		output = output.substring(0, output.length() - 1);
		output += "}, Method -> CircularEmbedding]";
		return output;
	}

	/**
	 * This will generate and return a String representing the CayleyGraph in a
	 * basic common separated value format.
	 * 
	 * @return String representing the CayleyGraph in a basic common separated
	 *         value format.
	 */
	public String printCayleyGraphBasic() {
		String output = "";

		for (int i = 0; i < this.numOfElements; i++) {
			output += "";
			for (int j = 0; j < this.numOfElements; j++) {

				if (i != j && this.cayleyGraphArray[i].getEdge(this.cayleyGraphArray[j]).getColor() == "RED") {
					output += "1,";
				} else {
					output += "0,";
				}
			}
			output = output.substring(0, output.length() - 1);
			output += "\n";
		}
		return output;
	}
	
	
	/**
	 * This will email a representation of the CayleyGraph in a format
	 * compatible with Mathematica.
	 * 
	 * @return void
	 */
	public void emailCayleyGraph() {
		String[] arguments = new String[3];

		arguments[0] = "Ramsey Solution found to R[" + this.numOfElements + "," + this.numOfElements + "]";
		arguments[1] = this.printCayleyGraphMathematica();

		@SuppressWarnings("unused")
		SendEmail email = new SendEmail(arguments);
	}
	
	/**
	 * This will print the total count of RED and BLUE edges in the CayleyGraph.
	 * <p>
	 * The formatting of the output will be in the form of:<br>
	 * [countRed:countBlue]
	 * <p>
	 * Note: They should always be equal if algorithms are working as expected.
	 * 
	 * @return A human readable string string of the total number of Red and
	 *         Blue Edges in this CayleyGraph.
	 */
	public String printRedBlueCount() {
		int countRed = 0;
		int countBlue = 0;
		for (int i = 0; i < this.numOfElements; i++) {
			for (int j = i + 1; j < this.numOfElements; j++) {
				if (this.cayleyGraphArray[i].getEdge(this.cayleyGraphArray[j]).getColor() == "RED") {
					countRed++;
				} else {
					countBlue++;
				}
			}
		}
		return "[RED:" + countRed + "] [BLUE:" + countBlue + "]";
	}
	
	/**
	 * This Will a human readable string representing the color distribution of
	 * this CayleyGraph. The color distribution will consist of numOfElements
	 * integers each one representing the number of Edges of a given color
	 * connected to each Vertex.<p>
	 * The formatting of the output will be in the form of:<br>
	 * [#,#,#,...,#,#,#]
	 * 
	 * @param color This is the color of the Edges which will be counted in
	 *        calculating this distribution.
	 * @return A human readable string representing the color distribution of
	 *         this CayleyGraph.
	 */
	public String printDistribution(String color) {
		int count;
		String output = "[";

		for (int i = 0; i < this.numOfElements; i++) {
			count = this.cayleyGraphArray[i].getEdgeCount(color);
			output += count + ",";
		}
		output = output.substring(0, output.length() - 1);
		output += "]";
		return output;
	}
		
	/**
	 * Randomly flip countOfSwaps pairs of edges, one edge of each color per
	 * pair to maintain balance.
	 * 
	 * @return void
	 */
	public void mutateGraphRandom(int countOfSwaps) {
		Edge redEdge = null;
		Edge blueEdge = null;

		for (int i = 0; i < countOfSwaps; i++) {

			redEdge = this.getRandomEdge("RED");
			blueEdge = this.getRandomEdge("BLUE");

			redEdge.setColor("BLUE");
			blueEdge.setColor("RED");
		}
	}

	/**
	 * This will flip the color of one edge from the identified clique along
	 * with one random edge of the opposite color to maintain balance.
	 * 
	 * @return void
	 */
	public void mutateGraphTargeted() {
		Random generator = new Random();
		boolean redSelected = false;
		boolean blueSelected = false;
		Edge redEdge = null;
		Edge blueEdge = null;
		int x, y;

		// Select the edge from clique to swap
		x = 0;
		y = 0;
		while (x == y) {
			x = generator.nextInt(this.clique.getCliqueSize());
			y = generator.nextInt(this.clique.getCliqueSize());
		}

		if (this.clique.getCliqueVertexByPosition(x).getEdge(this.clique.getCliqueVertexByPosition(y)).getColor() == "RED") {
			redEdge = this.clique.getCliqueVertexByPosition(x).getEdge(this.clique.getCliqueVertexByPosition(y));
			redSelected = true;
		}
		else {
			blueEdge = this.clique.getCliqueVertexByPosition(x).getEdge(this.clique.getCliqueVertexByPosition(y));
			blueSelected = true;
		}

		// Select the edge of opposite color to swap as well
		if (redSelected) {
			blueEdge = this.getRandomEdge("BLUE");
		}
		else if (blueSelected) {
			redEdge = this.getRandomEdge("RED");
		}

		redEdge.setColor("BLUE");
		blueEdge.setColor("RED");
	}
	
	/**
	 * This will return an edge from this CayleyGraph based on the vertex IDs of
	 * the two vertices it connects.
	 * 
	 * @param vertexIdA The ID of a Vertex which is connected to one end point
	 *        of the desired edge.
	 * @param vertexIdB The ID of a Vertex which is connected to the other end
	 *        point of the desired edge.
	 * @return The Edge object connecting the two vertices having the vertex IDs
	 *         given as input.
	 */
	public Edge getEdgeByVertexIds(int vertexIdA, int vertexIdB) {
		return this.cayleyGraphArray[vertexIdA].getEdge(vertexIdB);
	}

	/**
	 * This will return an edge from this CayleyGraph based on the two vertices
	 * it connects.
	 * 
	 * @param vertexA A Vertex which is connected to one end point of the
	 *        desired edge.
	 * @param vertexB A Vertex which is connected to the other end point of the
	 *        desired edge.
	 * @return The Edge object connecting the two vertices given as input.
	 */
	public Edge getEdgeByVertices(Vertex vertexA, Vertex vertexB) {
		return vertexA.getEdge(vertexB);
	}

	/**
	 * This will return the Clique identified for this CayleyGraph.
	 * 
	 * @return The Clique for this CayleyGraph
	 */
	public Clique getClique() {
		return this.clique;
	}

	/**
	 * Prints a distribution summary for a given input color. The distribution
	 * summary will describe how many edges of a given color are connected to
	 * the first half of the elements and compare this to the number of edges of
	 * the same color connected to the second half of the elements.
	 * 
	 * @param color This is the color which will be counted when determining the
	 *        distribution.
	 * @return A human readable String of the form [x:y]<br>
	 *         x = number of edges of color "color" in the first half of the
	 *         Cayley Graph<br>
	 *         y = number of edges of color "color" in the second half of the
	 *         Cayley Graph
	 */
	public String printDistributionSummary(String color) {
		int firstHalfCount = 0;
		int secondHalfCount = 0;

		for (int i = 0; i < this.numOfElements / 2; i++) {
			firstHalfCount += this.cayleyGraphArray[i].getEdgeCount(color);
		}
		for (int i = this.numOfElements / 2; i < this.numOfElements; i++) {
			secondHalfCount += this.cayleyGraphArray[i].getEdgeCount(color);
		}

		return "[" + firstHalfCount + ":" + secondHalfCount + "]";
	}
	
	/**
	 * Get a random edge object of a given color from the cayleyGraph.
	 * 
	 * @param color This defines what color edge will be returned at random.
	 * @return A random edge of the given color specified by the input.
	 */
	public Edge getRandomEdge(String color) {
		Random generator = new Random();
		int x = 0;
		int y = 0;
		while (x == y || getEdgeByVertexIds(x, y).getColor() != color) {
			x = generator.nextInt(this.numOfElements);
			y = generator.nextInt(this.numOfElements);
		}
		return getEdgeByVertexIds(x, y);
	}
	
	/**
	 * Rotate all the vertices by a factor of rotationCount then reassign all
	 * vertexIDs. This will be done serially
	 * 
	 * @param rotationCount This is the number of times each Vertex will be
	 *        shifted. Generally this is just 1.
	 * @return void
	 */
	public void rotateCayleyGraph(int rotationCount) {
		Vertex swap;
		
		// Shift all vertices
		for (int count = 0; count < rotationCount; count++) {
			
			swap = this.cayleyGraphArray[0];
			System.arraycopy(this.cayleyGraphArray, 1, this.cayleyGraphArray, 0, this.numOfElements - 1);
			this.cayleyGraphArray[this.numOfElements - 1] = swap;

			for (int i = 0; i < this.numOfElements; i++) {
				this.cayleyGraphArray[i].rotateEdges();
			}
		}
		// Update IDs so that this.CayleyGraph[x] still has vertexId x
		for (int i = 0; i < this.numOfElements; i++) {
			this.cayleyGraphArray[i].updateId(i);
		}
	}
	
	/**
	 * Rotate all the vertices by a factor of rotationCount then reassign all
	 * vertexIDs. This will be done in parallel with the number of concurrent
	 * threads being defined by the input maxThreads variable.
	 * 
	 * @param rotationCount This is the number of times each Vertex will be
	 *        shifted. Generally this is just 1.
	 * @param maxThreads This is the number of threads that will execute the
	 *        rotation concurrently.
	 * @return void
	 */
	public void rotateCayleyGraphParallel(int rotationCount, int maxThreads) {
		Vertex swap;
		RotateEdgeThread[] threads = new RotateEdgeThread[maxThreads];

		// Shift all vertices
		for (int count = 0; count < rotationCount; count++) {
			swap = this.cayleyGraphArray[0];
			System.arraycopy(this.cayleyGraphArray, 1, this.cayleyGraphArray, 0, this.numOfElements - 1);
			this.cayleyGraphArray[this.numOfElements - 1] = swap;

			// Shift all edges for each vertex
			// Initialize Threads
			for (int j = 0; j < maxThreads; j++) {
				threads[j] = new RotateEdgeThread(this.cayleyGraphArray, j, maxThreads);
			}

			// Sync up threads
			for (int j = 0; j < maxThreads; j++) {
				try {
					threads[j].join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Update IDs so that this.CayleyGraph[x] still has vertexId x
			for (int i = 0; i < this.numOfElements; i++) {
				this.cayleyGraphArray[i].updateId(i);
			}
		}
	}
	
	class RotateEdgeThread extends Thread{
		int threadId;
		int maxThreads;
		Vertex[] vertexArray;
		
		RotateEdgeThread(Vertex[] vertexArray, int threadId, int maxThreads){
			this.threadId = threadId;
			this.maxThreads = maxThreads;
			this.vertexArray = vertexArray;
			start();
		}
		
		public void run() {
			for (int i = this.threadId; i < vertexArray.length; i += this.maxThreads) {
				this.vertexArray[i].rotateEdges();
			}
		}
	}
	
	/**
	 * This will output the cayleyGraph to a file which can later be loaded. The
	 * file will be comma separated values as defined by printCayleyGraphBasic()
	 * 
	 * @param filePath This is the file path where the file should be saved.
	 * @param fileName This is the desired name of the file.
	 * @return void
	 */
	public void writeToFile(String filePath, String fileName) {
		File file;
		FileWriter fw;
		BufferedWriter bw;

		String content = this.printCayleyGraphBasic();

		// Write the "content" string to file
		try {
			file = new File(filePath + fileName + "");
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

			bw.write(content);

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will initialize the cayleyGraph from a checkpoint file
	 * assuming the data in the checkpoint file is formatted as defined in the
	 * printCayleyGraphBasic() method in this Class.
	 * 
	 * @return void
	 */
	public void loadFromFile() {
		String inputLine;
		StringTokenizer st;
		JFileChooser chooser = new JFileChooser();
		int retval;
		int nextInt;

		// Initialize all vertices with valid IDs
		for (int i = 0; i < this.numOfElements; i++) {
			this.cayleyGraphArray[i] = new Vertex(i, this.numOfElements);
		}

		// Select a file
		retval = chooser.showOpenDialog(null);

		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			BufferedReader buffRead;
			try {
				buffRead = new BufferedReader(new FileReader(file));

				// Loop through file creating edges as we go
				for (int i = 0; i < this.numOfElements; i++) {
					inputLine = buffRead.readLine();
					st = new StringTokenizer(inputLine, ",");

					for (int j = 0; j < this.numOfElements; j++) {
						nextInt = Integer.parseInt(st.nextToken());
						if (j > i) {
							if (nextInt == 0) {
								Edge edge = new Edge(this.cayleyGraphArray[i], this.cayleyGraphArray[j], "BLUE");
								this.cayleyGraphArray[i].setEdge(edge);
								this.cayleyGraphArray[j].setEdge(edge);
							}
							else {
								Edge edge = new Edge(this.cayleyGraphArray[i], this.cayleyGraphArray[j], "RED");
								this.cayleyGraphArray[i].setEdge(edge);
								this.cayleyGraphArray[j].setEdge(edge);
							}
						}
					}
				}
				buffRead.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("IO Error, returning null");
			}
		}
	}
	
	/**
	 * This will check if the cayleyGraph is still symmetric as expected. This
	 * is to say the Edge connecting Vertex A to Vertex B should be the same
	 * Edge which is connecting Vertex B to Vertex A. This will be used for
	 * debugging only.
	 * 
	 * @return This will return a True/False value indicating if the graph is
	 *         symmetric as expected.
	 */
	public boolean isSymmetric() {
		for (int i = 0; i < this.numOfElements; i++) {
			for (int j = i + 1; j < this.numOfElements; j++) {
				if (this.cayleyGraphArray[i].edges[j] != this.cayleyGraphArray[j].edges[i]) {
					return false;
				}
			}
		}
		return true;
	}
}
