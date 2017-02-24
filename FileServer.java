import java.io.IOException;
import java.util.concurrent.*;
public class FileServer {

	//global variables
	static String Mode = new String("");
	static int numOfThreads;
	static private Server myServer;




	public static void main(String args[]) throws IOException, InterruptedException{

		myServer = new Server();
		TestingArgs(args);// Method to test args parameters. Sets numOfThreads and Mode.
		ExecutorService tp = Executors.newFixedThreadPool(numOfThreads);	//create pool
		for(int i = 0; i < numOfThreads; i++){ //loop for threads
			Thread mythread = new Thread(new Clients(i, Mode,myServer)); // create threads
			tp.execute(mythread);// execute threads
		}

		tp.shutdown(); // ensures that no new tasks are accepted.
		boolean finished = tp.awaitTermination(1, TimeUnit.MINUTES);
		//check if all threads are finished and Mode is LIFO. Then calls LwriteFile.
		if ((finished == true) && (Mode == "L")) myServer.LwriteFile();

		//check if all threads are finished and Mode is Random. Then calls RwriteFile.
		if ((finished == true) && (Mode == "R")) myServer.RwriteFile();
	}


	/*Testing arg parameters
	 * Tests and sets numOfThreads
	 * Tests and sets Mode
	 */
	private static void TestingArgs(String[] args){

		// ******numOfThreads checking****
		if(args.length==0){ //no args given
			System.err.println("Number of threads not given (#W).   Ex:java FileServer #W [mode] ");
			System.exit(0);
		}
		try{
			numOfThreads =Integer.parseInt(args[0]);	//Parses int
			if (!(numOfThreads >= 1) && (numOfThreads <=9 )) { //if not from 1 to 9. error and exit
				System.err.println("Writer number must be from 1 to 9");
				System.exit(0);
			}
		}
		catch(NumberFormatException nfe) {		//If args[0] can not be parsed. exit
			System.err.println("Give a number of threads(#W).  Ex:java FileServer #W [mode]  ");
			System.exit(0);
		}

		//*******Mode checking*******
		try{
			if((args[1].length()==1)){ //check if args[1] is 1 or greater
				if( args[1].equals("F")){ //FCFS
					Mode = "F" ;
				}
				else if( args[1].equals("R")) { //Random
					Mode = "R";
				}
				else if(args[1].equals("L")){ //LIFO
					Mode ="L";
				}
			}else { //if not F,R,L. Print error and default to Random.
				System.err.println("Not accepted mode. Using R");
				Mode = "R";
			}
		}
		catch(ArrayIndexOutOfBoundsException ioe){ // No args[1] given. error and default Random
			System.err.println("No mode mentioned. Using default mode Random");
			Mode = "R";
		}
	}

}
