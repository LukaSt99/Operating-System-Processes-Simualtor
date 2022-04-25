import java.util.ArrayList;

public class NextFit extends MemoryAllocationAlgorithm {

    private static int currentlyIndex=0;
    private static int currentlyAddress=0;

    public NextFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        ArrayList<MemorySlot> certainSlots = new ArrayList<>();
        int [] Block_indexes = getBlockIndexes();

        int j=0;
        for (int i = currentlyIndex; j < availableBlockSizes.length; i = (i+2) % Block_indexes.length ) {

            if (p.getMemoryRequirements() <= availableBlockSizes[i / 2]) {
                if (!currentlyUsedMemorySlots.isEmpty()) {

                    for (MemorySlot slot : currentlyUsedMemorySlots) {
                        if (slot.getBlockStart() == Block_indexes[i] && slot.getBlockEnd() == Block_indexes[i + 1]) {
                            certainSlots.add(slot);
                        }
                    }
                    if (fit(p, i, certainSlots) != -1) {
                        address = fit(p, i, certainSlots);
                        break;
                    }

                } else {
                    address = Block_indexes[i];
                    break;
                }
            }
            certainSlots.clear();
            j++;
        }
        for (int i = 0; i < Block_indexes.length; i+=2) {
            if(address>=Block_indexes[i] && address<=Block_indexes[i+1]){
                currentlyIndex=i;
                break;
            }
        }
        return address;
    }

    private int fit(Process p ,int index, ArrayList<MemorySlot> slots){

        int possible_address=-1;
        if(!slots.isEmpty()){

            boolean block[] = new boolean [availableBlockSizes[index/2]];
            for(MemorySlot slot : slots) {

                for (int i = slot.getStart(); i <= slot.getEnd(); i++) {
                    block[i- slot.getBlockStart()] = true;
                }
            }

            int free=0;
            for (int j = 0; j <= block.length-p.getMemoryRequirements(); j++) {
                for(int k=j; k<j+p.getMemoryRequirements() ;k++){
                    if(block[j] == false){
                        free++;
                    }
                }
                if(free==p.getMemoryRequirements()) {
                    possible_address=getBlockIndexes()[index]+j;
                }
            }

        } else{
            possible_address=getBlockIndexes()[index];
        }
        return possible_address;
    }

    private int[] getBlockIndexes(){

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

}
