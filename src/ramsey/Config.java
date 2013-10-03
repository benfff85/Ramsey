package ramsey;

public class Config {

	public final int NUM_OF_ELEMENTS = 288;
	public final int CLIQUE_SIZE = 8;
	
	public final LAUNCH_TYPE LAUNCH_METHOD = LAUNCH_TYPE.OPEN_FROM_FILE;
	
	public final MUTATION_TYPE MUTATE_METHOD = MUTATION_TYPE.BALANCED;
	public final int MUTATE_COUNT = 1;
	
	public final ROTATION_TYPE ROTATION_METHOD = ROTATION_TYPE.SERIAL;
	public final int ROTATION_THREAD_COUNT = 1;
	public final int ROTATION_COUNT = 1;
	
	public final LOG_TYPE LOG_METHOD = LOG_TYPE.BOTH;
	public final int LOG_INTERVAL = 1000;
	public final String CHKPT_FILE_PATH = "D:\\";
	public final String CHKPT_FILE_MASK = "Ramsey_";
	public final String LOG_FILE_PATH = "X:\\";
	public final String LOG_FILE_MASK = "Ramsey_";
	public final String SOLUTION_FILE_PATH = "D:\\";
	public final String SOLUTION_FILE_MASK = "Ramsey_";
	
	public final boolean EMAIL_SOLUTION_IND = true;
	public final String EMAIL_ADDRESS = "ben.ferenchak@gmail.com";
	public final String EMAIL_USER_ID = "ben.ferenchak";
	public final String EMAIL_PASSWORD = "";
	
}
