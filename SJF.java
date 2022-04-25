
public class SJF extends Scheduler {

    public SJF() {
        /* TODO: you _may_ need to add some code here */
        super();
    }

    public void addProcess(Process p) {

        /* TODO: you need to add some code here */

        if(p!=null /*&& (p.getPCB().getState()==ProcessState.READY || p.getPCB().getState()==ProcessState.RUNNING )*/) {

            if(processes.size()==0){
                processes.add(p);
                p.waitInBackground();
            }
            else{

                processes.add(p);
                for (int i = 1; i < processes.size(); i++) {
                    for (int j = processes.size() - 1; j > i; j--) {
                        if (processes.get(i).getBurstTime() > processes.get(j).getBurstTime()) {
                            Process tmp = processes.get(i);
                            processes.set(i, processes.get(j));
                            processes.set(j, tmp);
                        }
                    }
                }
                p.waitInBackground();
            }

        }
    }
    //return the process with the lowest burst time value
    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
        Process p=processes.get(0);
        return p;
    }
}
