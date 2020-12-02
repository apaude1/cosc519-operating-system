import java.util.Random;

public class Helper {
	public static final int RANDOM_SEED = new Random().nextInt();
	public static final int JOB_QUEUE_CAPACITY = 10;	
	public static final int MAX_BURST_TIME = 5;	
	public static final int MAX_PRIORITY = 10;
	public static final int MAX_PROCESS = 100; //-1 would run indefinitely
	public static final long QUANTUM = 2;
	public static final int READY_QUEUE_CAPACITY = 5;	
	public static final float THRESHOLD_CAPACITY = 0.5f;	
}
