
import java.util.Comparator;
import java.util.Map.Entry;

public class ProcessControlTableComparator implements Comparator<Entry<Integer, ProcessControlBlock>> {
	
	@Override
	public int compare(Entry<Integer, ProcessControlBlock> arg0, Entry<Integer, ProcessControlBlock> arg1) {
		if (arg0.getValue().getArrivalTime() > arg1.getValue().getArrivalTime()) 
            return 1; 
        else if (arg0.getValue().getArrivalTime() < arg1.getValue().getArrivalTime()) 
            return -1; 
        else {
        	if (arg0.getValue().getCompletionTime() > arg1.getValue().getCompletionTime()) 
                return 1; 
            else if (arg0.getValue().getCompletionTime() < arg1.getValue().getCompletionTime()) 
                return -1; 
            else {
            	return 0;
            }   
        }            
	}
}
