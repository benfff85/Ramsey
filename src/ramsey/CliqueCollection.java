package ramsey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CliqueCollection implements java.io.Serializable {
	
	private static final long serialVersionUID = -1195229985562894286L;
	private List<Clique> cliqueList;
	
	public CliqueCollection(){
		cliqueList = Collections.synchronizedList(new ArrayList<Clique>());
	}
	
	public void addClique(Clique clique){
		cliqueList.add(clique);
	}
	
	public void clear(){
		cliqueList.clear();
	}
	
	public int getCliqueCount(){
		return cliqueList.size();
	}
	
	public Clique getCliqueByIndex(int index){
		return cliqueList.get(index);
	}
	
	/**
	 * This will randomly return one of the cliques contained within the CliqueCollection.
	 * 
	 * @return A random clique from within the CliqueCollection.
	 */
	public Clique getRandomClique(){
		Random generator = new Random();
		return getCliqueByIndex(generator.nextInt(getCliqueCount())); 
	}
	
}
