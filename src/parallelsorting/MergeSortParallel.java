import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
/**
 *
 * @author Nkosingiphile
 */
public class MergeSortParallel extends RecursiveAction {
    static int[] array;
    static int[] tempMergArr;
    static int length;
    int lo; // arguments
    int hi;
    float tick; //the start time of the 
    
    static final int SEQUENTIAL_CUTOFF=4000;
    
    public MergeSortParallel(int[] arr, int lowerBound, int upperBound){
        array = arr;
        lo = lowerBound;
        hi = upperBound;
    }
    
    protected void compute(){// return answer - instead of run
        if((hi-lo) < SEQUENTIAL_CUTOFF) { //the size of the array
            
            mergeSortMethod(array);
        }
        else{
            MergeSortParallel left = new MergeSortParallel(array,lo,(hi+lo)/2);
            MergeSortParallel right= new MergeSortParallel(array,(hi+lo)/2,hi);
            
            left.fork(); //
            right.compute();
            left.join(); 
        }
    }
    
    public static void mergeSortMethod(int[] inputArray){
        System.out.println("Printing merge sorted array..."); 
        sort(inputArray);
        for(int i:inputArray){
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println("");
    }
     
    static void sort(int inputArr[]) {
        array = inputArr;
        length = inputArr.length;
        tempMergArr = new int[length];
        doMergeSort(0, length - 1);
    }
 
    static void doMergeSort(int lowerIndex, int higherIndex) {
         
        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            // Below step sorts the left side of the array
            doMergeSort(lowerIndex, middle);
            // Below step sorts the right side of the array
            doMergeSort(middle + 1, higherIndex);
            // Now merge both sides
            mergeParts(lowerIndex, middle, higherIndex);
        }
    }
 
    static void mergeParts(int lowerIndex, int middle, int higherIndex) {
 
        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergArr[i] = array[i];
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
            if (tempMergArr[i] <= tempMergArr[j]) {
                array[k] = tempMergArr[i];
                i++;
            } else {
                array[k] = tempMergArr[j];
                j++;
            }
            k++;
        }
        while (i <= middle) {
            array[k] = tempMergArr[i];
            k++;
            i++;
        }
 
    }
}
