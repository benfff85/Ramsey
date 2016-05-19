package com.benajaminleephoto.ramsey.common;

import com.benajaminleephoto.ramsey.cliqueChecker.CLIQUE_SEARCH_TYPE;
import com.benajaminleephoto.ramsey.mutate.MUTATION_TYPE;

public class Config {

    public static final int NUM_OF_ELEMENTS = 288;
    public static final int CLIQUE_SIZE = 8;

    public static final LAUNCH_TYPE LAUNCH_METHOD = LAUNCH_TYPE.OPEN_FROM_FILE;

    public static final MUTATION_TYPE MUTATE_METHOD_PRIMARY = MUTATION_TYPE.BALANCED;
    public static final MUTATION_TYPE MUTATE_METHOD_SECONDARY = MUTATION_TYPE.COMPREHENSIVE;
    public static final int MUTATE_COUNT = 1;
    public static final int MUTATE_INTERVAL = 2;
    public static final int MUTATE_COMPREHENSIVE_EDGE_RANGE = 1;

    public static final CLIQUE_SEARCH_TYPE CLIQUE_SEARCH_STRATAGY = CLIQUE_SEARCH_TYPE.ALL;
    public static final int CLIQUE_SEARCH_THREAD_COUNT = 6;

    public static final LOG_TYPE LOG_METHOD = LOG_TYPE.BOTH;
    public static final int LOG_INTERVAL = 1;
    public static final String CHKPT_FILE_PATH = "C:\\Users\\admin\\Google Drive\\RamseySearch\\Runtime\\Checkpoints\\";
    public static final String CHKPT_FILE_MASK = "Ramsey_";
    public static final String LOG_FILE_PATH = "C:\\Users\\admin\\Google Drive\\RamseySearch\\Runtime\\Logs\\";
    public static final String LOG_FILE_MASK = "Ramsey_";
    public static final String SOLUTION_FILE_PATH = "C:\\Users\\admin\\Google Drive\\RamseySearch\\Runtime\\Checkpoints\\";
    public static final String SOLUTION_FILE_MASK = "Ramsey_";

    public static final boolean EMAIL_SOLUTION_IND = true;
    public static final String EMAIL_ADDRESS = "ben.ferenchak@gmail.com";
    public static final String EMAIL_USER_ID = "ben.ferenchak";
    public static final String EMAIL_PASSWORD = "";

    // public static final boolean DEBUG_MODE = false;

}
