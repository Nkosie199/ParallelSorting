import java.util.concurrent.RecursiveAction;

/**
 *
 * @author Nkosingiphile
 */
public class QuickSortParallel extends RecursiveAction {
    int lo; // arguments
    int hi;
    static int array[];
    int newArray[];
    static int SEQUENTIAL_CUTOFF;
    
    
    public QuickSortParallel(int[] arr, int lowerBound, int upperBound, int seqCut){
        array = arr;
        lo = lowerBound;
        hi = upperBound;
        SEQUENTIAL_CUTOFF = seqCut;
    }
    
    protected void compute(){// return answer - instead of run

        if((hi-lo) < SEQUENTIAL_CUTOFF) { //the size of the array
            quickSortMethod(array);
        }
        else{
            QuickSortParallel left = new QuickSortParallel(array,lo,(hi+lo)/2,SEQUENTIAL_CUTOFF);
            QuickSortParallel right= new QuickSortParallel(array,(hi+lo)/2,hi,SEQUENTIAL_CUTOFF);
            
            left.fork(); //
            right.compute();
            left.join(); 
        }
    }
    
    public static void quickSortMethod(int[] arr) {
        
        int n = arr.length;
        sort(arr, 0, n-1);
        //printArray(arr);
    }

    static int part(int tempArray[], int low, int high)
    {
        int pivot = tempArray[high]; 
        int i = (low-1);
        for (int j=low; j<=high-1; j++)
        {
            if (tempArray[j] <= pivot)
            {
                i++;
                int temp = tempArray[i];
                tempArray[i] = tempArray[j];
                tempArray[j] = temp;
            }
        }

        int temp = tempArray[i+1];
        tempArray[i+1] = tempArray[high];
        tempArray[high] = temp;
 
        return i+1;
    }
 
    static void sort(int tempArray[], int low, int high)
    {
        if (low < high)
        {
            int pi = part(tempArray, low, high);

            sort(tempArray, low, pi-1);
            sort(tempArray, pi+1, high);
        }
    }
 

    static void printArray(int arr[])
    {
        
        for (int i=0; i<arr.length; ++i)
            System.out.print(arr[i]+" ");
        System.out.println();
    }
 
    static int[] getFinalArray(){
        return array;
    }
}
