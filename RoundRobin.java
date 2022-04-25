

public class RoundRobin extends Scheduler {

    private int quantum;
    private static int count=0;

    public RoundRobin() {
        super();
        this.quantum = 1; // default quantum
        /* TODO: you _may_ need to add some code here */

    }

    public RoundRobin(int quantum) {
        this();
        this.quantum = quantum;
    }

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
        if(p!=null /*&& (p.getPCB().getState()==ProcessState.READY || p.getPCB().getState()==ProcessState.RUNNING )*/) {
            processes.add(p);
            p.waitInBackground();
        }
    }

    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
        Process process=processes.get(0);
        if(count == quantum && process.getBurstTime()-1==0) {
            removeProcess(process);
            addProcess(process);
            count = 0;
            count++;
        }
        else
        if(count == quantum ){
            removeProcess(process);
            addProcess(process);
            count=0;
            count++;
        }else
        if(count != quantum && process.getBurstTime()-1==0) {
            count=0;
        }else
            count++;
        return processes.get(0);
    }
}
