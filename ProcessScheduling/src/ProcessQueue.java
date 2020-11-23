import java.util.Iterator;
import java.util.Queue;

public abstract class ProcessQueue {	
		
	protected Queue<ProcessControlBlock> processQueue;	 
		
	public ProcessQueue(Queue<ProcessControlBlock> processQueue) {		
		this.processQueue = processQueue;
	}
		
	protected abstract int getAvailableCapacity();
	
	public abstract boolean isBelowThresholdCapacity();
		
	public int getSize() {
		return processQueue.size();
	}	
	
	public ProcessControlBlock peek() {
		return processQueue.peek();
	}
	
	public void enqueue(ProcessControlBlock processControlBlock) {
		processQueue.add(processControlBlock);	
	}
	
	public ProcessControlBlock dequeue() {
		return processQueue.poll();
	}
	
	public void remove(ProcessControlBlock processControlBlock) {
		processQueue.remove(processControlBlock);
	}
	
	public boolean isEmpty() {
		return processQueue.isEmpty();
	}
	
	public Iterator<ProcessControlBlock> getIterator() {
		return processQueue.iterator();
	}
}
