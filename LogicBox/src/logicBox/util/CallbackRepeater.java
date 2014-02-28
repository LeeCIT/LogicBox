


package logicBox.util;



/**
 * Spawns a thread which executes a callback at a set frequency.
 * Triggers the callback immediately.
 * @author Lee Coakley
 * @see Callback
 */
public class CallbackRepeater
{
	private Thread   thread;
	private Callback callback;
	private long 	 interval;
	private boolean  threadExecute;
	
	
	
	/**
	 * Starts a callback thread and runs it immediately.
	 * @param frequencyMillisecs How often to execute the callback.
	 * @param callback The callback to execute.
	 */
	public CallbackRepeater( long frequencyMillisecs, Callback callback ) {
		this.callback      = callback;
		this.interval      = frequencyMillisecs;
		this.threadExecute = true;
		
		this.thread = new Thread() {
			public void run() {
				threadLoop();
			}
		};
		
		thread.start();
	}
	
	
	
	/**
	 * Halts callback execution and joins the thread.
	 * The calling thread waits until this operation completes.
	 * Don't call this from inside the callback.
	 */
	public synchronized void join() {
		threadExecute = false;
		
		thread.interrupt();
		
		while (thread.isAlive()) {
			try {
				thread.join();
			}
			catch (InterruptedException ex) {
				Thread.interrupted();
			}
		}
	}
	
	
	
	/**
	 * Causes the repeater to stop after it completes the next execute-sleep cycle.
	 * The thread will exit normally.  The calling thread will NOT wait.
	 * You can safely call this from inside the callback.
	 */
	public synchronized void softStop() {
		threadExecute = false;
	}
	
	
	
	private void threadLoop() {
		while (threadExecute) {
			callback.execute();
		    sleep( interval );
		}
	}
	
	
	
	public static void sleep( long millis ) {
		try {
			Thread.sleep( millis );
		}
		catch (InterruptedException ex) {
			Thread.interrupted();
		}
	}
}











