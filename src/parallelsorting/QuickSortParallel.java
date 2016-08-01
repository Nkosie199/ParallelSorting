import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author Nkosingiphile
 */
public class QuickSortParallel extends RecursiveAction {
    int lo; // arguments
    int hi;
    int array[];
    //= {10, 7, 8, 9, 1, 5};
    
    static final int SEQUENTIAL_CUTOFF=4000;
    
    public QuickSortParallel(int[] arr, int lowerBound, int upperBound){
        array = arr;
        lo = lowerBound;
        hi = upperBound;
    }
    
    protected void compute(){// return answer - instead of run
        if((hi-lo) < SEQUENTIAL_CUTOFF) { //the size of the array
            quickSortMethod(array);
        }
        else{
            QuickSortParallel left = new QuickSortParallel(array,lo,(hi+lo)/2);
            QuickSortParallel right= new QuickSortParallel(array,(hi+lo)/2,hi);
            
            left.fork(); //
            right.compute();
            left.join(); 
        }
    }
    
    public static void quickSortMethod(int[] arr) {
        
        int n = arr.length;
 
        sort(arr, 0, n-1);
        printArray(arr);
    }
    /* This function takes last element as pivot,
       places the pivot element at its correct
       position in sorted array, and places all
       smaller (smaller than pivot) to left of
       pivot and all greater elements to right
       of pivot */
    static int partition(int arr[], int low, int high)
    {
        int pivot = arr[high]; 
        int i = (low-1); // index of smaller element
        for (int j=low; j<=high-1; j++)
        {
            // If current element is smaller than or
            // equal to pivot
            if (arr[j] <= pivot)
            {
                i++;
 
                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
 
        // swap arr[i+1] and arr[high] (or pivot)
        int temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;
 
        return i+1;
    }
 
 
    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    static void sort(int arr[], int low, int high)
    {
        if (low < high)
        {
            /* pi is partitioning index, arr[pi] is 
              now at right place */
            int pi = partition(arr, low, high);
 
            // Recursively sort elements before
            // partition and after partition
            sort(arr, low, pi-1);
            sort(arr, pi+1, high);
        }
    }
 
    /* A utility function to print array of size n */
    static void printArray(int arr[])
    {
        System.out.println("Pringing quick sorted array...");
        for (int i=0; i<arr.length; ++i)
            System.out.print(arr[i]+" ");
        System.out.println();
    }
 
    
}
