package ramsey;

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
		CayleyGraph cayleyGraph = setSize("TARGET");
		Logger logger = new Logger();
		Timer timer = new Timer();
		boolean counterExampleFound = false;

		cayleyGraph.generateRandomGraph();
		// cayleyGraph.loadFromFile();

		timer.newTimeSet("MUTATE");
		timer.newTimeSet("LOGGER");
		timer.newTimeSet("CLIQUE");
		timer.newTimeSet("ROTATE");

		while (!counterExampleFound) {

			timer.startTimer("CLIQUE");
			if (!cayleyGraph.cliqueChecker("RED") && !cayleyGraph.cliqueChecker("BLUE")) {
				counterExampleFound = true;
				printPositiveCase(cayleyGraph, logger);
				logger.closeLogFile();
				break;
			}
			timer.endTimer("CLIQUE");

			timer.startTimer("LOGGER");
			logger.parseCayleyGraph(cayleyGraph);
			timer.endTimer("LOGGER");

			if (logger.getAnalyzedGraphCount() % 100000 == 0) {
				printNegativeCase(cayleyGraph, logger, timer, "BOTH");
				timer.clearCumulativeDuration("MUTATE");
				timer.clearCumulativeDuration("LOGGER");
				timer.clearCumulativeDuration("CLIQUE");
				timer.clearCumulativeDuration("ROTATE");
			}

			timer.startTimer("MUTATE");
			mutatate(cayleyGraph);
			timer.endTimer("MUTATE");

			timer.startTimer("ROTATE");
			rotate(cayleyGraph);
			timer.endTimer("ROTATE");
		}
	}

	/**
	 * This method is responsible for rotating the CayleyGraphs vertices to
	 * prevent detection of cliques in the low vertices from causing issues with
	 * edge distribution.
	 * 
	 * @param cg The CayleyGraph which will be rotated
	 * @return void
	 */
	private static void rotate(CayleyGraph cg) {
		 cg.rotateCayleyGraph(1);
		// cg.rotateCayleyGraphParallel(1);
		// cg.rotateCayleyGraphParallel(1,1);
	}

	/**
	 * This method will dictate the method of mutation used on the CayleyGraph
	 * after a counter example is found
	 * 
	 * @param cg The CayleyGraph which will be mutated
	 * @return void
	 */
	private static void mutatate(CayleyGraph cg) {
		cg.mutateGraphTargeted();
		// cg.mutateGraphRandom(1);
		// cg.generateRandomGraph();
	}

	/**
	 * Sets the size of the CayleyGraph for which we will be searching for a
	 * counter example from several common sizes
	 * 
	 * @param size This is the size of the test being run which is selected from
	 *        a predefined list of common sizes. Valid values are:<br>
	 *        TARGET - Clique size 8 in 288 elements<br>
	 *        LARGE - Clique size 4 in 16 elements<br>
	 *        MEDIUM - Clique size 4 in 14 elements<br>
	 *        SMALL - Clique size 3 in 5 elements<br>
	 * @return The CayleyGraph object to begin searching on
	 */
	private static CayleyGraph setSize(String size) {
		if (size == "TARGET") {
			return new CayleyGraph(288, 8); // Target - Very Hard
		} else if (size == "LARGE") {
			return new CayleyGraph(16, 4); // Long Low Memory Consumption R(4) = 18
		} else if (size == "MEDIUM") {
			return new CayleyGraph(14, 4); // Quick Find R(4) = 18
		} else if (size == "SMALL") {
			return new CayleyGraph(5, 3); // Very Quick Find R(3) = 6
		} else {
			return null;
		}

	}

	/**
	 * Outputs statistics in the event of a counter example not being found to
	 * the console, a log file, or both.
	 * 
	 * 
	 * @param cg The CayleyGraph object the process is being applied to
	 * @param l The Logger object tracking the maximum values
	 * @param t The Timer object tracking processing duration
	 * @param outputMethod A String signifying the preferred output method.
	 *        Valid values are "CONSOLE", "FILE", "BOTH"
	 * @return void
	 */
	private static void printNegativeCase(CayleyGraph cg, Logger l, Timer t, String outputMethod) {
		String content =
				"#######################################################################\n" +
						"Graph Count:          " + l.getAnalyzedGraphCount() + "\n" +
						"Clique Color:         " + cg.getClique().getColor() + "\n" +
						"Clique:               " + cg.getClique().printClique() + "\n" +
						"Line Count:           " + cg.printRedBlueCount() + "\n" +
						"Distribution:         " + cg.printDistribution("RED") + "\n" +
						"Distribution Summary: " + cg.printDistributionSummary("RED") + "\n" +
						"Max First Clique ID:  " + l.getMaxFirstCliqueElement() + "\n" +
						"Max Clique Sum:       " + l.getMaxCliqueSum() + "\n" +
						"Weighted Clique Sum   " + l.getMaxWeightedCliqueSum() + "\n" +
						"Time Mutate:          " + t.printCumulativeDuration("MUTATE") + "\n" +
						"Time Logger:          " + t.printCumulativeDuration("LOGGER") + "\n" +
						"Time CliqueCheck:     " + t.printCumulativeDuration("CLIQUE") + "\n" +
						"Time Rotate:          " + t.printCumulativeDuration("ROTATE") + "\n";

		if (outputMethod == "FILE" || outputMethod == "BOTH") {
			l.writeToLogFile(content);
		}

		if (outputMethod == "CONSOLE" || outputMethod == "BOTH") {
			System.out.println(content);
		}

	}

	/**
	 * Outputs the counter example in the event it has been found. This will
	 * write to the log file, email, print to the console and write to a
	 * solution file Ramsey.sol. All the above will be in a format compatible
	 * with Mathematica with the exception of the .sol file. The .sol file will
	 * be comma separated digits so that it may be used as input to the
	 * CayleyGraph.loadFromFile method later.
	 * 
	 * @param cg The CayleyGraph object the process is being applied to (Counter
	 *        Example)
	 * @param l The Logger object responsible for writing to files
	 * @return void
	 */
	private static void printPositiveCase(CayleyGraph cg, Logger l) {
		System.out.println(cg.printCayleyGraphMathematica());
		l.writeToLogFile("SOLUTION:\n" + cg.printCayleyGraphMathematica());
		cg.writeToFile("D:\\", "Ramsey.sol");
		cg.emailCayleyGraph();
	}
}
