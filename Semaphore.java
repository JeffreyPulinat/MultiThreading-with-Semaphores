public class Semaphore{		//given semaphore class

   private int value;	//keep track if needs to wait or not

   public Semaphore(int value) {
      this.value = value;

   }

   public synchronized void acquire() {
      while (value <= 0) {		//while 0 or less wait. Once released value gets incremented
         try {
            wait();
         }
         catch (InterruptedException e) { }
      }

      value--;			//decrements value
   }

   public synchronized void release() { //release lock
      ++value;		//increment value

      notify();
   }
}
