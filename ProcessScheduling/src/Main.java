
public class Main {
	public static void main(String[] args) {
		//reset system timer
		Helper.currentTime = 0;
		Helper.processCounter = 1;
		System.out.println("Start of First Come First Serve Scheduling Simulation");
		FirstComeFirstServeScheduling firstComeFirstServeScheduling = new FirstComeFirstServeScheduling();
		firstComeFirstServeScheduling.run();
		System.out.println("End of First Come First Serve Scheduling Simulation");
		
		//reset system timer
		Helper.currentTime = 0;
		//reset process counter
		Helper.processCounter = 1;
		System.out.println("******************************************************************************************");
		System.out.println("Start of Round Robin Scheduling Simulation");
		RoundRobinScheduling roundRobinScheduling = new RoundRobinScheduling();
		roundRobinScheduling.run();
		System.out.println("End of Round Robin Scheduling Simulation");		
		
		//reset system timer
		Helper.currentTime = 0;
		//reset process counter
		Helper.processCounter = 1;
		System.out.println("******************************************************************************************");
		System.out.println("Start of Shortest Time Remaining First Scheduling Simulation");
		ShortestRemainingTimeFirstScheduling shortestRemainingTimeFirstScheduling = new ShortestRemainingTimeFirstScheduling();
		shortestRemainingTimeFirstScheduling.run();
		System.out.println("End of Shortest Time Remaining First Scheduling Simulation");		
		
		//reset system timer
		Helper.currentTime = 0;
		//reset process counter
		Helper.processCounter = 1;
		System.out.println("******************************************************************************************");
		System.out.println("Start of Priority Scheduling Simulation");
		PriorityScheduling priorityScheduling = new PriorityScheduling();
		priorityScheduling.run();
		System.out.println("End of Priority Scheduling Simulation");	
	}		
}
