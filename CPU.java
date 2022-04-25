import java.util.ArrayList;

public class CPU {

    public static int clock = 0; // this should be incremented on every CPU cycle

    private Scheduler scheduler;
    private MMU mmu;
    private Process[] processes;
    private int currentProcess;

    public CPU(Scheduler scheduler, MMU mmu, Process[] processes) {
        this.scheduler = scheduler ;
        this.mmu = mmu ;
        this.processes = processes ;
    }
    public void run() {
        /* TODO: you need to add some code here
         * Hint: you need to run tick() in a loop, until there is nothing else to do... */

        boolean RUN;
        do {

            //for processes that require more memory than blocks can provide
            int count=0;
            for(int i = 0; i < processes.length; i++){
                for(int j=0;j<mmu.getAvailableBlockSizes().length;j++) {
                    if (processes[i].getMemoryRequirements() > mmu.getAvailableBlockSizes()[j]){
                        count++;
                    }
                }
                if(count==mmu.getAvailableBlockSizes().length){
                    processes[i].setDisable(true);
                }
                count=0;
            }

            RUN=false;

            for (int i = 0; i < processes.length; i++) {
                if(processes[i].getPCB().getState()==ProcessState.NEW  && mmu.loadProcessIntoRAM(processes[i]) && !processes[i].isDisable()) {
                    processes[i].getPCB().setState(ProcessState.READY, clock);
                }
            }
            for (int i = 0; i < processes.length; i++) {
                if (processes[i].getArrivalTime() <= clock && processes[i].getPCB().getState()==ProcessState.READY
                        && !scheduler.processes.contains(processes[i]) && !processes[i].isDisable()) {
                    scheduler.addProcess(processes[i]);
                }
            }
            tick();

            for (Process p: processes) {
                if (p.getPCB().getState() != ProcessState.TERMINATED && !p.isDisable()){
                    RUN = true;
                    break;
                }
            }
        } while(RUN) ;
    }

    public void tick() {
        /* TODO: you need to add some code here
         * Hint: this method should run once for every CPU cycle */

        if(!scheduler.isEmpty()) {
            Process p = scheduler.getNextProcess();
            currentProcess=p.getPCB().getPid();
            p.run(); //or  processes[currentProcess-1].run();

            if (p.getBurstTime() == 0) {
                p.getPCB().setState(ProcessState.TERMINATED, clock);
                scheduler.removeProcess(p);
                mmu.loadProcessIntoRAM(p);
            }
        }
        clock ++;
    }
}