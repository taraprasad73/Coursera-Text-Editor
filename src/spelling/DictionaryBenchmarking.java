package spelling;


/** A class for timing the Dictionary Implementations
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 */

public class DictionaryBenchmarking {

	
	public static void main(String [] args) {

	    // Run each test more than once to get bigger numbers and less noise.
	    // You can try playing around with this number.
	    int trials = 10;

	    // The text to test on
	    String dictFile = "data/dict.txt";
		
	    // The amount of words to increment each step
	    // You can play around with this
		int increment = 200;

		// The number of steps to run.  
		// You can play around with this.
		int numSteps = 40;
		
		// The number of words to start with. 
		// You can play around with this.
		int start = 100;
		
		String notInDictionary = "notaword";
		
		// TODO: Play around with the numbers above and graph the output to see trends in the data
		for (int numToCheck = start; numToCheck < numSteps*increment + start; 
				numToCheck += increment)
		{
			// Time the creation of finding a word that is not in the dictionary.
			DictionaryLL llDict = new DictionaryLL();
			DictionaryBST bstDict = new DictionaryBST();
			
			DictionaryLoader.loadDictionary(llDict, dictFile, numToCheck);
			DictionaryLoader.loadDictionary(bstDict, dictFile, numToCheck);
			
			long startTime = System.nanoTime();
			for (int i = 0; i < trials; i++) {
				llDict.isWord(notInDictionary);
			}
			long endTime = System.nanoTime();
			long timeLL = (endTime - startTime);  
			double dtimeLL = timeLL/(double)(1E9);
			
			startTime = System.nanoTime();
			for (int i = 0; i < trials; i++) {
				bstDict.isWord(notInDictionary);
			}
			endTime = System.nanoTime();
			long timeBST = (endTime - startTime);
			double dtimeBST = timeBST/(double)(1E9);
			
			System.out.printf("%d\t%.9f\t%.9f\n",numToCheck, dtimeLL, dtimeBST);
			
		}
	
	}
	
}
