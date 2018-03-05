import java.util.Arrays;
import java.util.Random;

public class IntQuicksort {

    private static final int INSERTIONSORT_THRESHOLD = 16;

    private IntQuicksort() {

    }

    public static void sort(Integer[] array) {
        sort(array, 0, array.length);
    }

    @SuppressWarnings("Duplicates")
    public static void sort(Integer[] array, int fromIndex, int toIndex) {
        while (true) {
            int rangeLength = toIndex - fromIndex;

            if (rangeLength < 2) {
                return;
            }

            if (rangeLength < INSERTIONSORT_THRESHOLD) {
                insertionsort(array, fromIndex, toIndex);
                return;
            }

            int distance = rangeLength / 4;

            int a = array[fromIndex + distance];
            int b = array[fromIndex + (rangeLength >>> 1)];
            int c = array[toIndex - distance];

            int pivot = Util.median(a, b, c);
            int leftPartitionLength = 0;
            int rightPartitionLength = 0;
            int index = fromIndex;

            while (index < toIndex - rightPartitionLength) {
                int current = array[index];

                if (current > pivot) {
                    ++rightPartitionLength;
                    Util.swap(array, toIndex - rightPartitionLength, index);
                } else if (current < pivot) {
                    Util.swap(array, fromIndex + leftPartitionLength, index);
                    ++index;
                    ++leftPartitionLength;
                } else {
                    ++index;
                }
            }

            if (leftPartitionLength < rightPartitionLength) {
                sort(array, fromIndex, fromIndex + leftPartitionLength);
                fromIndex = toIndex - rightPartitionLength;
            } else {
                sort(array, toIndex - rightPartitionLength, toIndex);
                toIndex = fromIndex + leftPartitionLength;
            }
        }
    }

    private static void insertionsort(Integer[] array, int fromIndex, int toIndex) {
        for (int i = fromIndex + 1; i < toIndex; ++i) {
            int current = array[i];
            int j = i  - 1;

            while (j >= fromIndex && array[j] > current) {
                array[j + 1] = array[j];
                --j;
            }

            array[j + 1] = current;
        }
    }

    private static final int SIZE = 50000;
    private static final int FROM = 0;
    private static final int TO = SIZE;

    public static void main(String[] args) {
        long seed = System.nanoTime();
        Random random = new Random(seed);
        Integer[] array1 = getRandomArray(SIZE, 1, 100000000, random);
        Integer[] array2 = array1.clone();
//        int[] array3 = array1.clone();

        long startTime = System.nanoTime();
        IntQuicksort.sort(array1, FROM, TO);
        long endTime = System.nanoTime();

        System.out.printf("No parallel : %.2f milliseconds.\n",
                (endTime - startTime) / 1e6);

        startTime = System.nanoTime();
        ParallelIntQuicksort.sort(array2, FROM, TO);
        endTime = System.nanoTime();

        System.out.printf("Parallel : %.2f milliseconds.\n",
                (endTime - startTime) / 1e6);

        boolean isArraysEquals = Arrays.equals(array1, array2);

        System.out.println("ArraysEquals? = " + isArraysEquals);
    }

    public static Integer[] getRandomArray(int size,
                                       int minimum,
                                       int maximum,
                                       Random random) {
        Integer[] array = new Integer[size];

        for (int i = 0; i < size; ++i) {
            array[i] = random.nextInt(maximum - minimum + 1) + minimum;
        }

        return array;
    }
}