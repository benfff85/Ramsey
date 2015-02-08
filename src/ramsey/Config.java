package ramsey;

public class Config {

	public static final int NUM_OF_ELEMENTS = 288;
	public static final int CLIQUE_SIZE = 8;
	
	public static final LAUNCH_TYPE LAUNCH_METHOD = LAUNCH_TYPE.OPEN_FROM_FILE;
	
	public static final MUTATION_TYPE MUTATE_METHOD_PRIMARY = MUTATION_TYPE.BALANCED;
	public static final MUTATION_TYPE MUTATE_METHOD_SECONDARY = MUTATION_TYPE.TARGETED;
	public static final int MUTATE_COUNT = 1;
	public static final int MUTATE_INTERVAL = 10;
	
	
	public static final ROTATION_TYPE ROTATION_METHOD = ROTATION_TYPE.SERIAL;
	public static final DIRECTION ROTATION_DIRECTION = DIRECTION.RIGHT; 
	public static final int ROTATION_THREAD_COUNT = 1;
	public static final int ROTATION_COUNT = 5;
		
	public static final LOG_TYPE LOG_METHOD = LOG_TYPE.BOTH;
	public static final int LOG_INTERVAL = 1000;
	public static final String CHKPT_FILE_PATH = "X:\\";
	public static final String CHKPT_FILE_MASK = "Ramsey_";
	public static final String LOG_FILE_PATH = "X:\\";
	public static final String LOG_FILE_MASK = "Ramsey_";
	public static final String SOLUTION_FILE_PATH = "S:\\";
	public static final String SOLUTION_FILE_MASK = "Ramsey_";
	
	public static final boolean EMAIL_SOLUTION_IND = true;
	public static final String EMAIL_ADDRESS = "ben.ferenchak@gmail.com";
	public static final String EMAIL_USER_ID = "ben.ferenchak";
	public static final String EMAIL_PASSWORD = "";
	
	public static final boolean DEBUG_MODE = false;
	
}
