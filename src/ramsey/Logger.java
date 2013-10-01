package ramsey;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private long analyzedGraphCount;
	private BufferedWriter bw;
	private String formattedDate;
	private Config config = new Config();

	/**
	 * This is the main Logger constructor which will initialize tracked values
	 * to 0.
	 */
	public Logger() {
		this.maxCliqueSum = 0;
		this.maxWeightedCliqueSum = new BigInteger("0");
		this.maxFirstCliqueElement = 0;
		this.analyzedGraphCount = 0;
		
		DateFormat df = new SimpleDateFormat("MMddyyyyHHmmss");
		this.formattedDate = df.format(new Date());
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

		if (cliqueSum > this.maxCliqueSum) {
			this.maxCliqueSum = cliqueSum;
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
	private boolean updateMaxWeightedCliqueSum(Clique clique, int numOfElements) {
		BigInteger cliqueSum = new BigInteger("0");
		long elementSum = 0;
		
		for (int i = 0; i < clique.getCliqueSize(); i++) {
			elementSum = (long)Math.pow(numOfElements, clique.getCliqueSize() - (i + 1)) * clique.getCliqueVertexByPosition(i).getId();
			cliqueSum = cliqueSum.add(BigInteger.valueOf(elementSum));
		}

		if (cliqueSum.compareTo(this.maxWeightedCliqueSum) > 0) {
			this.maxWeightedCliqueSum = cliqueSum;
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
		if (clique.getCliqueVertexByPosition(0).getId() > this.maxFirstCliqueElement) {
			this.maxFirstCliqueElement = clique.getCliqueVertexByPosition(0).getId();
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
	public void writeToLogFile(String content) {
		if (this.bw == null) {
			openLogFile();
		}

		try {
			bw.append(content);
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
			this.bw.close();
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
		String fileName = config.LOG_FILE_PATH + config.LOG_FILE_MASK + this.formattedDate + ".log";
		try {
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			this.bw = new BufferedWriter(fw);
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
	public void printPositiveCase(CayleyGraph cayleyGraph) {
		System.out.println(cayleyGraph.printCayleyGraphMathematica());
		writeToLogFile("SOLUTION:\n" + cayleyGraph.printCayleyGraphMathematica());
		cayleyGraph.writeToFile(config.SOLUTION_FILE_PATH, config.SOLUTION_FILE_MASK + this.formattedDate + ".sol");
		cayleyGraph.emailCayleyGraph();
	}
	
	/**
	 * Outputs statistics in the event of a counter example not being found to
	 * the console, a log file, or both.
	 * 
	 * 
	 * @param cg The CayleyGraph object the process is being applied to
	 * @param t The Timer object tracking processing duration
	 * @return void
	 */
	public void processNegativeCase(CayleyGraph cayleyGraph, Timer timer) {
		// Update all tracking
		this.analyzedGraphCount++;
		updateMaxFirstCliqueElement(cayleyGraph.getClique());
		updateMaxCliqueSum(cayleyGraph.getClique());
		if(updateMaxWeightedCliqueSum(cayleyGraph.getClique(), cayleyGraph.getNumOfElements())){
			cayleyGraph.writeToFile(config.CHKPT_FILE_PATH, config.CHKPT_FILE_MASK + this.formattedDate + ".chk");
		}
		
		String content =
				"#######################################################################\n" +
						"Graph Count:          " + this.analyzedGraphCount + "\n" +
						"Clique Color:         " + cayleyGraph.getClique().getColor() + "\n" +
						"Clique:               " + cayleyGraph.getClique().printClique() + "\n" +
						"Line Count:           " + cayleyGraph.printRedBlueCount() + "\n" +
						"Distribution:         " + cayleyGraph.printDistribution("RED") + "\n" +
						"Distribution Summary: " + cayleyGraph.printDistributionSummary("RED") + "\n" +
						"Max First Clique ID:  " + this.maxFirstCliqueElement + "\n" +
						"Max Clique Sum:       " + this.maxCliqueSum + "\n" + 
						"Weighted Clique Sum   " + this.maxWeightedCliqueSum + "\n" +
						"Time Mutate:          " + timer.printCumulativeDuration("MUTATE") + "\n" +
						"Time Logger:          " + timer.printCumulativeDuration("LOGGER") + "\n" +
						"Time CliqueCheck:     " + timer.printCumulativeDuration("CLIQUE") + "\n" +
						"Time Rotate:          " + timer.printCumulativeDuration("ROTATE") + "\n";
		
		if (this.analyzedGraphCount % config.LOG_INTERVAL == 0) {
			writeToLog(content);
			timer.clearCumulativeDuration("MUTATE");
			timer.clearCumulativeDuration("LOGGER");
			timer.clearCumulativeDuration("CLIQUE");
			timer.clearCumulativeDuration("ROTATE");
			//System.gc();
		}
	}
		
	private void writeToLog(String content){
		if (config.LOG_METHOD == LOG_TYPE.FILE || config.LOG_METHOD == LOG_TYPE.BOTH) {
			writeToLogFile(content);
		}

		if (config.LOG_METHOD == LOG_TYPE.FILE || config.LOG_METHOD == LOG_TYPE.BOTH) {
			System.out.println(content);
		}
	}

}
