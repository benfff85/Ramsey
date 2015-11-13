package ramsey;

//import ui.GenericFrame;

/**
 * This is the main Controller Class where the program will initiate. It handles
 * the outermost loop of logic which encompasses a framework for the entire
 * application.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class Controller {
	
	CayleyGraph cayleyGraph;
	Logger logger;
	Timer timer;
	CliqueChecker cliqueChecker;
	GraphMutator mutator;
	CumulativeStatistics stats;
	//GraphRotator rotator;
	boolean counterExampleFound;
	
	//GenericFrame frame;
	
	/**
	 * This is the main method for the application. It will initialize the
	 * CayleyGraph object and begin searching for a counter example. A counter
	 * example is defined as a CayleyGraph with no clique (complete subgraph)
	 * within it. This method will loop continually searching and mutating until
	 * a counter example is found.
	 * <p>
	 * It is noted the passing of Strings to methods as done below for the
	 * purposes of switch statements is generally poor practice, however it has
	 * done here to allow for quick modifications and testing of the code
	 * without the use of a GUI.
	 * 
	 * @param args
	 * @return void
	 */
	public Controller(){
		cayleyGraph = new CayleyGraph();
		timer = new Timer();
		logger = new Logger(cayleyGraph, timer);
		cliqueChecker = new CliqueChecker(cayleyGraph, Config.CLIQUE_SIZE);
		mutator = new GraphMutator();
		stats = new CumulativeStatistics(cayleyGraph);
		//rotator = new GraphRotator();
		counterExampleFound = false;
		
		//frame = new GenericFrame(cayleyGraph,logger,timer,this);
		//frame.setVisible(true);
		
		timer.newTimeSet("MUTATE");
		timer.newTimeSet("LOGGER");
		timer.newTimeSet("CLIQUE");
		timer.newTimeSet("ROTATE");
		timer.newTimeSet("STATS");

	}
	
	public void runSearch() throws Exception{
		while (!counterExampleFound) {
			runIteration();
		}
	}
	
	public void runIteration() throws Exception{
		
		// If an initial load or rollback has occurred we will need to identify cliques.
		if (!cayleyGraph.isCliqueIdentified()){
			findCliques();
		}
		
		if(logger.getAnalyzedGraphCount() > 0){
			processMutation();
			findCliques();
		}
		
		processStatistics();
		processLogging();
		
		if (!cayleyGraph.isCliqueIdentified()){
			counterExampleFound = true;
			logger.processPositiveCase(cayleyGraph);
			return;
		} else {
			logger.processCheckpoint();
		}
		
		//frame.refreshData();
		
	}
	
	private void findCliques(){
		timer.startTimer("CLIQUE");
		cliqueChecker.findCliqueParallel(Config.CLIQUE_SEARCH_THREAD_COUNT,"RED");
		cliqueChecker.findCliqueParallel(Config.CLIQUE_SEARCH_THREAD_COUNT,"BLUE");
		timer.endTimer("CLIQUE");
	}
	
	private void processLogging(){
		timer.startTimer("LOGGER");
		logger.logIteration();
		timer.endTimer("LOGGER");
	}
	
	private void processMutation(){
		timer.startTimer("MUTATE");
		System.out.println("Mutating");
		mutator.mutateGraph(cayleyGraph);
		timer.endTimer("MUTATE");
	}
	
	private void processRotation(){
		timer.startTimer("ROTATE");
		//rotator.rotate(cayleyGraph);
		timer.endTimer("ROTATE");
	}
	
	private void processStatistics(){
		timer.startTimer("STATS");
		stats.updateStatistics();
		timer.endTimer("STATS");
	}

}
