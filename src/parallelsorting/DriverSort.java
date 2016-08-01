import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author gmdnko003
 */
public class DriverSort {
    static List<String> lineList;
    
    static int[] array; //the integer array
    
    static boolean j = false; //boolean to find if arrayList is past the first index
    
    static long startTime = 0;
    static Scanner sc;
    static Random rn = new Random();
    static float time;
    static String outFile;
    
    private static void tick(){
	startTime = System.currentTimeMillis();
    }
    private static float tock(){
	return (System.currentTimeMillis() - startTime) / 1000.0f; 
    }
    static final ForkJoinPool fjPool = new ForkJoinPool();
    
    public static void main (String[] args){
        //args[0].split(" "); //takes in command line parameters:...      
        //checking for number of processors
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Number of available processors "+runtime.availableProcessors()+"");
        
        //1st parameter <sort>: Is a word that specifies the sorting algorithm, either Mergesort or Quicksort or, optionally, Altsort;
        System.out.println("Please enter a word that specifies the sorting algorithm, either Mergesort or Quicksort or Altsort;");
        sc = new Scanner(System.in);
        String sortType = sc.nextLine();
        
        //2nd parameter <arraySizeMin>: Size of the smallest (random) array of integers to be sorted
        System.out.println("Please enter the size of the smallest (random) array of integers to be sorted");
        sc = new Scanner(System.in);
        int smallestArraySize = sc.nextInt();
        
        //3rd parameter <arraySizeMax>: Size of the largest (random) array of integers to be sorted
        System.out.println("Please enter the size of the largest (random) array of integers to be sorted");
        sc = new Scanner(System.in);
        int largestArraySize = sc.nextInt();
        
        //4th parameter <arratSizeIncr>: The step size for increasing the array of integers to be sorted
        System.out.println("Please enter the step size for increasing the array of integers to be sorted");
        sc = new Scanner(System.in);
        int increment = sc.nextInt();
        
        //5th parameter <outFileName>: Is the name of the file to which the output data will be written
        System.out.println("Please enter the name of the output text file");
        sc = new Scanner(System.in);
        outFile = sc.nextLine();
        
        //now we need a loop to randomly generate integer arrays....
        for (int i=smallestArraySize; i<largestArraySize; i=i+increment){
            array = new int[i]; //initaite an integer array of size i
            
            for (int l=0; l<i; l++){ //for each element in that array of size i
               int randomInt = rn.nextInt(100); //populate the index with a random integer
               array[l] = randomInt;
            }
            //debug print this array
            //debugPrintArray(array);
            
            if (sortType == "mergesort"){
                //MergeSortParallel msp = new MergeSortParallel(array, smallestArraySize, largestArraySize);
                tick();
                fjPool.invoke(new MergeSortParallel(array, smallestArraySize, largestArraySize));
                time = tock();
                System.out.println("Run took "+ time +" seconds");
                System.out.println("");
            }
            else if (sortType == "quicksort"){
                //QuickSortParallel qsp = new QuickSortParallel(array, smallestArraySize, largestArraySize);
                tick();
                fjPool.invoke(new QuickSortParallel(array, smallestArraySize, largestArraySize));
                time = tock();
                System.out.println("Run took "+ time +" seconds");
                System.out.println("");
            }
            else if (sortType == "altsort"){
                //AltSortParallel asp = new AltSortParallel(array, smallestArraySize, largestArraySize);
                
            }           
        }       
        //next...
        
    }
    
    //concluding methods...
    
    public static void debugPrintArray(int[] debugableArray){
        System.out.println("The values of the debugable array are as follows...");
        for (int i = 0; i < debugableArray.length; i++) {
            System.out.print(debugableArray[i]+", ");             
        }
        System.out.println("");
    }
    
    /*
        This method is used to checks if the naive and parallel arrays 
    */
    public static void filePrintAll(int[] naiveArray, int[] parallelArray) throws IOException{
        PrintWriter writer = new PrintWriter(outFile, "UTF-8");
        writer.println("Output results for naive and parallel solutions...");
        writer.printf("%-22s%-22s%s\n","Index","SerialArray","ParallelArray");
        writer.println("");
        for (int i=0; i<naiveArray.length; i++){
            writer.printf("%-22s%-22s%s\n",i+" : ",naiveArray[i],parallelArray[i]);
            writer.println("");
        }
        writer.close();
    }


}