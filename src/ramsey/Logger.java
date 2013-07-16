package ramsey;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

	int maxCliqueSum;
	long maxWeightedCliqueSum;
	int maxFirstCliqueElement;
	long analyzedGraphCount;

	File file;
	FileWriter fw;
	BufferedWriter bw;

	/**
	 * This is the main logger constructor which will initialize tracked values
	 * to 0.
	 */
	public Logger() {
		this.maxCliqueSum = 0;
		this.maxWeightedCliqueSum = 0;
		this.maxFirstCliqueElement = 0;
		this.analyzedGraphCount = 0;
	}

	/**
	 * This will take a CayleyGraph as input and parse if for the various values
	 * we are tracking.
	 * 
	 * @param cayleyGraph The CayleyGraph to be parsed for various values we are
	 *        tracking.
	 * @return void
	 */
	public void parseCayleyGraph(CayleyGraph cayleyGraph) {
		this.analyzedGraphCount++;
		updateMaxCliqueSum(cayleyGraph.getClique());
		if(updateMaxWeightedCliqueSum(cayleyGraph.getClique(), cayleyGraph.numOfElements)){
			cayleyGraph.writeToFile("D:\\", "Ramsey.chk");
		}
		updateMaxFirstCliqueElement(cayleyGraph.getClique());
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
		long cliqueSum = 0;
		for (int i = 0; i < clique.getCliqueSize(); i++) {
			cliqueSum += (Math.pow(numOfElements, clique.getCliqueSize() - (i + 1)) * clique.getCliqueVertexByPosition(i).getId());
		}

		if (cliqueSum > this.maxWeightedCliqueSum) {
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
	 * This will return the maximum sum of Vertex IDs in an identified clique.
	 * 
	 * @return The maximum sum of Vertex IDs in an identified clique.
	 */
	public int getMaxCliqueSum() {
		return this.maxCliqueSum;
	}

	/**
	 * This will return the maximum weighted sum of Vertex IDs in an identified clique.
	 * 
	 * @return The maximum weighted sum of Vertex IDs in an identified clique.
	 */
	public long getMaxWeightedCliqueSum() {
		return this.maxWeightedCliqueSum;
	}
	
	/**
	 * This will return the maxFirstCliqueElement which is the greatest first
	 * Vertex ID in an identified clique.
	 * 
	 * @return The greatest first Vertex ID in an identified clique.
	 */
	public int getMaxFirstCliqueElement() {
		return this.maxFirstCliqueElement;
	}

	/**
	 * This will return the analyzedGraphCount which tracks the number of
	 * CayleyGraph instances which have been checked for complete subgraphs.
	 * 
	 * @return The number of CayleyGraphs that have been analyzed thus far.
	 */
	public long getAnalyzedGraphCount() {
		return this.analyzedGraphCount;
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
		if (this.file == null) {
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
		DateFormat df = new SimpleDateFormat("MMddyyyyHHmmss");
		String formattedDate = df.format(new Date());
		String fileName = "D:\\Ramsey_" + formattedDate + ".log";

		try {
			this.file = new File(fileName);
			this.fw = new FileWriter(file.getAbsoluteFile());
			this.bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
