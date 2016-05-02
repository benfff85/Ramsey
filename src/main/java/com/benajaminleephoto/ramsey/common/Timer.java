package com.benajaminleephoto.ramsey.common;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * This Class is responsible for capturing the runtime of various modules and reporting of them to
 * facilitate future performance tuning. Modules must be initialized with the newTimeSet at which
 * point they must be given a label by which they will be referred to going forward.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 * 
 */
public class Timer {

    private Map<String, TimeSet> timerArray = new HashMap<String, TimeSet>();

    class TimeSet {
        long startTime;
        long endTime;
        long duration;
        long cumulativeDuration;


        public void clearCumulativeDuration() {
            cumulativeDuration = 0;
        }
    }


    /**
     * This will initialize a new TimeSet for a labeled module of code. The given label is how the
     * TimeSet will be referenced going forward.
     * 
     * @param label This is the label applied to the TimeSet and is how the TimeSet will be
     *        referenced going forward.
     */
    public void newTimeSet(String label) {
        timerArray.put(label, new TimeSet());
    }


    /**
     * This will begin the timer for a given label. Since it is a new run for the TimeSet it will
     * reset the endTime and Duration to 0;
     * 
     * @param label This is the label for which the timer should be started.
     */
    public void startTimer(String label) {
        timerArray.get(label).startTime = System.nanoTime();
        timerArray.get(label).endTime = 0;
        timerArray.get(label).duration = 0;
    }


    /**
     * This will end the timer for a given label. It will calculate the duration for this given run
     * and assign it to the duration variable. It will then add the duration to the cumulative
     * duration.
     * 
     * @param label This is the label for which the timer should be stopped.
     */
    public void endTimer(String label) {
        timerArray.get(label).endTime = System.nanoTime();
        timerArray.get(label).duration = timerArray.get(label).endTime - timerArray.get(label).startTime;
        timerArray.get(label).cumulativeDuration += timerArray.get(label).duration;
    }


    /**
     * This will format that duration for a given label defined by the input into human readable
     * seconds and return the String.
     * 
     * @param label This is the label for which the duration will be returned.
     * @return The human readable sting for the duration in seconds.
     */
    public String printDuration(String label) {
        double duration = timerArray.get(label).duration / 1000000000.0;
        duration = round(duration, 2, BigDecimal.ROUND_HALF_UP);
        return duration + "";
    }


    /**
     * This will format that cumulative duration for a given label defined by the input into human
     * readable seconds and return the String.
     * 
     * @param label This is the label for which the cumulative duration will be returned.
     * @return The human readable sting for the cumulative duration in seconds.
     */
    public String printCumulativeDuration(String label) {
        double duration = timerArray.get(label).cumulativeDuration / 1000000000.0;
        duration = round(duration, 2, BigDecimal.ROUND_HALF_UP);
        return duration + "";
    }


    /**
     * This will resent the cumulative duration being tracked for a given label defined by the
     * input.
     * 
     * @param label This is the label for which the cumulative duration will be cleared.
     */
    public void clearCumulativeDuration(String label) {
        timerArray.get(label).clearCumulativeDuration();
    }


    /**
     * This will reset the cumulative duration being tracked for all labels.
     */
    public void clearCumulativeDuration() {
        for (Map.Entry<String, TimeSet> timeset : timerArray.entrySet()) {
            timeset.getValue().clearCumulativeDuration();
        }
    }


    /**
     * This is a local method used to round an input variable of type double. Rounding precision and
     * type are also defined by the input.
     * 
     * @param unrounded Input variable which will be rounded by this method.
     * @param precision Number of decimal places to be retained by the returned value.
     * @param roundingMode Type of rounding to be done. This is defined by constants in the
     *        BigDecimal class, generally BigDecimal.ROUND_HALF_UP is used.
     * @return The rounded double variable.
     */
    private static double round(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }

}
