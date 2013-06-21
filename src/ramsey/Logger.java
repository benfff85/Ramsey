package ramsey;

public class Logger {

	int maxCliqueSum;
	int maxFirstCliqueElement;
	long analyzedGraphCount;
	
	public Logger(){
		this.maxCliqueSum = 0;
		this.maxFirstCliqueElement = 0;
		this.analyzedGraphCount = 0;
	}
	
	public void parseCayleyGraph(CayleyGraph cayleyGraph){
		this.analyzedGraphCount++;
		updateMaxCliqueSum(cayleyGraph.getClique());
		updateMaxFirstCliqueElement(cayleyGraph.getClique());
	}
	
	
	private void updateMaxCliqueSum(Clique clique){
		int cliqueSum = 0;
		for (int i=0; i<clique.getCliqueSize(); i++){
			cliqueSum += clique.getCliqueVertexByPosition(i).getId();
		}
		
		if (cliqueSum > this.maxCliqueSum){
			this.maxCliqueSum = cliqueSum;
		}
	}
	
	private void updateMaxFirstCliqueElement(Clique clique){
		if (clique.getCliqueVertexByPosition(0).getId() > this.maxFirstCliqueElement){
			this.maxFirstCliqueElement = clique.getCliqueVertexByPosition(0).getId();
		}
	}

	public int getMaxCliqueSum(){
		return this.maxCliqueSum;
	}
	
	public int getMaxFirstCliqueElement(){
		return this.maxFirstCliqueElement;
	}
	
	public long getAnalyzedGraphCount(){
		return this.analyzedGraphCount;
	}
}
