package ramsey;

public class test {
	
	public static void main (String [] args){
		CayleyGraph cayleyGraph;
		
		//cayleyGraph = new CayleyGraph(288,8);
		cayleyGraph = new CayleyGraph(5,3);

		cayleyGraph.generateRandomGraph();
		while(cayleyGraph.cliqueChecker("RED") || cayleyGraph.cliqueChecker("BLUE")){
			cayleyGraph.generateRandomGraph();
		}
		
		cayleyGraph.printCayleyGraph();
	}

}
