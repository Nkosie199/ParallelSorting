import java.util.concurrent.RecursiveAction;
/**
 *
 * @author Nkosingiphile
 */
public class BubbleSortParallel extends RecursiveAction{
    int lo; // arguments
    int hi;
    static int array[];
    static int n, c, d, swop;
    static int SEQUENTIAL_CUTOFF;
    
    public BubbleSortParallel(int[] arr, int lowerBound, int upperBound, int seqCut){
        array = arr;
        lo = lowerBound;
        hi = upperBound;
        SEQUENTIAL_CUTOFF = seqCut;
    }
    
    protected void compute(){// return answer - instead of run
        if((hi-lo) < SEQUENTIAL_CUTOFF) { //the size of the array
            bubbleSortMethod(array);
        }
        else{
            BubbleSortParallel left = new BubbleSortParallel(array,lo,(hi+lo)/2,SEQUENTIAL_CUTOFF);
            BubbleSortParallel right= new BubbleSortParallel(array,(hi+lo)/2,hi,SEQUENTIAL_CUTOFF);
            
            left.fork(); //
            right.compute();
            left.join(); 
        }
    }

    public static void bubbleSortMethod(int[] arr) {

        n = arr.length;
        for (c = 0; c < ( n - 1 ); c++) {
            for (d = 0; d < n - c - 1; d++) {
                if (array[d] > array[d+1]) /* For descending order use < */
                {
                    swop       = array[d];
                    array[d]   = array[d+1];
                    array[d+1] = swop;
                }
            }
        }
        //printArray(array);       
    }
    
    public static void printArray(int[] arr) {
        //System.out.println("Bubble sorted list of numbers");

        for (c = 0; c < n; c++) 
            System.out.println(arr[c]);
    }
    
    static int[] getFinalArray(){
        return array;
    }
}
