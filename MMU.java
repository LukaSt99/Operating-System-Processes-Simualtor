
import java.util.ArrayList;

public class MMU {

    private final int[] availableBlockSizes;
    private MemoryAllocationAlgorithm algorithm;
    private ArrayList<MemorySlot> currentlyUsedMemorySlots;


    public MMU(int[] availableBlockSizes, MemoryAllocationAlgorithm algorithm) {
        this.availableBlockSizes = availableBlockSizes;
        this.algorithm = algorithm;
        this.currentlyUsedMemorySlots = new ArrayList<MemorySlot>();
    }

    public boolean loadProcessIntoRAM(Process p) {

        boolean fit = false;
        /* TODO: you need to add some code here
         * Hint: this should return true if the process was able to fit into memory
         * and false if not */

        if(p.getPCB().getState()==ProcessState.TERMINATED){
            currentlyUsedMemorySlots.remove(p.getMemorySlot());
            return false;
        }
        int address=algorithm.fitProcess(p,currentlyUsedMemorySlots);
        if( address!=-1){
            int address_End=address+p.getMemoryRequirements()-1;

            int [] BlockIndexes= getIndexes();
            for(int i=0; i<BlockIndexes.length; i+=2){
                if(address>=BlockIndexes[i] && address_End <= BlockIndexes[i+1]){
                    MemorySlot s = new MemorySlot(address,address_End,BlockIndexes[i], BlockIndexes[i+1]);
                    currentlyUsedMemorySlots.add(s);
                    p.setMemorySlot(s);
                    break;
                }
            }
            fit=true;

        }
        return fit;
    }
    private int[] getIndexes(){

        int availableBlock_indexes[] = new int[availableBlockSizes.length * 2];
        availableBlock_indexes[0] = 0;
        for (int i = 1; i < availableBlock_indexes.length; i++) {
            if (i % 2 == 0)
                availableBlock_indexes[i] = availableBlock_indexes[i - 1] + 1;
            else
                availableBlock_indexes[i] = availableBlock_indexes[i - 1] + availableBlockSizes[i / 2] - 1;
        }
        return availableBlock_indexes;
    }

    public int[] getAvailableBlockSizes() {
        return availableBlockSizes;
    }
}
