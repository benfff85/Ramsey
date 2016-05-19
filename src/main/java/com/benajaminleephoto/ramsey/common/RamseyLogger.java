package com.benajaminleephoto.ramsey.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This Class will help facilitate logging to a file as well as tracking various values to gauge
 * performance and progress.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class RamseyLogger {

    private BufferedWriter bufferedLogWriter;
    private String formattedLogDate;
    private CayleyGraph cayleyGraph;
    private Timer timer;
    private CumulativeStatistics stats;
    private CliqueCollectionSnapshot cliqueCollectionSnapshot;


    /**
     * This is the main Logger constructor which will initialize tracked values to 0.
     * 
     * @param cayleyGraph The Cayley Graph being checked.
     * @param timer A time which can be polled to query performance.
     * @param stats Statistics object which can be pulled for cumulative statistics of the run.
     */
    public RamseyLogger(CayleyGraph cayleyGraph, Timer timer, CumulativeStatistics stats) {
        formattedLogDate = getDateTimeStamp();
        this.cayleyGraph = cayleyGraph;
        this.timer = timer;
        this.stats = stats;
        cliqueCollectionSnapshot = new CliqueCollectionSnapshot();
    }


    /**
     * This will take an input String of content and write it to a log file. If the log file is not
     * opened it will open it. All content will append to the end of the file if the file is already
     * opened.
     * 
     * @param content The String content which will be written to the log file.
     */
    private void writeToLogFile(String content) {
        if (bufferedLogWriter == null) {
            openLogFile();
        }

        try {
            bufferedLogWriter.append(content);
            bufferedLogWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This is a public method to close the BufferedWritter used by this class.
     */
    public void closeLogFile() {
        try {
            bufferedLogWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This is a local method used to open a new file if the public BufferedWriter is not already
     * opened.
     */
    private void openLogFile() {
        String fileName = Config.LOG_FILE_PATH + Config.LOG_FILE_MASK + formattedLogDate + ".log";
        try {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            bufferedLogWriter = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        System.out.println(cayleyGraph.printCayleyGraphMathematica());
        writeToLogFile("SOLUTION:\n" + cayleyGraph.printCayleyGraphMathematica());
        GraphFileWriter.writeSolutionFile();
        // cayleyGraph.emailCayleyGraph();
        closeLogFile();
    }


    /**
     * This will determine if a checkpoint is required and if so create it. At the moment
     * checkpoints will be created when a CayleyGraph with a new minimum number of cliques is found.
     */
    public void processCheckpoint() {
        if (stats.getMinCliqueCount() == cayleyGraph.getCliqueCollection().getCliqueCount()) {
            System.out.println("Writing MAX File");
            GraphFileWriter.writeMaxFile();
            cliqueCollectionSnapshot.clearCliqueCollectionArray();
            cliqueCollectionSnapshot.populateSnapshot(cayleyGraph.getCliqueCollection());
            if (Config.LAUNCH_METHOD != LAUNCH_TYPE.OPEN_FROM_FILE || stats.getAnalyzedGraphCount() != 1) {
                System.out.println("Writing CHK File");
                GraphFileWriter.writeCheckpointFile();
            }
        } else {
            System.out.println("Rolling Back");
            cayleyGraph.rollback(Config.CHKPT_FILE_PATH + "Ramsey_MAX.chk", cliqueCollectionSnapshot);
            System.gc();
        }
    }


    public void logIteration() {
        // Write to log if appropriate
        if (stats.getAnalyzedGraphCount() % Config.LOG_INTERVAL == 0) {
            writeToLog(printSummaryInfo());
            timer.clearCumulativeDuration();
        }
    }


    /**
     * This will generate a string with a series of data elements about the clique to be logged.
     * 
     * @return String of various data elements to be logged.
     */
    private String printSummaryInfo() {
        String content = stats.getAnalyzedGraphCount() + "|" + cayleyGraph.getClique().getColor() + "|" + cayleyGraph.getClique().printClique() + "|" + cayleyGraph.printRedBlueCount() + "|" + cayleyGraph.printDistributionSummary("RED") + "|" + cayleyGraph.getCliqueCollection().getCliqueCount() + "|" + stats.getMinCliqueCount() + "|" + timer.printCumulativeDuration("MUTATE") + "|" + timer.printCumulativeDuration("LOGGER") + "|" + timer.printCumulativeDuration("CLIQUE") + "|" + timer.printCumulativeDuration("STATS") + "|" + cayleyGraph.printDistribution("RED") + "\n";

        return content;
    }


    private void writeToLog(String content) {
        if (Config.LOG_METHOD == LOG_TYPE.FILE || Config.LOG_METHOD == LOG_TYPE.BOTH) {
            writeToLogFile(content);
        }
        if (Config.LOG_METHOD == LOG_TYPE.CONSOLE || Config.LOG_METHOD == LOG_TYPE.BOTH) {
            System.out.print(content);
        }
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
