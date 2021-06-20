import java.util.concurrent.RecursiveAction;
/**
 *
 * @author Nkosingiphile
 */
public class MergeSortParallel extends RecursiveAction {
    static int[] array;
    static int[] tempArray;
    static int[] newArray;
    static int length;
    int lo; // arguments
    int hi;
    float tick; //the start time of the 
    
    static int SEQUENTIAL_CUTOFF;
    
    public MergeSortParallel(int[] arr, int lowerBound, int upperBound, int seqCut){
        array = arr;
        lo = lowerBound;
        hi = upperBound;
        SEQUENTIAL_CUTOFF = seqCut;
    }
    
    protected void compute(){// return answer - instead of run
        if((hi-lo) < SEQUENTIAL_CUTOFF) { //the size of the array
            
            mergeSortMethod(array);
        }
        else{
            MergeSortParallel left = new MergeSortParallel(array,lo,(hi+lo)/2,SEQUENTIAL_CUTOFF);
            MergeSortParallel right= new MergeSortParallel(array,(hi+lo)/2,hi,SEQUENTIAL_CUTOFF);
            
            left.fork(); //  
            left.join();
            right.compute();
        }
    }
    
    public static void mergeSortMethod(int[] inputArray){
         
        sort(inputArray);
//        for(int i:inputArray){
//            System.out.print(i);
//            System.out.print(" ");
//        }
//        System.out.println("");
    }
     
    static void sort(int inputArr[]) {
        array = inputArr;
        length = inputArr.length;
        tempArray = new int[length];
        mergeSort(0, length - 1);
    }
 
    static void mergeSort(int loIndex, int hiIndex) {
         
        if (loIndex < hiIndex) {
            int mid = loIndex + (hiIndex - loIndex) / 2;
            
            mergeSort(loIndex, mid);
            
            mergeSort(mid + 1, hiIndex);
            
            mergeParts(loIndex, mid, hiIndex);
        }
    }
 
    static void mergeParts(int lowerIndex, int middle, int higherIndex) {
 
        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempArray[i] = array[i];
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) { //while there are still values on either array
            if (tempArray[i] <= tempArray[j]) {
                array[k] = tempArray[i];
                i++;
            } else {
                array[k] = tempArray[j];
                j++;
            }
            k++;
        }
        while (i <= middle) {
            array[k] = tempArray[i];
            k++;
            i++;
        }
 
    }
    
    public static int[] getFinalArray(){
        return array;
    }
}
