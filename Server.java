import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
public class Server {

	private static Semaphore mutex ; 	//read lock
	private static BufferedWriter bfwriter;		//writer
	private static BufferedReader br;			//reader
	private static Stack<String> myStack;		//Stack of strings
	private static ArrayList<String> myArray;	//ArrayList of strings

	//Constructor
	public Server() throws IOException{
		mutex = new Semaphore(1);		//first thread will skip lock if 1.
		bfwriter = new BufferedWriter(new FileWriter("ServerFile.txt")); //Write to specific file. Makes file or overwrites current file.
		myStack = new Stack<>();
		myArray = new ArrayList<>();
	}


//********FCFS**********
	public void FreadFile(int threadId) throws IOException{
		try{
			mutex.acquire();	//lock
			br = new BufferedReader(new FileReader(new File("Writer" + threadId + "File.txt")));//corresponding file for thread

			String line = "";
			while((line=br.readLine())!=null){ //reading line by line till end o file
				FwriteFile(line);		//passes string to writer method
			}
			mutex.release();	//release lock
		}

	catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
	}


	public  void FwriteFile(String file){

		try{	//writes line to file
			bfwriter.write(file);		//writes line to ServerFile.txt
			bfwriter.newLine();			//makes new in line txtfile
			bfwriter.flush();

		} catch (Exception e) {
		   e.printStackTrace();
		}

	}



//******RANDOM*********
	public  void RreadFile(int threadId) throws IOException{ // works like FCFS read except puts line into arraylist
		try{
			
			mutex.acquire();
			br = new BufferedReader(new FileReader(new File("Writer" + threadId + "File.txt")));//Thread reads corresponding file
			String line = "";
			while((line=br.readLine())!=null){		//read line by line. till end of file
				myArray.add(line);
			}
			mutex.release();
		}
		catch(FileNotFoundException ex){
			ex.printStackTrace();
		}

	}

	//Lwrite method gets called when all threads are finished executing
	public  void RwriteFile(){

		try{
			Collections.shuffle(myArray);	//shuffles arraylist. Randomizes
			for (int i = 0; i < myArray.size(); i++) {
				String rando = myArray.get(i);
				bfwriter.write(rando);		//Writes line to ServerFile.txt
				bfwriter.newLine();			//makes a new line
				bfwriter.flush();
			}

		} catch (Exception e) {
		   e.printStackTrace();
		}

	}



//********LIFO********
	public  void LreadFile(int threadId) throws IOException{	//Reads like FCFS but puts line in a Stack
		mutex.acquire();
		try{


		//lock thread
		System.out.println(threadId);
		br = new BufferedReader(new FileReader(new File("Writer" + threadId + "File.txt"))); //Thread reads corresponding file

		String line = "";
		while((line=br.readLine())!=null){		//read line by line till end of file.
			myStack.push(line);				//pushes line to Stack
			}
		mutex.release();
		}
		catch(FileNotFoundException ex){
			ex.printStackTrace();
		}
	}

	//Lwrite method gets called when all threads are finished executing
	public  void LwriteFile(){
		try{
			while(!myStack.isEmpty()){	//While the stack is not empty
				String last = myStack.pop();	//pop. gets last element.
				bfwriter.write(last);			//writes line to ServerFile.txt
				bfwriter.newLine();				//makes a new line
				bfwriter.flush();
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }

	}
}
