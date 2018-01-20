import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort
{

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     *  The array can only have ASCII Strings (sequence of 1 byte characters)
     *  The sorting is stable and non-destructive
     *  The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] asciis)
    {
        // get maximum string length
        int maxLength = Integer.MIN_VALUE;
        for (String s : asciis) {
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }
        String[] sorted = asciis;
        // LSD algorithm (sorting from least significant digit)
        for (int i = 0; i < maxLength; i++) {
//            System.out.println(Arrays.toString(sorted));
            sorted = sortByDigit(sorted, maxLength - 1 - i);
        }
        return sorted;
    }

    /**
     * Returns an array of strings sorted by the char at given index.
     * @param asciis Strings to be sorted
     * @param index index of char to sort
     * @return
     */
    private static String[] sortByDigit(String[] asciis, int index) {
        // collect all the chars at the given index
        int[] charsToSort = new int[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            String s = asciis[i];
            if (s.length() <= index) {
                // if string is not long enough, treat it to be smallest
                charsToSort[i] = Character.MIN_VALUE;
            } else {
                charsToSort[i] = s.charAt(index);
            }
        }
        // counting-sort the chars
        int[] sortedChars = CountingSort.naiveCountingSort(charsToSort);
        // rearrange strings based on the sorted chars
        String[] sortedStrings = new String[asciis.length];
        boolean[] visited = new boolean[asciis.length]; // Key: Keep track of strings that has already been placed
        for (int i = 0; i < asciis.length; i++) {
            // loop through sorted chars
            for (int j = 0; j < asciis.length; j++) {
                // loop through unsorted chars
                if (!visited[j] && charsToSort[j] == sortedChars[i]) {
                    // If this string is not visited and corresponding char is the same as the sorted one,
                    // put this string in the sorted string array. Guarantees stability.
                    sortedStrings[i] = asciis[j];
                    visited[j] = true;
                    break;
                }
            }
        }
        return sortedStrings;
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     *  destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelper(String[] asciis, int start, int end, int index)
    {

    }

    @Test
    public void radixSortTest() {
        String[] toSort = new String[]{"aab", "a", "zac", "pqwwf", "#5$", "c", "77e67"};
        String[] actual = sort(toSort);
//        System.out.println(Arrays.toString(actual));
        Arrays.sort(toSort);
//        System.out.println(Arrays.toString(toSort));
        assertArrayEquals(toSort, actual);
    }
}
