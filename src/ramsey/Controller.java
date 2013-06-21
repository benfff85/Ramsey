package ramsey;

public class Controller {
	
	public static void main (String [] args){
		CayleyGraph cayleyGraph = setSize();
		Logger logger = new Logger();
		boolean counterExampleFound = false;

		cayleyGraph.generateRandomGraph();
		while(!counterExampleFound){
			if(cayleyGraph.cliqueChecker("RED")){
				logger.parseCayleyGraph(cayleyGraph);	
				if(logger.getAnalyzedGraphCount()%1000==0){
					printNegativeCase(cayleyGraph, logger);
				}				
			}	
			else if(cayleyGraph.cliqueChecker("BLUE")){
				logger.parseCayleyGraph(cayleyGraph);	
				if(logger.getAnalyzedGraphCount()%1000==0){
					printNegativeCase(cayleyGraph, logger);
				}						
			}
			else{
				counterExampleFound = true;
				System.out.println(cayleyGraph.printCayleyGraph());
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
		return new CayleyGraph(288,8);
		//return new CayleyGraph(14,4);
		//return new CayleyGraph(5,3);
	}

	
	public static void printNegativeCase(CayleyGraph cg, Logger l){
		System.out.println("#######################################################################");
		System.out.println("Graph Count:           " + l.getAnalyzedGraphCount());
		System.out.println("Clique Color:          " + cg.getClique().getColor());
		System.out.println("Clique:                " + cg.getClique().printClique());
		System.out.println("Line Count:            " + cg.printRedBlueCount());
		System.out.println("Distribution:          " + cg.printDistribution("RED"));
		System.out.println("Distribution Summary:  " + cg.printDistributionSummary("RED"));
		System.out.println("Max First Clique ID:   " + l.getMaxFirstCliqueElement());
		System.out.println("Max Clique Sum:        " + l.getMaxCliqueSum());
	}

}
