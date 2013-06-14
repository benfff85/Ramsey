package ramsey;

public class test {
	
	public static void main (String [] args){
		CayleyGraph cayleyGraph;
		
		cayleyGraph = new CayleyGraph(288,8);
		int count = 0;
		while(true){
		cayleyGraph.generateRandomGraph();
		count ++;
		System.out.println(count);
		cayleyGraph.printCayleyGraph();
		cayleyGraph.printRedBlueCount();
		}
	}

}
