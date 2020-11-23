
public class Main {
	public static void main(String[] args) {
		
		System.out.println("******************************************************************************************");
		System.out.println("Start of First Come First Serve Scheduling Simulation");
		FirstComeFirstServeScheduling firstComeFirstServeScheduling = new FirstComeFirstServeScheduling();
		firstComeFirstServeScheduling.run();
		System.out.println("End of First Come First Serve Scheduling Simulation");
		
		System.out.println("******************************************************************************************");
		System.out.println("Start of Round Robin Scheduling Simulation");
		RoundRobinScheduling roundRobinScheduling = new RoundRobinScheduling();
		roundRobinScheduling.run();
		System.out.println("End of Round Robin Scheduling Simulation");		
		
		System.out.println("******************************************************************************************");
		System.out.println("Start of Shortest Time Remaining First Scheduling Simulation");
		ShortestRemainingTimeFirstScheduling shortestRemainingTimeFirstScheduling = new ShortestRemainingTimeFirstScheduling();
		shortestRemainingTimeFirstScheduling.run();
		System.out.println("End of Shortest Time Remaining First Scheduling Simulation");		
		
		System.out.println("******************************************************************************************");
		System.out.println("Start of Priority Scheduling Simulation");
		PriorityScheduling priorityScheduling = new PriorityScheduling();
		priorityScheduling.run();
		System.out.println("End of Priority Scheduling Simulation");	
		System.out.println("******************************************************************************************");
	}		
}
