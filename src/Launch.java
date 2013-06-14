
public class Launch {

	public static void main (String [] args)
	{
		int threadCount = Integer.parseInt(args[0]);
		System.out.println("threadCount:  " + threadCount + "!!");
		MyThread[] threads = new MyThread[threadCount];
		Q q = new Q();
		System.out.println("Count,Duration,Max First Tree Element");
		
		for (int i=0;i<threadCount;i++){
			threads[i] = new MyThread(q);
		}
	}
	
}


class MyThread extends Thread
{
	Q q;
	MyThread (Q q){
		this.q = q;
		start();
	}
   
	public void run ()
   {
       test x = new test();
	   x.main(q); 
   }
}


class Q {
	long count = 0;
	boolean valueSet = false;
	long startTime = System.nanoTime();
	long endTime;
	long duration;
	int maxFirstTree = 0;
	
	public void increment(){
		count++;
		/*
		if(count%1000==0){
			endTime = System.nanoTime();
			long duration = endTime - startTime;
			System.out.println(count + "," + duration + "," + maxFirstTree);
			startTime = System.nanoTime();
		}
		*/
	}
	public void maxFirstTreeTracker(int x){
		if (x > maxFirstTree){
			maxFirstTree = x;
		}
	}
	
	public long getCount(){
		return this.count;
	}
}