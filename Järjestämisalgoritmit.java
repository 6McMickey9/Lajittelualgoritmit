import java.util.*;

public class SortTest {

    // Funktioreferenssi: jokainen lajittelualgoritmi sopii tähän muotoon.
    // (Jos sinulla on oma FunctionRef.java, voit käyttää sitä tämän tilalla.)
    interface Sorter {
        void sort(int[] data);
    }

    // ---------------------------------------------------------------
    // Lajittelualgoritmit
    // ---------------------------------------------------------------

    public static void insertionSort(int[] data) {
        int n = data.length;
        for (int i = 1; i < n; ++i) {
            int key = data[i];
            int j = i - 1;
            while (j >= 0 && data[j] > key) {
                data[j + 1] = data[j];
                j = j - 1;
            }
            data[j + 1] = key;
        }
    }

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped)
                break;
        }
    }

    public static void arraysSort(int[] data) {
        Arrays.sort(data);
    }

    public static void shellSort(int[] a) {
        int n = a.length;
        int[] cols = {1391376, 463792, 198768, 86961, 33936, 13776, 4592,
                      1968, 861, 336, 112, 48, 21, 7, 3, 1};
        for (int k = 0; k < cols.length; k++) {
            int h = cols[k];
            for (int i = h; i < n; i++) {
                int v = a[i];
                int j = i;
                while (j >= h && a[j - h] > v) {
                    a[j] = a[j - h];
                    j = j - h;
                }
                a[j] = v;
            }
        }
    }

    // --- Quicksort (perusversio, pivot keskeltä) ---
    public static void quickSort(int[] a) {
        quickSort(a, 0, a.length - 1);
    }

    private static void quickSort(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int p = a[(lo + hi) >>> 1];
        int i = lo, j = hi;
        while (i <= j) {
            while (a[i] < p) i++;
            while (a[j] > p) j--;
            if (i <= j) {
                int t = a[i]; a[i] = a[j]; a[j] = t;
                i++;
                j--;
            }
        }
        quickSort(a, lo, j);
        quickSort(a, i, hi);
    }

    // --- Quicksort (optimoitu) ---
    // 1) mediaani-of-kolme pivotin valinta
    // 2) pienet osataulukot lajitellaan insertion sortilla
    // 3) rekursio vain pienempään osaan, isompi käydään silmukalla
    private static final int QS_CUTOFF = 16;

    public static void quickSortOptimized(int[] a) {
        quickSortOpt(a, 0, a.length - 1);
    }

    private static void quickSortOpt(int[] a, int lo, int hi) {
        while (hi - lo >= QS_CUTOFF) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < a[lo]) swap(a, mid, lo);
            if (a[hi] < a[lo]) swap(a, hi, lo);
            if (a[hi] < a[mid]) swap(a, hi, mid);
            int p = a[mid];

            int i = lo, j = hi;
            while (i <= j) {
                while (a[i] < p) i++;
                while (a[j] > p) j--;
                if (i <= j) {
                    swap(a, i, j);
                    i++;
                    j--;
                }
            }
            if (j - lo < hi - i) {
                quickSortOpt(a, lo, j);
                lo = i;
            } else {
                quickSortOpt(a, i, hi);
                hi = j;
            }
        }
        insertionRange(a, lo, hi);
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void insertionRange(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= lo && a[j] > key) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    // --- MergeSort (perusversio) ---
    public static void mergeSort(int[] a) {
        int[] tmp = new int[a.length];
        mergeSort(a, tmp, 0, a.length - 1);
    }

    private static void mergeSort(int[] a, int[] tmp, int lo, int hi) {
        if (lo >= hi) return;
        int mid = (lo + hi) >>> 1;
        mergeSort(a, tmp, lo, mid);
        mergeSort(a, tmp, mid + 1, hi);
        merge(a, tmp, lo, mid, hi);
    }

    private static void merge(int[] a, int[] tmp, int lo, int mid, int hi) {
        System.arraycopy(a, lo, tmp, lo, hi - lo + 1);
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            if (tmp[i] <= tmp[j]) a[k++] = tmp[i++];
            else a[k++] = tmp[j++];
        }
        while (i <= mid) a[k++] = tmp[i++];
        while (j <= hi) a[k++] = tmp[j++];
    }

    // --- MergeSort (optimoitu) ---
    // 1) pienet osataulukot insertion sortilla
    // 2) jos osat ovat jo järjestyksessä (a[mid] <= a[mid+1]), yhdistämistä ei tehdä
    private static final int MS_CUTOFF = 16;

    public static void mergeSortOptimized(int[] a) {
        int[] tmp = new int[a.length];
        mergeSortOpt(a, tmp, 0, a.length - 1);
    }

    private static void mergeSortOpt(int[] a, int[] tmp, int lo, int hi) {
        if (hi - lo < MS_CUTOFF) {
            insertionRange(a, lo, hi);
            return;
        }
        int mid = (lo + hi) >>> 1;
        mergeSortOpt(a, tmp, lo, mid);
        mergeSortOpt(a, tmp, mid + 1, hi);
        if (a[mid] <= a[mid + 1]) return;
        merge(a, tmp, lo, mid, hi);
    }

    // ---------------------------------------------------------------
    // Taulukoiden luonti
    // ---------------------------------------------------------------

    public static int[] createAscendingTable(int n) {
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = i;
        }
        return data;
    }

    public static int[] createDescendingTable(int n) {
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = n - i;
        }
        return data;
    }

    public static int[] createRandomData(int n) {
        int[] data = new int[n];
        Random gen = new Random();
        for (int i = 0; i < n; i++) {
            data[i] = gen.nextInt(n);
        }
        return data;
    }

    // ---------------------------------------------------------------
    // Apumetodit
    // ---------------------------------------------------------------

    public static void printData(int[] data) {
        for (int x : data) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    public static boolean isSorted(int[] data) {
        for (int i = 1; i < data.length; i++) {
            if (data[i - 1] > data[i]) return false;
        }
        return true;
    }

    // Mittaa yhden algoritmin ajan. Lajitellaan kopio, jotta alkuperäinen säilyy.
    public static double measure(Sorter sorter, int[] original) {
        int[] copy = original.clone();
        long start = System.nanoTime();
        sorter.sort(copy);
        long stop = System.nanoTime();
        if (!isSorted(copy)) {
            System.out.println("VIRHE: taulukko ei ole järjestyksessä!");
        }
        return (stop - start) / 1000000000.0;
    }

    // Ajaa kaikki algoritmit yhdelle taulukolle ja tulostaa tulokset
    public static void runAll(String title, int[] data, String[] names, Sorter[] sorters) {
        System.out.println("=== " + title + " (n = " + data.length + ") ===");
        for (int i = 0; i < sorters.length; i++) {
            double t = measure(sorters[i], data);
            System.out.printf("%-22s %10.4f s%n", names[i], t);
        }
        System.out.println();
    }

    // ---------------------------------------------------------------
    // Pääohjelma
    // ---------------------------------------------------------------

    public static void main(String[] args) {
        int size = 100000;
        if (args.length > 0) {
            size = Integer.parseInt(args[0]);
        }

        // Algoritmit funktioreferensseinä (metodiviittaukset)
        String[] names = {
            "Insertion sort", "Bubble sort", "Shellsort", "Arrays.sort",
            "Quicksort", "Quicksort (optim.)", "MergeSort", "MergeSort (optim.)"
        };
        Sorter[] sorters = {
            SortTest::insertionSort, SortTest::bubbleSort, SortTest::shellSort, SortTest::arraysSort,
            SortTest::quickSort, SortTest::quickSortOptimized, SortTest::mergeSort, SortTest::mergeSortOptimized
        };

        // Lämmittelyajo, jotta JIT-kääntäjä ehtii optimoida (tasaisempi mittaus)
        int[] warm = createRandomData(2000);
        for (Sorter s : sorters) {
            measure(s, warm);
        }

        runAll("Satunnainen taulukko", createRandomData(size), names, sorters);
        runAll("Nouseva taulukko", createAscendingTable(size), names, sorters);
        runAll("Laskeva taulukko", createDescendingTable(size), names, sorters);
    }
}