import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ProcessGenerator {
	
	Random random;
	
	public ProcessGenerator() {		
		random = new Random(Helper.RANDOM_SEED);
	}
		
	public void randomizeProcessArrivalInJobQueue(JobQueue jobQueue) {
		int capacity = jobQueue.getAvailableCapacity();
		//simulate different arrival time by randomly choosing to run
		int size = random.nextInt(capacity);
		if (size > 0) {
			ArrayList<ProcessControlBlock> processes = generate(size);		
			//populate the job queue
			Iterator<ProcessControlBlock> iterator = processes.iterator();
		    while (iterator.hasNext()) {
		    	ProcessControlBlock processControlBlock = iterator.next();
		        jobQueue.enqueue(processControlBlock);
		    }
		}
	}
	
	private ArrayList<ProcessControlBlock> generate(int numberOfProcesses) {	
		ArrayList<ProcessControlBlock> processes = new ArrayList<ProcessControlBlock>();
		for (int i = 0; i < numberOfProcesses; i++) {				
			if (Helper.processCounter > Helper.MAX_PROCESS) {
				break;
			}
			processes.add(new ProcessControlBlock(Helper.processCounter, ProcessStateEnum.NEW, 1));
			Helper.processCounter++;			
		}
		return processes;
	}
}
