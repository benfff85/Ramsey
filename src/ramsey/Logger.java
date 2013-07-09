package ramsey;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	int maxCliqueSum;
	int maxFirstCliqueElement;
	long analyzedGraphCount;
	
	File file;
	FileWriter fw;
	BufferedWriter bw;
	
	public Logger(){
		this.maxCliqueSum = 0;
		this.maxFirstCliqueElement = 0;
		this.analyzedGraphCount = 0;
		
		try {
			this.file = new File("D:\\Ramsey.log");
			this.fw = new FileWriter(file.getAbsoluteFile());
			this.bw = new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public void writeToLogFile(String content){
		try {
			//String content = "This is the content to write into file";
			bw.append(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeLogFile(){
		try {
			this.bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
