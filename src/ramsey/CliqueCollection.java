package ramsey;

import java.util.ArrayList;

public class CliqueCollection implements java.io.Serializable {
	
	private static final long serialVersionUID = -1195229985562894286L;
	private ArrayList<Clique> cliqueList;
	
	public CliqueCollection(){
		cliqueList = new ArrayList<Clique>();
	}
	
	public void addClique(Clique clique){
		Debug.write(clique.printClique());
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
}
