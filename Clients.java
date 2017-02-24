import java.io.IOException;

public class Clients implements Runnable {	//Thread class

	private int threadId;
	private String mode;
	private Server myServer;

	//Constructor
	public Clients(int threadId, String mode, Server myServer){
		this.threadId = threadId;
		this.mode = mode;
		this.myServer = myServer;
	}

	public int  getThreadId(){
		return this.threadId;
	}

	//runs corresponding method in Server
	public void  run(){
		try {
			//FCFS
			if(mode == "F") myServer.FreadFile(this.threadId);

			//RANDOM
			if(mode == "R") myServer.RreadFile(this.threadId);

			//LIFO
			if(mode == "L") myServer.LreadFile(this.threadId);
		} catch (IOException e) {
			e.printStackTrace();
		}



	}
}
