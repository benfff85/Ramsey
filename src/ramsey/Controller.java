package ramsey;

public class Controller {
	
	public static void main (String [] args){
		CayleyGraph cayleyGraph;
		
		cayleyGraph = setSize();

		cayleyGraph.generateRandomGraph();
		while(true){
			if(!cayleyGraph.cliqueChecker("RED") && !cayleyGraph.cliqueChecker("BLUE")){
				System.out.println(cayleyGraph.printCayleyGraph());	
			}
			else{
				printNegativeCase(cayleyGraph);
			}
			mutatate(cayleyGraph);
		}
	}
	
	
	public static void mutatate(CayleyGraph cg){
		cg.mutateGraphTargeted();
		//cg.mutateGraphRandom(1);
		//cg.generateRandomGraph();
	}
	
	
	public static CayleyGraph setSize(){
		//return new CayleyGraph(288,8);
		return new CayleyGraph(14,4);
		//return new CayleyGraph(5,3);
	}

	
	public static void printNegativeCase(CayleyGraph cg){
		System.out.println("########################################################");
		System.out.println("Clique Color: " + cg.getClique().getColor());
		System.out.println("Clique:       " + cg.getClique().printClique());
		System.out.println("Line Count:   " + cg.printRedBlueCount());
		System.out.println("Distribution: " + cg.printDistribution("RED"));
	}

}
