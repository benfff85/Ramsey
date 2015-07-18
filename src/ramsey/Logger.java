package ramsey;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
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

	private int maxCliqueSum;
	private BigInteger maxWeightedCliqueSum;
	private int maxFirstCliqueElement;
	private int minCliqueCount;
	private long analyzedGraphCount;
	private BufferedWriter bufferedLogWriter;
	private String formattedLogDate;
	CayleyGraph cayleyGraph;
	Timer timer;
	CayleyGraph cayleyGraphCheckpoint;

	/**
	 * This is the main Logger constructor which will initialize tracked values
	 * to 0.
	 */
	public Logger(CayleyGraph cayleyGraph, Timer timer) {
		maxCliqueSum = 0;
		minCliqueCount = 999999999;
		maxWeightedCliqueSum = new BigInteger("0");
		maxFirstCliqueElement = 0;
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
	 * This will take a Clique as input and update maxCliqueSum if the sum of
	 * elements (Vertex IDs) of the input Clique is greater than the current
	 * maximum Clique sum logged till now.
	 * 
	 * @param clique The Clique which will be checked to see if the sum of
	 *        elements (Vertex IDs) is greater than the current maximum Clique
	 *        sum logged till now.
	 * @return True if this clique has the new largest cliqueSum, otherwise false.
	 */
	private boolean updateMaxCliqueSum(Clique clique) {
		int cliqueSum = 0;
		for (int i = 0; i < clique.getCliqueSize(); i++) {
			cliqueSum += clique.getCliqueVertexByPosition(i).getId();
		}

		if (cliqueSum > maxCliqueSum) {
			maxCliqueSum = cliqueSum;
			return true;
		}
		return false;
	}

	/**
	 * This will take a Clique as input and update maxWeightedCliqueSum if the
	 * sum of elements (Vertex IDs) of the input Clique is greater than the
	 * current maximum weighted Clique sum logged till now.
	 * 
	 * @param clique The Clique which will be checked to see if the sum of
	 *        elements (Vertex IDs) is greater than the current maximum weighted
	 *        Clique sum logged till now.
	 * @return True if this clique has the new largest weighted clique sum, otherwise false.
	 */
	private boolean updateMaxWeightedCliqueSum(Clique clique) {
		BigInteger cliqueSum = new BigInteger("0");

		for (int i = 0; i < Config.CLIQUE_SIZE; i++) {
			cliqueSum = cliqueSum.add(BigInteger.valueOf(Config.NUM_OF_ELEMENTS).pow(Config.CLIQUE_SIZE - (i + 1)).multiply(BigInteger.valueOf(clique.getCliqueVertexByPosition(i).getId())));
		}

		if (cliqueSum.compareTo(maxWeightedCliqueSum) > 0) {
			maxWeightedCliqueSum = cliqueSum;
			return true;
		}
		return false;
	}
	
	/**
	 * This will take a Clique as input and update maxFirstCliqueElement if the
	 * first element of the input Clique is greater than the current maximum
	 * first clique element logged till now.
	 * 
	 * @param clique The Clique which will be checked to see if the first
	 *        element is greater than the current maximum first clique element
	 *        logged till now.
	 * @return void
	 */
	private void updateMaxFirstCliqueElement(Clique clique) {
		Debug.write("Beginning updateMaxFirstCliqueElement");
		if (clique.getCliqueVertexByPosition(0).getId() > maxFirstCliqueElement) {
			maxFirstCliqueElement = clique.getCliqueVertexByPosition(0).getId();
		}
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
	}
	

	/**
	 * This will update all the global tracking information in the logger class
	 * like the analyzed graph count and the max clique sum.
	 * 
	 * @return void
	 */
	private void updateTrackingData(){
		analyzedGraphCount++;
		updateMaxFirstCliqueElement(cayleyGraph.getCliqueCollection().getCliqueByIndex(0));
		updateMaxCliqueSum(cayleyGraph.getCliqueCollection().getCliqueByIndex(0));
		updateMaxWeightedCliqueSum(cayleyGraph.getCliqueCollection().getCliqueByIndex(0));
		updateMinCliqueCount(cayleyGraph.getCliqueCollection());
	}
	
	
	/**
	 * This will determine if a checkpoint is required and if so create it. At
	 * the moment checkpoints will be created when a CayleyGraph with a new
	 * minimum number of cliques is found.
	 * 
	 * @return void
	 */
	private void processCheckpoint(){
		if(minCliqueCount == cayleyGraph.getCliqueCollection().getCliqueCount()){			
			writeGraphFile(cayleyGraph,Config.CHKPT_FILE_PATH + Config.CHKPT_FILE_MASK + getDateTimeStamp() +".chk");
			//cayleyGraphCheckpoint = SerializationUtils.clone(cayleyGraph);
			//Debug.write("New min CC - CG Master Hash: " + cayleyGraph.hashCode());
			//Debug.write("New min CC - CG Clone Hash: " + cayleyGraphCheckpoint.hashCode());
		}
		else {
			//Debug.write("Not Min CC - CG Master Hash: " + cayleyGraph.hashCode());
			//Debug.write("Not Min CC - CG Clone Hash: " + cayleyGraphCheckpoint.hashCode());
			//Debug.write("CC before rollback: " + cayleyGraph.getCliqueCollection().getCliqueCount());
			//Debug.write("Clone CC before rollback: " + cayleyGraphCheckpoint.getCliqueCollection().getCliqueCount());
			//cayleyGraph.rollback(cayleyGraphCheckpoint);
			//Debug.write("Not Min CC - CG Master Hash after rollback: " + cayleyGraph.hashCode());
			//Debug.write("Clone CC after rollback: " + cayleyGraphCheckpoint.getCliqueCollection().getCliqueCount());
			//Debug.write("CC after rollback: " + cayleyGraph.getCliqueCollection().getCliqueCount());

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
	
	
	/**
	 * This will generate a string with a series of data elemnts about the
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
			maxFirstCliqueElement                               +"|"+
			maxCliqueSum                                        +"|"+ 
			maxWeightedCliqueSum                                +"|"+
			cayleyGraph.printDistributionSummary("RED")         +"|"+
			cayleyGraph.getCliqueCollection().getCliqueCount()  +"|"+
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
