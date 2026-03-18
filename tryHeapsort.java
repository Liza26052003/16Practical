import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
// cunsaltation of Google AI and VS code Agent was used
/**
 * CSC 211 - Practical 6: Heap Construction and Sorting
 * Developed with the assistance of an AI.
 */


public class tryHeapsort {

    public static void main(String[] args) {
        // 1. small dataset for testing (Section 2c)
        String[] originalData = {"banana", "apple", "cherry", "date", "elderberry", "fig", "grape"};

        // 2. Test with small set first (Section 2c)
        System.out.println("Testing with small array...");
        runTest(originalData.clone());
        // 2b. Test with Ulysses words (Section 2d) which are on the anagram.tex file
         // read a latex file
          System.out.println("\n-----------this array is not a required output ----------\n");
        String[] ulyssesWords = loadUlyssesWords();
        if (ulyssesWords != null && ulyssesWords.length > 0) {
          // System.out.println("\nTesting with Ulysses words...");
            runTest(ulyssesWords);
        } else {
           // System.out.println("No data loaded from anagram.tex; skipping Ulysses test.");
        } 
        System.out.println("\n----------------------\n");
        // 3. Timing for larger dataset (Section 2d/e)//
       
        String[] largeData = loadAnagramWords();
        
        if (largeData != null && largeData.length > 0) {
           System.out.println("large data set timings:- ");
           runTimings(largeData);
        } else {
            System.out.println("No data loaded from anagram.tex; skipping timing test.");
        }
    }
    // --- HEAP METHODS --- //

    public static void buildHeapBottomUp(String[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(arr, n, i);
        }
    }

    public static void buildHeapTopDown(String[] arr) {
        // Repeatedly "inserting" by sifting up each new element//
        for (int i = 1; i < arr.length; i++) {
            siftUp(arr, i);
        }
    }

    /**
     * Shared sorting logic (Section 2d)
     */
    public static void heapSortFromHeap(String[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            swap(arr, 0, i); // Move current root to end
            siftDown(arr, i, 0); // Restore heap property for remaining array
        }
    }

    // --- HELPER METHODS ---

    private static void siftDown(String[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left].compareTo(arr[largest]) > 0) largest = left;
        if (right < n && arr[right].compareTo(arr[largest]) > 0) largest = right;

        if (largest != i) {
            swap(arr, i, largest);
            siftDown(arr, n, largest);
        }
    }

    private static void siftUp(String[] arr, int i) {
        int parent = (i - 1) / 2;
        if (i > 0 && arr[i].compareTo(arr[parent]) > 0) {
            swap(arr, i, parent);
            siftUp(arr, parent);
        }
    }

    private static void swap(String[] arr, int i, int j) {
        String temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static void runTest(String[] data) {
        String[] bottomUpData = data.clone();
        buildHeapBottomUp(bottomUpData);
        heapSortFromHeap(bottomUpData);
        System.out.println("Bottom-up Sort: " + Arrays.toString(bottomUpData));

        String[] topDownData = data.clone();
        buildHeapTopDown(topDownData);
        heapSortFromHeap(topDownData);
        System.out.println("Top-down Sort:  " + Arrays.toString(topDownData));
    }

    private static void runTimings(String[] data) {
        // Timing for Bottom-up Heap Construction
        long startTimeBottomUp = System.nanoTime();
        buildHeapBottomUp(data.clone());
        long endTimeBottomUp = System.nanoTime();
        long durationBottomUp = (endTimeBottomUp - startTimeBottomUp);
        System.out.println("Bottom-up Heap Construction Time: " + durationBottomUp + " ns");

        // Timing for Top-down Heap Construction
        long startTimeTopDown = System.nanoTime();
        buildHeapTopDown(data.clone());
        long endTimeTopDown = System.nanoTime();
        long durationTopDown = (endTimeTopDown - startTimeTopDown);
        System.out.println("Top-down Heap Construction Time: " + durationTopDown + " ns");
    }

    private static String[] loadUlyssesWords() {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("anagram.tex"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Basic cleaning: remove punctuation and convert to lowercase
                String[] lineWords = line.toLowerCase().split("[\\s\\p{Punct}]+");
                for (String word : lineWords) {
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return new String[0];
        }

        return words.toArray(new String[0]);
    }

    private static String[] loadAnagramWords() {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("anagram.tex"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Remove LaTeX comments
                line = line.replaceAll("%.*", "");
                // Remove common LaTeX commands and braces
                line = line.replaceAll("\\\\[a-zA-Z@]+\\*?(\\[[^\\]]*\\])?(\\{[^}]*\\})?", " ");
                line = line.replaceAll("[{}]", " ");
                // Keep only letters and whitespace
                line = line.replaceAll("[^a-zA-Z\\s]", " ");
                // Basic cleaning: split and lowercase
                String[] lineWords = line.toLowerCase().trim().split("\\s+");
                for (String word : lineWords) {
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return new String[0];
        }

        return words.toArray(new String[0]);
    }
}