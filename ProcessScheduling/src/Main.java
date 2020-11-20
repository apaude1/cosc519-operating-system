
public class Main {
	public static void main(String[] args) {
		//reset system timer
		Helper.currentTime = 0;
		Helper.processCounter = 1;
		System.out.println("Start of First Come First Serve Scheduling Simulation");
		FirstComeFirstServeScheduling firstComeFirstServeScheduling = new FirstComeFirstServeScheduling();
		firstComeFirstServeScheduling.run();
		firstComeFirstServeScheduling.displayAccountingInformation();
		System.out.println("End of First Come First Serve Scheduling Simulation");
		
		//reset system timer
		Helper.currentTime = 0;
		//reset process counter
		Helper.processCounter = 1;
		System.out.println("Start of Round Robin Scheduling Simulation");
		RoundRobinScheduling roundRobinScheduling = new RoundRobinScheduling();
		roundRobinScheduling.run();
		roundRobinScheduling.displayAccountingInformation();
		System.out.println("End of Round Robin Scheduling Simulation");		
		
		//reset system timer
		Helper.currentTime = 0;
		//reset process counter
		Helper.processCounter = 1;
		System.out.println("Start of Shortest Time Remaining First Scheduling Simulation");
		ShortestRemainingTimeFirstScheduling shortestRemainingTimeFirstScheduling = new ShortestRemainingTimeFirstScheduling();
		shortestRemainingTimeFirstScheduling.run();
		System.out.println("End of Shortest Time Remaining First Scheduling Simulation");		
		
		//reset system timer
		Helper.currentTime = 0;
		//reset process counter
		Helper.processCounter = 1;
		System.out.println("Start of Priority Scheduling Simulation");
		PriorityScheduling priorityScheduling = new PriorityScheduling();
		priorityScheduling.run();
		priorityScheduling.displayAccountingInformation();
		System.out.println("End of Priority Scheduling Simulation");	
	}
		/*// TODO Auto-generated method stub
		//System.out.println("This is our CPU Scheduling project");
		 long id = getPID();
		 int pid = (int) id;
        System.out.println("-----------------Running First Come First Serve (FCFS)----------------");
        fcfs(pid);
        System.out.println("-----------------Running Shortest Job First (SJF)-----------------");
        sjf(pid);
        System.out.println("-----------------Running Priority-----------------");
        ps(pid);
        System.out.println("-----------------Running RoundRobin(RR)------------------");
        rr(pid);
	}
        
        public static void fcfs(int pid)
        {
        	Scheduler fcfs = new FirstComeFirstServe();
        	fcfs.add(new Process(pid, "P1", 0, 5));
        	fcfs.add(new Process(pid, "P2", 2, 4));
        	fcfs.add(new Process(pid, "P3", 4, 3));
        	fcfs.add(new Process(pid, "P4", 6, 6));
        	fcfs.process();
        	display(fcfs);
        }
        
        public static void sjf(int pid)
        {
        }
        
        public static void ps(int pid)
        {
        }
        
        public static void rr(int pid)
        {
        }
        
        public static void display(Scheduler obj)
        {
            System.out.println("pid\tProcess\tAT\tBT\tWT\tTAT");

            for (Process prc : obj.getProcess())
            {
                System.out.println(prc.getPID() + "\t" +  prc.getProcessName() + "\t" + prc.getArrivalTime() + "\t" + prc.getBurstTime() + "\t" + prc.getWaitingTime() + "\t" + prc.getTurnaroundTime());
            }
            
            System.out.println();
            
            for (int i = 0; i < obj.getTimeline().size(); i++)
            {
                List<Event> prc_timeline = obj.getTimeline();
                System.out.print(prc_timeline.get(i).getStartTime() + "(" + prc_timeline.get(i).getProcessName() + ")");
                
                if (i == obj.getTimeline().size() - 1)
                {
                    System.out.print(prc_timeline.get(i).getFinishTime());
                }
            }
            
            System.out.println("\n\nAverage Wait Time: " + obj.getAverageWaitingTime() + "\nAverage Turn Arounf Time: " + obj.getAverageTurnAroundTime());
        }
        
        public static long getPID() {
            String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
            if (processName != null && processName.length() > 0) {
                try {
                    return Long.parseLong(processName.split("@")[0]);
                }
                catch (Exception e) {
                    return 0;
                }
            }

            return 0;
        }*/
}
