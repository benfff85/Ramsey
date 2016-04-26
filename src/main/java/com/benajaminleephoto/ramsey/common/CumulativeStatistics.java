package ramsey;

public class CumulativeStatistics {

	private int minCliqueCount;
	private long analyzedGraphCount;
	private CayleyGraph cayleyGraph;
	
	
	/**
	 * Main constructor for the CumulativeStatistics class.
	 * 
	 * @param cg Cayley Graph for which statistics are being tracked.
	 */
	public CumulativeStatistics(CayleyGraph cg) {
		setAnalyzedGraphCount(0);
		setCayleyGraph(cg);
		setMinCliqueCount(999999999);
	}
	
	/**
	 * This is the main publicly exposed method of this class which will trigger various updates based on data currently found in the classes CayleyGraph object.
	 */
	public void updateStatistics(){
		incrementAnalyzedGraphCount();
		updateMinCliqueCount(getCayleyGraph().getCliqueCollection());	
	}
	
	/**
	 * This will increment the number of analyzed graphs by one.
	 */
	private void incrementAnalyzedGraphCount(){
		setAnalyzedGraphCount(getAnalyzedGraphCount() + 1);	
	}
	
	/**
	 * This will check the number of cliques found for the current CayleyGraph.
	 * If the number of cliques found is a new lowest value than the
	 * minCliqueCount values will be updated.
	 */
	private void updateMinCliqueCount(CliqueCollection cliqueCollection){
		int cliquecount = cliqueCollection.getCliqueCount();
		if (cliquecount < getMinCliqueCount()) {
			setMinCliqueCount(cliquecount);
		}
	}
	
	/**
	 * Standard getter for the analyzed graph count.
	 * 
	 * @return The number of graphs analyzed so far.
	 */
	public long getAnalyzedGraphCount(){
		return analyzedGraphCount;
	}
	
	/**
	 * Standard setter for the analyzed graph count.
	 * 
	 * @param newAnalyzedGraphCount The new value for analyzed graph count.
	 */
	private void setAnalyzedGraphCount(long newAnalyzedGraphCount){
		analyzedGraphCount = newAnalyzedGraphCount;
	}
	
	/**
	 * Standard getter for the cayley graph.
	 * 
	 * @return The cayley graph for which statistics are being tracked.
	 */
	private CayleyGraph getCayleyGraph(){
		return cayleyGraph;
	}
	
	/**
	 * Standard setter for the cayley graph.
	 * 
	 * @param cg The cayley graph for which statistics are being tracked.
	 */
	private void setCayleyGraph(CayleyGraph cg){
		cayleyGraph = cg;
	}
	
	/**
	 * Standard getter for the minimum clique count.
	 * 
	 * @return The minimum clique count seen so far.
	 */
	public int getMinCliqueCount(){
		return minCliqueCount;
	}
	
	/**
	 * Standard setter method for the minimum clique count.
	 * 
	 * @param mcc Integer representing the new minimum clique count.
	 */
	private void setMinCliqueCount(int mcc){
		minCliqueCount = mcc;
	}
	
}
