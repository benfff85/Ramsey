package com.benajaminleephoto.ramsey.common;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benajaminleephoto.ramsey.cliqueChecker.CliqueCheckerFactory;
import com.benajaminleephoto.ramsey.mutate.GraphMutatorFactory;

/**
 * This is the main Controller Class where the program will initiate. It handles the outermost loop
 * of logic which encompasses a framework for the entire application.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class Controller {

    private CayleyGraph cayleyGraph;
    private RamseyLogger ramseyLogger;
    private Timer timer;
    private CliqueCheckerFactory cliqueCheckerFactory;
    private GraphMutatorFactory graphMutatorFactory;
    private CumulativeStatistics stats;
    private boolean counterExampleFound;
    private static final Logger logger = LoggerFactory.getLogger(Controller.class.getName());


    /**
     * This is the main method for the application. It will initialize the CayleyGraph object and
     * begin searching for a counter example. A counter example is defined as a CayleyGraph with no
     * clique (complete subgraph) within it. This method will loop continually searching and
     * mutating until a counter example is found.
     * <p>
     * It is noted the passing of Strings to methods as done below for the purposes of switch
     * statements is generally poor practice, however it has done here to allow for quick
     * modifications and testing of the code without the use of a GUI.
     */
    public Controller() {

        logger.info("Beginning Controller initialization.");

        cayleyGraph = new CayleyGraph();
        ApplicationContext.setCayleyGraph(cayleyGraph);
        ApplicationContext.setGenerator(new Random());
        timer = new Timer();
        stats = new CumulativeStatistics(cayleyGraph);
        ramseyLogger = new RamseyLogger(cayleyGraph, timer, stats);
        cliqueCheckerFactory = new CliqueCheckerFactory();
        graphMutatorFactory = new GraphMutatorFactory();
        counterExampleFound = false;

        timer.newTimeSet("MUTATE");
        timer.newTimeSet("LOGGER");
        timer.newTimeSet("CLIQUE");
        timer.newTimeSet("STATS");

        logger.info("Logger initialization successful.");
    }


    public void runSearch() throws Exception {
        logger.info("Beginning search...");
        while (!counterExampleFound) {
            runIteration();
        }
    }


    public void runIteration() throws Exception {

        // If an initial load or rollback has occurred we will need to identify cliques.
        if (!cayleyGraph.isCliqueIdentified()) {
            findCliques();
        }

        if (stats.getAnalyzedGraphCount() > 0) {
            processMutation();
            findCliques();
        }

        processStatistics();
        processLogging();

        if (!cayleyGraph.isCliqueIdentified()) {
            counterExampleFound = true;
            ramseyLogger.processPositiveCase(cayleyGraph);
            return;
        } else {
            ramseyLogger.processCheckpoint();
        }

    }


    private void findCliques() throws Exception {
        timer.startTimer("CLIQUE");
        cliqueCheckerFactory.getCliqueChecker().findClique("RED");
        cliqueCheckerFactory.getCliqueChecker().findClique("BLUE");
        timer.endTimer("CLIQUE");
    }


    private void processLogging() {
        timer.startTimer("LOGGER");
        ramseyLogger.logIteration();
        timer.endTimer("LOGGER");
    }


    private void processMutation() {
        timer.startTimer("MUTATE");
        graphMutatorFactory.getGraphMutator().mutateGraph();
        timer.endTimer("MUTATE");
    }


    private void processStatistics() {
        timer.startTimer("STATS");
        stats.updateStatistics();
        timer.endTimer("STATS");
    }

}
