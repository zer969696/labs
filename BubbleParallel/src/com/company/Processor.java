package com.company;

import java.util.GregorianCalendar;

public class Processor implements Runnable {

    private int[] array;
    private int processorID;
    private Thread thread;
    private int workTime;
    private boolean isInvolved;

    public final int[] getArray() { return array; }
    public final int getProcessorID() { return processorID; }
    public final int getWorkTime() { return workTime; }
    public final boolean isInvolved() { return isInvolved; }

    public final void setInvolved(boolean involved) { isInvolved = involved; }
    public final void setArray(int[] aArray) { array = aArray; }
    public final void setProcessorID(int aProcessorID) { processorID = aProcessorID; }

    public final boolean isAlive() { return thread.isAlive(); }

    public Processor() {
        this(0);
    }

    Processor(int aProcessorID) {
        this(aProcessorID, null, null);
    }
    private Processor(int aProcessorID, int[] aArray, Thread aThread) {
        array = aArray;
        thread = (aThread == null)? new Thread(this): aThread;
        processorID = aProcessorID;
        workTime = 0;
        isInvolved = false;
    }

    private void sort() {
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array.length - i - 1; j++){
                if(array[j] > array[j+1]){
                    array[j] = array[j] + array[j+1];
                    array[j+1] = array[j] - array[j+1];
                    array[j] = array[j] - array[j+1];
                }
            }
        }
    }

    public final void start() {
        thread.run();
    }
    @Override
    public void run() {
        long start = new GregorianCalendar().getTimeInMillis();
        sort();
        long end = new GregorianCalendar().getTimeInMillis();
        workTime = workTime + ((int)(end - start));
    }
}
