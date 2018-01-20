import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 * 
 * @author 	Akhil Batra
 * @version	1.1 - April 16, 2016
 * 
**/
public class CountingSort {
    
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     *  does not touch original array (non-destructive method)
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
    **/
    public static int[] naiveCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            if (i > max) {
                max = i;
            }
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i] += 1;
        }

        // put the value count times into a new array
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }

        // return the sorted array
        return sorted;
    }

    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     *  does not touch original array (non-destructive method)
     * 
     * @param toSort int array that will be sorted
    **/
    public static int[] betterCountingSort(int[] toSort) {
        // find number of negative and positive integers
        int nPos = 0, nNeg = 0;
        for (int i : toSort) {
            if (i >= 0) {
                nPos++;
            } else {
                nNeg++;
            }
        }
        // store negative values as its inverse
        int[] pos = new int[nPos], neg = new int[nNeg];
        int kPos = 0, kNeg = 0;
        for (int i : toSort) {
            if (i >= 0) {
                pos[kPos] = i;
                kPos++;
            } else {
                neg[kNeg] = -i;
                kNeg++;
            }
        }
        // sort positive and negative numbers separately, then combine
        int[] posSorted = new int[0];
        int[] negSorted = new int[0];
        if (nPos != 0) {
            posSorted = naiveCountingSort(pos);
        }
        if (nNeg != 0) {
            negSorted = naiveCountingSort(neg);
        }
        int[] sorted = new int[toSort.length];
        int k = 0;
        for (int i = 0; i < negSorted.length; i++) {
            sorted[k] = -negSorted[negSorted.length - 1 - i];
            k++;
        }
        for (int i : posSorted) {
            sorted[k] = i;
            k++;
        }
        return sorted;
    }

    @Test
    public void countingSortTest() {
        int[] toSort = new int[]{9, -6, 15, 0, 3, 0, -2, 101};
        CountingSortTester.assertIsSorted(betterCountingSort(toSort));
        toSort = new int[]{-9, -6, -15, -3, -1, -2, -101};
        CountingSortTester.assertIsSorted(betterCountingSort(toSort));
        toSort = new int[]{9, 6, 15, 3, 7, 2, 101};
        CountingSortTester.assertIsSorted(betterCountingSort(toSort));
    }
}
