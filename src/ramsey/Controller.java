package ramsey;

public class Controller {
	
	public static void main (String [] args){
		CayleyGraph cayleyGraph = setSize();
		Logger logger = new Logger();
		Timer timer = new Timer();
		boolean counterExampleFound = false;

		cayleyGraph.generateRandomGraph();
		timer.newTimeSet("MUTATE");
		timer.newTimeSet("LOGGER");
		timer.newTimeSet("CLIQUE");
		timer.newTimeSet("ROTATE");
		
		while(!counterExampleFound){
			
			timer.startTimer("CLIQUE");
			if(!cayleyGraph.cliqueChecker("RED") && !cayleyGraph.cliqueChecker("BLUE")){
				counterExampleFound = true;
				printPositiveCase(cayleyGraph);
				break;
			}
			timer.endTimer("CLIQUE");

			timer.startTimer("LOGGER");
			logger.parseCayleyGraph(cayleyGraph);
			timer.endTimer("LOGGER");
			
			if(logger.getAnalyzedGraphCount()%1000==0){
				printNegativeCase(cayleyGraph, logger, timer);
				timer.clearCumulativeDuration("MUTATE");
				timer.clearCumulativeDuration("LOGGER");
				timer.clearCumulativeDuration("CLIQUE");
				timer.clearCumulativeDuration("ROTATE");
			}		
			
			timer.startTimer("MUTATE");
			mutatate(cayleyGraph);
			timer.endTimer("MUTATE");
			
			timer.startTimer("ROTATE");
			//cayleyGraph.rotateCayleyGraph(1);
			timer.endTimer("ROTATE");
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

	
	public static void printNegativeCase(CayleyGraph cg, Logger l, Timer t){
		System.out.println("#######################################################################");
		System.out.println("Graph Count:           " + l.getAnalyzedGraphCount());
		System.out.println("Clique Color:          " + cg.getClique().getColor());
		System.out.println("Clique:                " + cg.getClique().printClique());
		System.out.println("Line Count:            " + cg.printRedBlueCount());
		System.out.println("Distribution:          " + cg.printDistribution("RED"));
		System.out.println("Distribution Summary:  " + cg.printDistributionSummary("RED"));
		System.out.println("Max First Clique ID:   " + l.getMaxFirstCliqueElement());
		System.out.println("Max Clique Sum:        " + l.getMaxCliqueSum());
		System.out.println("Time Mutate:           " + t.printCumulativeDuration("MUTATE"));
		System.out.println("Time Logger:           " + t.printCumulativeDuration("LOGGER"));
		System.out.println("Time CliqueCheck:      " + t.printCumulativeDuration("CLIQUE"));
		System.out.println("Time Rotate:           " + t.printCumulativeDuration("ROTATE"));
	}
	
	public static void printPositiveCase(CayleyGraph cg){
		System.out.println(cg.printCayleyGraph());
		// Add email here
	}

}
