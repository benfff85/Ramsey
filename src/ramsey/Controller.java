package ramsey;

import ui.GenericFrame;

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
	GraphRotator rotator;
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
		logger = new Logger();
		timer = new Timer();
		cliqueChecker = new CliqueChecker(cayleyGraph);
		mutator = new GraphMutator();
		rotator = new GraphRotator();
		counterExampleFound = false;
		
		//frame = new GenericFrame(cayleyGraph,logger,timer,this);
		//frame.setVisible(true);
		
		timer.newTimeSet("MUTATE");
		timer.newTimeSet("LOGGER");
		timer.newTimeSet("CLIQUE");
		timer.newTimeSet("ROTATE");
	}
	
	public void runSearch(){
		
		while (!counterExampleFound) {

			timer.startTimer("CLIQUE");
			if (!cliqueChecker.findClique("RED") && !cliqueChecker.findClique("BLUE")) {
				counterExampleFound = true;
				logger.processPositiveCase(cayleyGraph);
				logger.closeLogFile();
				break;
			}
			timer.endTimer("CLIQUE");

			timer.startTimer("LOGGER");
			logger.processNegativeCase(cayleyGraph, timer);
			timer.endTimer("LOGGER");

			//frame.refreshData();
			
			timer.startTimer("MUTATE");
			mutator.mutateGraph(cayleyGraph);
			timer.endTimer("MUTATE");

			timer.startTimer("ROTATE");
			rotator.rotate(cayleyGraph);
			timer.endTimer("ROTATE");
		}
	}
	
	public void runOneIteration(){
		timer.startTimer("CLIQUE");
		if (!cliqueChecker.findClique("RED") && !cliqueChecker.findClique("BLUE")) {
			counterExampleFound = true;
			logger.processPositiveCase(cayleyGraph);
			logger.closeLogFile();
			return;
		}
		timer.endTimer("CLIQUE");

		timer.startTimer("LOGGER");
		logger.processNegativeCase(cayleyGraph, timer);
		timer.endTimer("LOGGER");

		//frame.refreshData();
		
		timer.startTimer("MUTATE");
		mutator.mutateGraph(cayleyGraph);
		timer.endTimer("MUTATE");

		timer.startTimer("ROTATE");
		rotator.rotate(cayleyGraph);
		timer.endTimer("ROTATE");
	}

}
