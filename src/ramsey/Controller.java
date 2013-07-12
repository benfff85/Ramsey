package ramsey;

public class Controller {
	
	public static void main (String [] args) {
		CayleyGraph cayleyGraph = setSize();
		Logger logger = new Logger();
		Timer timer = new Timer();
		boolean counterExampleFound = false;

		cayleyGraph.generateRandomGraph();
		//cayleyGraph.loadFromFile();
		
		timer.newTimeSet("MUTATE");
		timer.newTimeSet("LOGGER");
		timer.newTimeSet("CLIQUE");
		timer.newTimeSet("ROTATE");
		
		while(!counterExampleFound){
			
			timer.startTimer("CLIQUE");
			if(!cayleyGraph.cliqueChecker("RED") && !cayleyGraph.cliqueChecker("BLUE")){
				counterExampleFound = true;
				printPositiveCase(cayleyGraph, logger);
				logger.closeLogFile();
				break;
			}
			timer.endTimer("CLIQUE");

			timer.startTimer("LOGGER");
			logger.parseCayleyGraph(cayleyGraph);
			timer.endTimer("LOGGER");
			
			if(logger.getAnalyzedGraphCount()%100000==0){
				printNegativeCase(cayleyGraph, logger, timer);
				cayleyGraph.writeToFile("D:\\", "Ramsey.chk");
				timer.clearCumulativeDuration("MUTATE");
				timer.clearCumulativeDuration("LOGGER");
				timer.clearCumulativeDuration("CLIQUE");
				timer.clearCumulativeDuration("ROTATE");
			}		
			
			timer.startTimer("MUTATE");
			mutatate(cayleyGraph);
			timer.endTimer("MUTATE");
			
			timer.startTimer("ROTATE");
			cayleyGraph.rotateCayleyGraph(1);
			//cayleyGraph.rotateCayleyGraphParallel(1);
			timer.endTimer("ROTATE");
		}
	}
	
	
	public static void mutatate(CayleyGraph cg){
		cg.mutateGraphTargeted();
		//cg.mutateGraphRandom(1);
		//cg.generateRandomGraph();
	}
	
	
	public static CayleyGraph setSize(){
		return new CayleyGraph(288,8);//      Target - Very Hard
		//return new CayleyGraph(16,4); //      
		//return new CayleyGraph(14,4); //      Quick Find R(4) = 18
		//return new CayleyGraph(5,3);  // Very Quick Find R(3) = 6
	}

	
	public static void printNegativeCase(CayleyGraph cg, Logger l, Timer t){
		
		l.writeToLogFile("#######################################################################\n");
		l.writeToLogFile("Graph Count:          " + l.getAnalyzedGraphCount() + "\n");
		l.writeToLogFile("Clique Color:         " + cg.getClique().getColor() + "\n");
		l.writeToLogFile("Clique:               " + cg.getClique().printClique() + "\n");
		l.writeToLogFile("Line Count:           " + cg.printRedBlueCount() + "\n");
		l.writeToLogFile("Distribution:         " + cg.printDistribution("RED") + "\n");
		l.writeToLogFile("Distribution Summary: " + cg.printDistributionSummary("RED") + "\n");
		l.writeToLogFile("Max First Clique ID:  " + l.getMaxFirstCliqueElement() + "\n");
		l.writeToLogFile("Max Clique Sum:       " + l.getMaxCliqueSum() + "\n");
		l.writeToLogFile("Time Mutate:          " + t.printCumulativeDuration("MUTATE") + "\n");
		l.writeToLogFile("Time Logger:          " + t.printCumulativeDuration("LOGGER") + "\n");
		l.writeToLogFile("Time CliqueCheck:     " + t.printCumulativeDuration("CLIQUE") + "\n");
		l.writeToLogFile("Time Rotate:          " + t.printCumulativeDuration("ROTATE") + "\n");
		
		/*
		System.out.println("#######################################################################\n");
		System.out.println("Graph Count:          " + l.getAnalyzedGraphCount());
		System.out.println("Clique Color:         " + cg.getClique().getColor());
		System.out.println("Clique:               " + cg.getClique().printClique());
		System.out.println("Line Count:           " + cg.printRedBlueCount());
		System.out.println("Distribution:         " + cg.printDistribution("RED"));
		System.out.println("Distribution Summary: " + cg.printDistributionSummary("RED"));
		System.out.println("Max First Clique ID:  " + l.getMaxFirstCliqueElement());
		System.out.println("Max Clique Sum:       " + l.getMaxCliqueSum());
		System.out.println("Time Mutate:          " + t.printCumulativeDuration("MUTATE"));
		System.out.println("Time Logger:          " + t.printCumulativeDuration("LOGGER"));
		System.out.println("Time CliqueCheck:     " + t.printCumulativeDuration("CLIQUE"));
		System.out.println("Time Rotate:          " + t.printCumulativeDuration("ROTATE"));
		*/
	}
	
	public static void printPositiveCase(CayleyGraph cg, Logger l){
		System.out.println(cg.printCayleyGraphMathematica());
		//cg.emailCayleyGraph();
		l.writeToLogFile("SOLUTION:\n" + cg.printCayleyGraphMathematica());
		cg.writeToFile("D:\\", "Ramsey.sol");
	}

}
