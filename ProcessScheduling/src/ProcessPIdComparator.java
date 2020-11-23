import java.util.Comparator;

public class ProcessPIdComparator implements Comparator<ProcessControlBlock> {
	
	@Override
	public int compare(ProcessControlBlock process1, ProcessControlBlock process2) {
        if (process1.getPID() > process2.getPID()) 
            return 1; 
        else if (process1.getPID() < process2.getPID()) 
            return -1; 
        else
        	return 0;  
	}
}
