package ramsey;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Timer {

	Map<String, TimeSet> timerArray = new HashMap<String, TimeSet>();

	class TimeSet {
		long startTime;
		long endTime;
		long duration;
		long cumulativeDuration;
	}
	
	public Timer(){
	}
	
	public void newTimeSet(String label){
		this.timerArray.put(label,new TimeSet());
	}
	
	public void startTimer(String label){
		this.timerArray.get(label).startTime = System.nanoTime();
		this.timerArray.get(label).endTime = 0;
		this.timerArray.get(label).duration = 0;
	}
	
	public void endTimer(String label){
		this.timerArray.get(label).endTime = System.nanoTime();
		this.timerArray.get(label).duration = this.timerArray.get(label).endTime - this.timerArray.get(label).startTime;
		this.timerArray.get(label).cumulativeDuration += this.timerArray.get(label).duration;
	}
	
	public String printDuration(String label){
		double duration = this.timerArray.get(label).duration/1000000000.0; 
		duration = round(duration,2,BigDecimal.ROUND_HALF_UP);
		return duration + "";
	}
	
	public String printCumulativeDuration(String label){
		double duration = this.timerArray.get(label).cumulativeDuration/1000000000.0; 
		duration = round(duration,2,BigDecimal.ROUND_HALF_UP);
		return duration + "";
	}
	
	public void clearCumulativeDuration(String label){
		this.timerArray.get(label).cumulativeDuration = 0;
	}
	
	private static double round(double unrounded, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.doubleValue();
	}
	
}


