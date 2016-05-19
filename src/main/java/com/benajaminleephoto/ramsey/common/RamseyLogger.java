package com.benajaminleephoto.ramsey.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class will help facilitate logging to a file as well as tracking various values to gauge
 * performance and progress.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class RamseyLogger {

    private CayleyGraph cayleyGraph;
    private Timer timer;
    private CumulativeStatistics stats;
    private CliqueCollectionSnapshot cliqueCollectionSnapshot;
    private static final Logger logger = LoggerFactory.getLogger(RamseyLogger.class.getName());


    /**
     * This is the main Logger constructor which will initialize tracked values to 0.
     * 
     * @param cayleyGraph The Cayley Graph being checked.
     * @param timer A time which can be polled to query performance.
     * @param stats Statistics object which can be pulled for cumulative statistics of the run.
     */
    public RamseyLogger(CayleyGraph cayleyGraph, Timer timer, CumulativeStatistics stats) {
        this.cayleyGraph = cayleyGraph;
        this.timer = timer;
        this.stats = stats;
        cliqueCollectionSnapshot = new CliqueCollectionSnapshot();
    }


    /**
     * Outputs the counter example in the event it has been found. This will write to the log file,
     * email, print to the console and write to a solution file Ramsey.sol. All the above will be in
     * a format compatible with Mathematica with the exception of the .sol file. The .sol file will
     * be comma separated digits so that it may be used as input to the CayleyGraph.loadFromFile
     * method later.
     * 
     * @param cayleyGraph The CayleyGraph object the process is being applied to (Counter Example)
     */
    public void processPositiveCase(CayleyGraph cayleyGraph) {
        logger.info("SOLUTION: " + cayleyGraph.printCayleyGraphMathematica());
        GraphFileWriter.writeSolutionFile();
        // cayleyGraph.emailCayleyGraph();
    }


    /**
     * This will determine if a checkpoint is required and if so create it. At the moment
     * checkpoints will be created when a CayleyGraph with a new minimum number of cliques is found.
     */
    public void processCheckpoint() {
        if (stats.getMinCliqueCount() == cayleyGraph.getCliqueCollection().getCliqueCount()) {
            GraphFileWriter.writeMaxFile();
            cliqueCollectionSnapshot.clearCliqueCollectionArray();
            cliqueCollectionSnapshot.populateSnapshot(cayleyGraph.getCliqueCollection());
            if (Config.LAUNCH_METHOD != LAUNCH_TYPE.OPEN_FROM_FILE || stats.getAnalyzedGraphCount() != 1) {
                GraphFileWriter.writeCheckpointFile();
            }
        } else {
            logger.info("Rolling Back");
            cayleyGraph.rollback(Config.CHKPT_FILE_PATH + "Ramsey_MAX.chk", cliqueCollectionSnapshot);
            System.gc();
        }
    }


    public void logIteration() {
        // Write to log if appropriate
        if (stats.getAnalyzedGraphCount() % Config.LOG_INTERVAL == 0) {
            logger.info(getSummaryInfo());
            timer.clearCumulativeDuration();
        }
    }


    /**
     * This will generate a string with a series of data elements about the clique to be logged.
     * 
     * @return String of various data elements to be logged.
     */
    private String getSummaryInfo() {
        String content = stats.getAnalyzedGraphCount() + "|" + cayleyGraph.printDistributionSummary("RED") + "|" + cayleyGraph.getCliqueCollection().getCliqueCount() + "|" + stats.getMinCliqueCount() + "|" + timer.printCumulativeDuration("MUTATE") + "|" + timer.printCumulativeDuration("LOGGER") + "|" + timer.printCumulativeDuration("CLIQUE") + "|" + timer.printCumulativeDuration("STATS") + "|" + cayleyGraph.printDistribution("RED");
        return content;
    }


    /**
     * This is used for generating the date time stamps in file names.
     * 
     * @return String value of the current date time.
     */
    static String getDateTimeStamp() {
        DateFormat df = new SimpleDateFormat("MMddyyyyHHmmss");
        return df.format(new Date());
    }
}
