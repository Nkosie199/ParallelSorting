import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
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
    static int[] finalArray;
    static float seqCut[] = {1000000, 10, 100, 1000}; //sequential cutoff: 1, 5 and 50 threads respectively
    //therefore array size must between 1000 & 1000000 in order for both sequential and parallel algorithms to take place
    
    //run input parameters...
    static String sortType;
    static int arraySizeMin;
    static int arraySizeMax;
    static int increment;
    static String outFile;
    static PrintWriter writer;
    
    //variables to store output parameters
    static ArrayList<Float> bestTime = new ArrayList<>();
    static ArrayList<Float> bestNoOfThreads = new ArrayList<>();
    
    private static void tick(){
	startTime = System.currentTimeMillis();
    }
    
    private static float tock(){
	return (System.currentTimeMillis() - startTime) / 1000.0f; 
    }
    
    private static float getBestTime(ArrayList<Float> times){
        float best = times.get(0);
        
        for(float t: times){
            //System.out.println("Time #"+times.indexOf(t)+" = "+t);
            if (t < best){
                best = t;
            }
        }
        return best;
    }
    
    
    static final ForkJoinPool fjPool = new ForkJoinPool();
    
    public static void main (String[] args){
        //takes in command line parameters:... 
        sortType = args[0];
        arraySizeMin = Integer.parseInt(args[1]);
        arraySizeMax = Integer.parseInt(args[2]);
        increment = Integer.parseInt(args[3]);
        outFile = args[4];     
        
	System.gc();	  
	/*
        //checking for number of processors
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Number of available processors "+runtime.availableProcessors()+"");
        
        //1st parameter <sort>: Is a word that specifies the sorting algorithm, either Mergesort or Quicksort or, optionally, Altsort;
        System.out.println("Please enter a word that specifies the sorting algorithm, either 'mergesort', 'quicksort' or 'bubblesort'");
        sc = new Scanner(System.in);
        sortType = sc.nextLine();
        
        //2nd parameter <arraySizeMin>: Size of the smallest (random) array of integers to be sorted
        System.out.println("Please enter the size of the smallest (random) array of integers to be sorted");
        //System.out.println("(prefereably greater than 1000 in order for both sequential and parallel algorithms to work)");
        sc = new Scanner(System.in);
        arraySizeMin = sc.nextInt();
        
        //3rd parameter <arraySizeMax>: Size of the largest (random) array of integers to be sorted
        System.out.println("Please enter the size of the largest (random) array of integers to be sorted");
        //System.out.println("(prefereably less than 1000000 in order for both sequential and parallel algorithms to work)");
        sc = new Scanner(System.in);
        arraySizeMax = sc.nextInt();
        
        //4th parameter <arratSizeIncr>: The step size for increasing the array of integers to be sorted
        System.out.println("Please enter the step size for increasing the array of integers to be sorted");
        sc = new Scanner(System.in);
        increment = sc.nextInt();
        
        //5th parameter <outFileName>: Is the name of the file to which the output data will be written
        System.out.println("Please enter the name of the output text file");
        sc = new Scanner(System.in);
        outFile = sc.nextLine();
        */
        
        try {
            writer = new PrintWriter(outFile, "UTF-8");
            writer.println("Output results...");
            writer.printf("%-22s%-22s%-22s%s\n","ArraySize","OptimalNumThreads","BestTime","BestSpeedup");
            writer.println("");
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DriverSort.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DriverSort.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //now we need a loop to randomly generate integer arrays....
        for (int i=arraySizeMin; i<arraySizeMax; i=i+increment){
            array = new int[i]; //initaite an integer array of size i
            
            for (int l=0; l<i; l++){ //for each element in that array of size i
               int randomInt = rn.nextInt(100); //populate the index with a random integer
               array[l] = randomInt;
            }
            //debug print this array
            //debugPrintArray(array);
            switch (sortType) {
                case "mergesort":
                    //MergeSortParallel msp = new MergeSortParallel(array, smallestArraySize, largestArraySize);
                    System.out.println("Printing merge sorted array...");
                    
                    for (int k=0; k<seqCut.length; k++){
                        tick();
                        fjPool.invoke(new MergeSortParallel(array, arraySizeMin, arraySizeMax, (int) seqCut[k]));
                        time = tock();
                        //
                        finalArray = MergeSortParallel.getFinalArray();
/*
                        for(int v:finalArray){ //prints out final array 
                            System.out.print(v);
                            System.out.print(" ");
                        }
                        System.out.println("");
*/                       
                        //
                        System.out.println("Run took "+ time +" seconds");
                        System.out.println("Number of threads: "+(int) arraySizeMax/seqCut[k]);
                        bestTime.add(time);
                        bestNoOfThreads.add(arraySizeMax/seqCut[k]);
                        //System.out.println("");                      
                    }
                    //writer.printf("%-22s%-22s%s-22s%s\n","ArraySize","OptimalNumThreads","BestTime","BestSpeedup");
                    writer.printf("%-22s%-22s%-22s%s\n",i,bestNoOfThreads.get(bestTime.indexOf(getBestTime(bestTime))), getBestTime(bestTime), getBestTime(bestTime)/bestTime.get(0));                       
                    break;
                case "quicksort":
                    //QuickSortParallel qsp = new QuickSortParallel(array, smallestArraySize, largestArraySize);
                    System.out.println("Printing quick sorted array...");
                    
                    for (int k=0; k<seqCut.length; k++){
                        tick();
                        fjPool.invoke(new QuickSortParallel(array, arraySizeMin, arraySizeMax, (int) seqCut[k]));
                        time = tock();
                        //
                        finalArray = QuickSortParallel.getFinalArray();
/*
                        for(int v:finalArray){
                            System.out.print(v);
                            System.out.print(" ");
                        }
                        System.out.println("");
*/
                        //
                        System.out.println("Run took "+ time +" seconds");
                        System.out.println("Number of threads: "+(int) arraySizeMax/seqCut[k]);
                        bestTime.add(time);
                        bestNoOfThreads.add(arraySizeMax/seqCut[k]);
                        //System.out.println("");
                    }
                    //writer.printf("%-22s%-22s%s-22s%s\n","ArraySize","OptimalNumThreads","BestTime","BestSpeedup");
                    writer.printf("%-22s%-22s%-22s%s\n",i,bestNoOfThreads.get(bestTime.indexOf(getBestTime(bestTime))), getBestTime(bestTime), getBestTime(bestTime)/bestTime.get(0));                    
                    break;
                case "bubblesort":
                    //BubbleSortParallel bsp = new BubbleSortParallel(array, smallestArraySize, largestArraySize);
                    System.out.println("Printing bubble sorted array...");
                    
                    for (int k=0; k<seqCut.length; k++){
                        tick();
                        fjPool.invoke(new BubbleSortParallel(array, arraySizeMin, arraySizeMax, (int) seqCut[k]));
                        time = tock();
                        //
                        finalArray = BubbleSortParallel.getFinalArray();
/*
                        for(int v:finalArray){
                            System.out.print(v);
                            System.out.print(" ");
                        }
                        System.out.println("");
*/
                        //
                        System.out.println("Run took "+ time +" seconds");
                        System.out.println("Number of threads: "+(int) arraySizeMax/seqCut[k]);
                        bestTime.add(time);
                        bestNoOfThreads.add(arraySizeMax/seqCut[k]);
                        //System.out.println("");
                    }
                    //writer.printf("%-22s%-22s%s-22s%s\n","ArraySize","OptimalNumThreads","BestTime","BestSpeedup");
                    writer.printf("%-22s%-22s%-22s%s\n",i,bestNoOfThreads.get(bestTime.indexOf(getBestTime(bestTime))), getBestTime(bestTime), getBestTime(bestTime)/bestTime.get(0));              
                    break;
            }
        }       
        //next...
        writer.close();
    }
}
