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
	public static void main(String[] args) {
		Logger logger = new Logger();
		Timer timer = new Timer();
		GraphMutator mutator = new GraphMutator();
		GraphRotator rotator = new GraphRotator();
		boolean counterExampleFound = false;

		CayleyGraph cayleyGraph = new CayleyGraph();
		
		GenericFrame f = new GenericFrame(cayleyGraph,logger,timer);
		f.setVisible(true);
		
		
		timer.newTimeSet("MUTATE");
		timer.newTimeSet("LOGGER");
		timer.newTimeSet("CLIQUE");
		timer.newTimeSet("ROTATE");

		while (!counterExampleFound) {

			timer.startTimer("CLIQUE");
			if (!cayleyGraph.checkForClique("RED") && !cayleyGraph.checkForClique("BLUE")) {
				counterExampleFound = true;
				logger.processPositiveCase(cayleyGraph);
				logger.closeLogFile();
				break;
			}
			timer.endTimer("CLIQUE");

			timer.startTimer("LOGGER");
			logger.processNegativeCase(cayleyGraph, timer);
			timer.endTimer("LOGGER");

			timer.startTimer("MUTATE");
			mutator.mutateGraph(cayleyGraph);
			timer.endTimer("MUTATE");

			timer.startTimer("ROTATE");
			rotator.rotate(cayleyGraph);
			timer.endTimer("ROTATE");
			
			f.refreshData();
		}
	}
	
	public void executeSearch(){
		System.out.println("TEST");
	}
}
