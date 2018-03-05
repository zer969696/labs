package com.company;

import java.util.Random;

public class ProcessorController {

    private static ProcessorController self = null;

    private static final int DEFAULT_PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int DEFAULT_ARRAY_LENGTH = 50000;

    private Processor[] processors;
    private int[] array;

    private ProcessorController(int aProcessorCount, int aArrayLength) {
        super();

        processors = new Processor[aProcessorCount];
        for(int i = 0; i<aProcessorCount; ++i) {
            processors[i] = new Processor(i);
        }
        array = new int[aArrayLength];

        self = this;
    }

    public static synchronized ProcessorController getInstance(int aProcessorCount, int aArrayLength) {
        return (self == null)? new ProcessorController(aProcessorCount, aArrayLength) : self;
    }

    public static synchronized ProcessorController getInstance() {
        return (self == null)? new ProcessorController(DEFAULT_PROCESSOR_COUNT, DEFAULT_ARRAY_LENGTH) : self;
    }

    public void fillArray() {
        Random r = new Random();
        for(int i = 0; i<array.length; ++i) {
            array[i] = r.nextInt(array.length);
        }
    }

    public int[] getArray() {
        return array;
    }

    public void start() {
        boolean isOdd = true;
        int[] tmp = null;
        do {
            for(int i = 0; i<processors.length; ++i) {
                if(!isOdd && i == processors.length - 1) break;
                tmp = splitArray(processors[i].getProcessorID(), isOdd);
                if(!isSorted(tmp)){
                    processors[i].setArray(tmp);
                    processors[i].setInvolved(true);
                    processors[i].start();
                }
                else {
                    processors[i].setInvolved(false);
                }
            }
            for(int i = 0; i<processors.length; ++i) {
                if(!isOdd && i == processors.length - 1) break;
                if(processors[i].isInvolved()){
                    while(processors[i].isAlive()) {
                        try{ Thread.sleep(1); } catch(Exception ignored) {}
                    }
                    joinArray(processors[i].getProcessorID(), isOdd, processors[i].getArray());
                }
            }
            isOdd = !isOdd;
        } while(!isSorted());
    }

    public void printArray() {
        for (int anArray : array) {
            System.out.print(anArray + " ");
        }
        System.out.println();
    }

    public void printStatistics() {
        int pcWorkTime = 0;
        for (Processor processor : processors) {
            pcWorkTime += processor.getWorkTime();
//            System.out.println("ID: " + processor.getProcessorID() +
//                    "\nWork time: " + processor.getWorkTime() + "ms");
        }
//        System.out.println("-------\nWork time: " + pcWorkTime + "ms");
        System.out.println("Параллельная сортировка: " + pcWorkTime + "ms");

    }

    private final int[] splitArray(int processorID, boolean isOdd){
        int from = processorID*(array.length/processors.length) +
                ((isOdd)? 0 : array.length/(processors.length * 2));
        int to = (processorID + 1)*(array.length/processors.length) +
                ((isOdd)? 0 : array.length/(processors.length * 2));

        int[] tmp = new int[array.length/processors.length];

        for (int i = from, j = 0; i<to; i++, j++){
            tmp[j] = array[i];
        }

        return tmp;
    }

    private final void joinArray(int processorID, boolean isOdd, int[] tmp){
        int from = processorID*(array.length/processors.length) +
                ((isOdd)? 0 : array.length/(processors.length * 2));
        int to = (processorID + 1)*(array.length/processors.length) +
                ((isOdd)? 0 : array.length/(processors.length * 2));

        for (int i = from, j = 0; i<to; i++, j++){
            array[i] = tmp[j];
        }
    }

    private final boolean isSorted() {
        for(int i = 0; i<array.length - 1; i++)
            if(array[i] > array[i + 1]) return false;
        return true;
    }

    private final boolean isSorted(int[] array) {
        for(int i = 0; i<array.length - 1; i++)
            if(array[i] > array[i + 1]) return false;
        return true;
    }

}