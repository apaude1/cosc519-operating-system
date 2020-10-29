import java.util.*;

// Implement Ready Queue using Priority Queue
public class ReadyQueue implements IReadyQueue {
	
	PriorityQueue<Process> _readyQueue;
	
	public ReadyQueue(int size, ProcessArrivalTimeComparator processArrivalTimeComparator) {
		_readyQueue = new PriorityQueue<Process>(size, processArrivalTimeComparator);		
	}	
}
