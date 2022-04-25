
public class FCFS extends Scheduler {

    public FCFS() {
        /* TODO: you _may_ need to add some code here */
        super();
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

        Process p=processes.get(0);
        return p;
    }

}
