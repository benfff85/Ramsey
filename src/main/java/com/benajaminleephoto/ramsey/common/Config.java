package com.benajaminleephoto.ramsey.common;

import com.benajaminleephoto.ramsey.mutate.MUTATION_TYPE;

public class Config {

    public static int NUM_OF_ELEMENTS;
    public static int CLIQUE_SIZE;

    public static final LAUNCH_TYPE LAUNCH_METHOD = LAUNCH_TYPE.OPEN_FROM_FILE;

    public static final MUTATION_TYPE MUTATE_METHOD_PRIMARY = MUTATION_TYPE.BALANCED;
    public static final MUTATION_TYPE MUTATE_METHOD_SECONDARY = MUTATION_TYPE.COMPREHENSIVE;
    public static int MUTATE_COUNT;
    public static int MUTATE_INTERVAL;
    public static int MUTATE_COMPREHENSIVE_EDGE_RANGE;

    public static int CLIQUE_SEARCH_THREAD_COUNT;

    public static String CHKPT_FILE_PATH;
    public static String CHKPT_FILE_MASK;
    public static String MAX_FILE_PATH;
    public static String MAX_FILE_MASK;
    public static String SOLUTION_FILE_PATH;
    public static String SOLUTION_FILE_MASK;

    public static boolean EMAIL_SOLUTION_IND;
    public static String EMAIL_ADDRESS;
    public static String EMAIL_USER_ID;
    public static String EMAIL_PASSWORD;

}
