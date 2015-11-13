package ramsey;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;

/**
 * This Class will help facilitate logging to a file as well as tracking various
 * values to gauge performance and progress.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class Logger {

	private int minCliqueCount;
	private long analyzedGraphCount;
	private BufferedWriter bufferedLogWriter;
	private String formattedLogDate;
	private CayleyGraph cayleyGraph;
	private Timer timer;

	/**
	 * This is the main Logger constructor which will initialize tracked values
	 * to 0.
	 */
	public Logger(CayleyGraph cayleyGraph, Timer timer) {
		minCliqueCount = 999999999;
		analyzedGraphCount = 0;
		formattedLogDate = getDateTimeStamp();
		this.cayleyGraph = cayleyGraph;
		this.timer = timer;
	}
	
	/**
	 * This will check the number of cliques found for the current CayleyGraph.
	 * If the number of cliques found is a new lowest value than the
	 * minCliqueCount values will be updated.
	 * 
	 * @return True if the current CayleyGraphs clique count was a new lowest
	 *         value, otherwise false.
	 */
	private boolean updateMinCliqueCount(CliqueCollection cliqueCollection){
		int cliquecount = cliqueCollection.getCliqueCount();
		if (cliquecount < minCliqueCount) {
			minCliqueCount = cliquecount;
			return true;
		}
		return false;
	}

	/**
	 * This will take an input String of content and write it to a log file. If
	 * the log file is not opened it will open it. All content will append to
	 * the end of the file if the file is already opened.
	 * 
	 * @param content The String content which will be written to the log file.
	 * @return void
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
	 * 
	 * @return void
	 */
	public void closeLogFile() {
		try {
			bufferedLogWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is a local method used to open a new file if the public
	 * BufferedWriter is not already opened.
	 * 
	 * @return void
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
	 * Outputs the counter example in the event it has been found. This will
	 * write to the log file, email, print to the console and write to a
	 * solution file Ramsey.sol. All the above will be in a format compatible
	 * with Mathematica with the exception of the .sol file. The .sol file will
	 * be comma separated digits so that it may be used as input to the
	 * CayleyGraph.loadFromFile method later.
	 * 
	 * @param cg The CayleyGraph object the process is being applied to (Counter
	 *        Example)
	 * @return void
	 */
	public void processPositiveCase(CayleyGraph cayleyGraph) {
		System.out.println(cayleyGraph.printCayleyGraphMathematica());
		writeToLogFile("SOLUTION:\n" + cayleyGraph.printCayleyGraphMathematica());
		writeGraphFile(cayleyGraph,Config.SOLUTION_FILE_PATH + Config.SOLUTION_FILE_MASK + getDateTimeStamp() +".sol");
		//cayleyGraph.emailCayleyGraph();
		closeLogFile();
	}
	

	/**
	 * This will update all the global tracking information in the logger class
	 * like the analyzed graph count and the max clique sum.
	 * 
	 * @return void
	 */
	private void updateTrackingData(){
		analyzedGraphCount++;
		updateMinCliqueCount(cayleyGraph.getCliqueCollection());
	}
	
	
	/**
	 * This will determine if a checkpoint is required and if so create it. At
	 * the moment checkpoints will be created when a CayleyGraph with a new
	 * minimum number of cliques is found.
	 * 
	 * @return void
	 */
	public void processCheckpoint(){
		if(minCliqueCount == cayleyGraph.getCliqueCollection().getCliqueCount()){	
			System.out.println("Writing MAX File");
			writeGraphFile(cayleyGraph,Config.CHKPT_FILE_PATH + Config.CHKPT_FILE_MASK + "MAX" +".chk");
			if(Config.LAUNCH_METHOD != LAUNCH_TYPE.OPEN_FROM_FILE || analyzedGraphCount != 1){
				System.out.println("Writing CHK File");
				writeGraphFile(cayleyGraph,Config.CHKPT_FILE_PATH + Config.CHKPT_FILE_MASK + getDateTimeStamp() +".chk");
			}
		}
		else {
			System.out.println("Rolling Back");
			cayleyGraph.rollback("S:\\Ramsey\\Ramsey_MAX.chk");
		}
	}
	
	
	/**
	 * Outputs statistics in the event of a counter example not being found to
	 * the console, a log file, or both.
	 * 
	 * @return void
	 */
	public void processNegativeCase() {
		updateTrackingData();
		
		// Write to log if appropriate
		if (this.analyzedGraphCount % Config.LOG_INTERVAL == 0) {
			writeToLog(printSummaryInfo());
			timer.clearCumulativeDuration();
		}
		
		processCheckpoint();
	}
	
	public void logIteration(){
		updateTrackingData();
		
		// Write to log if appropriate
		if (this.analyzedGraphCount % Config.LOG_INTERVAL == 0) {
			writeToLog(printSummaryInfo());		
			timer.clearCumulativeDuration();
		}
	}
	
	
	/**
	 * This will generate a string with a series of data elements about the
	 * clique to be logged.
	 * 
	 * @return String of various data elements to be logged.
	 */
	private String printSummaryInfo() {
		String content = 
			analyzedGraphCount                                  +"|"+
			cayleyGraph.getClique().getColor()                  +"|"+
			cayleyGraph.getClique().printClique()               +"|"+
			cayleyGraph.printRedBlueCount()                     +"|"+
			cayleyGraph.printDistributionSummary("RED")         +"|"+
			cayleyGraph.getCliqueCollection().getCliqueCount()  +"|"+
			minCliqueCount                                      +"|"+
			timer.printCumulativeDuration("MUTATE")             +"|"+
	        timer.printCumulativeDuration("LOGGER")             +"|"+
	        timer.printCumulativeDuration("CLIQUE")             +"|"+
	        timer.printCumulativeDuration("ROTATE")             +"|"+
			cayleyGraph.printDistribution("RED") + "\n";
		
		return content;		
	}
	
	private void writeToLog(String content){
		if (Config.LOG_METHOD == LOG_TYPE.FILE || Config.LOG_METHOD == LOG_TYPE.BOTH) {
			writeToLogFile(content);
		}
		if (Config.LOG_METHOD == LOG_TYPE.CONSOLE || Config.LOG_METHOD == LOG_TYPE.BOTH) {
			System.out.print(content);
		}
	}
	
	/**
	 * This is used to write a basic representation of an input CayleyGraph to a
	 * given file which can later be loaded into this program.
	 * 
	 * @param cayleyGraph The CayleyGraph to be written to a file.
	 * @param qualifiedFileName The file path and name where the CayleyGraph
	 *        representation is to be written.
	 */
	private void writeGraphFile(CayleyGraph cayleyGraph, String qualifiedFileName) {
		String content = cayleyGraph.printCayleyGraphBasic();

		// Write the "content" string to file
		try {
			File file = new File(qualifiedFileName);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This is used for generating the date time stamps in file names.
	 * 
	 * @return String value of the current date time.
	 */
	private String getDateTimeStamp(){
		DateFormat df = new SimpleDateFormat("MMddyyyyHHmmss");
		return df.format(new Date());
	}
	
	public long getAnalyzedGraphCount(){
		return analyzedGraphCount;
	}

}
