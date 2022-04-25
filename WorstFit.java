import java.util.ArrayList;

public class WorstFit extends MemoryAllocationAlgorithm {

    private ArrayList<Integer> availableBlocks;
    public WorstFit(int[] availableBlockSizes) {
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
        availableBlocks = new ArrayList<>();

        int [] Block_indexes = getBlockIndexes();
        for (int i = 0; i < Block_indexes.length; i += 2) {

            if (p.getMemoryRequirements() <= availableBlockSizes[i / 2]) {
                if (!currentlyUsedMemorySlots.isEmpty()) {

                    for (MemorySlot slot : currentlyUsedMemorySlots) {
                        if (slot.getBlockStart() == Block_indexes[i] && slot.getBlockEnd() == Block_indexes[i + 1]) {
                            certainSlots.add(slot);
                        }
                    }

                    availableMemory(p,i,certainSlots);

                } else {
                    availableBlocks.add(availableBlockSizes[i/2]);
                    availableBlocks.add(Block_indexes[i]);

                }
            }
            certainSlots.clear();
        }
        if(!availableBlocks.isEmpty()) {
            int maxIndex=0, maxAvailableMemory = availableBlocks.get(0);
            for (int y = 0; y < availableBlocks.size(); y += 2) {
                if (availableBlocks.get(y) > maxAvailableMemory) {
                    maxAvailableMemory = availableBlocks.get(y);
                    maxIndex=y;
                }
            }
            address=availableBlocks.get(maxIndex+1);
        }
        return address;
    }

    private void availableMemory(Process p, int index, ArrayList<MemorySlot> slots){

        int count=0;
        boolean block[] = new boolean[availableBlockSizes[index / 2]];
        if(!slots.isEmpty()) {

            for (MemorySlot slot : slots) {
                for (int i = slot.getStart(); i <=  slot.getEnd(); i++) {
                    block[i - slot.getBlockStart()] = true;
                }
            }

            int k=0,free=0;
            for (int j = 0; j < block.length; j++) {
                if(block[j]==false){
                    k=j;
                    while(k < block.length && block[k]!=true ){
                        free++;
                        k++;
                    }
                    if(free>=p.getMemoryRequirements()) {
                        availableBlocks.add(free);
                        availableBlocks.add(getBlockIndexes()[index]+j);
                    }
                    free = 0;
                    j=k-1;
                }

            }

        }else{
            availableBlocks.add(availableBlockSizes[index/2]);
            availableBlocks.add(getBlockIndexes()[index]);
        }
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
