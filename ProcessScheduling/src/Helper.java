import java.util.Random;

public class Helper {
	public static int contextSwitchCount = 0;
	public static int currentTime = 0;
	public static int processCounter = 1;	
	public static final int randomSeed = 10 + new Random().nextInt(100);
	public static final int JOB_QUEUE_CAPACITY = 20;	
	public static final int MAX_BURST_TIME = 5;	
	public static final int MAX_PRIORITY = 10;
	public static final int MAX_PROCESS = 100; 
	public static final int QUANTUM = 3;
	public static final int READY_QUEUE_CAPACITY = 10;	
	public static final float THRESHOLD_CAPACITY = 0.25f;	
}
