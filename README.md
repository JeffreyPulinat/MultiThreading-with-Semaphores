# MultiThreading-with-Semaphores
reading multiple files with a thread each and writing to one file.

[Design Overview:]

FileServer which allows mulitple writers (Threads) to read and write the contents of file in synchronized fashion. 
The FileServer allows Multiple Writers to read a local file Writer#File.txt (where # is the Writer ID) 
and write to ServerFile.txt in a synchronized fashion. Writers can write in following 3 modes: FCFS, Random and LIFO

FileSever test args[] before starting the threads. 

java FileServer #W [mode] 
 
#W: Number of Writer Threads(from 1 to 9 writers)
mode (optional): F for FCFS ; R for Random ; L for LIFO
if mode is not provided or accepted. R is default

------------------------------------------------------------------------------
[Complete Specification]

******
FCFS: Reader method locks thread and reads from corresponding file.
It then passes the line read by file to writer method that writes the line to ServerFile.txt.
After writing it releases the thread.
read method of FCFS
			mutex.acquire();	//lock
			br = new BufferedReader(new FileReader(new File("Writer" + threadId + "File.txt")));
			
			String line = "";
			while((line=br.readLine())!=null){ //reading line by line till end o file
			FwriteFile(line);		//passes string to writer method
			
			}
			mutex.release();	//release lock

*****
RANDOM: Reader method similar to FCFS. Gets line from corresponding file but puts it into an arraylist
Only diff from FCFS
		while((line=br.readLine())!=null){		//read line by line. till end of file
			myArray.add(line);
		}	
Writer method only gets called after all threads finish. in FileServer.java
		tp.shutdown();
		boolean finished = tp.awaitTermination(1, TimeUnit.MINUTES);
		//check if all threads are finished and Mode is Random. Then calls Random.
		if ((finished == true) && (Mode == "R")) Server.RwriteFile();


******
LIFO: Reads similar to FCFS. Except pushes line into stack.
Only diff from FCFS
	while((line=br.readLine())!=null){		//read line by line till end of file.
			myStack.push(line);				//pushes line to Stack
			System.out.println(line);
			}

Writer method only gets called after all threads finish.
in FileServer.java
		tp.shutdown();
		boolean finished = tp.awaitTermination(1, TimeUnit.MINUTES);
		//check if all threads are finished and Mode is LIFO. Then calls LwriteFile.
		if ((finished == true) && (Mode == "L")) Server.LwriteFile();

Writer method pops the line from the stack and writes to ServerFile.txt till the stack is empty.

******
//Server.java
initializing the BufferedWriter
Write to specific file. Makes file if doesnt exist or overwrites current file.
bfwriter = new BufferedWriter(new FileWriter("ServerFile.txt"));

----------------------------------------------------
[Known Bugs]

Not sure if random is supposed to work like i implemented.
I tried have no lock on reader method 
and a lock on writer method. so one thread writes at a time writes
But some lines were being skipped. Not all lines were being outputted.

So I implemented my current method(mentioned above).
