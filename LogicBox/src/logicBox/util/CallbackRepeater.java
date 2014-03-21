


package logicBox.util;



/**
 * Spawns a thread which executes a callback at a set frequency.
 * @author Lee Coakley
 * @see Callback
 */
public class CallbackRepeater
{
	private Thread   thread;
	private Callback callback;
	private long 	 interval;
	private boolean  threadExecute;
	private boolean  threadPause;
	
	
	
	/**
	 * Starts a callback thread and runs it immediately.
	 * @param frequencyMillisecs How often to execute the callback.
	 * @param callback The callback to execute.
	 */
	public CallbackRepeater( long frequencyMillisecs, Callback callback ) {
		this( frequencyMillisecs, false, callback );
	}
	
	
	
	/**
	 * Optionally start paused.  If starting paused, nothing will happen until unpause() is called.
	 */
	public CallbackRepeater( long frequencyMillisecs, boolean startPaused, Callback callback ) {
		this.callback      = callback;
		this.interval      = frequencyMillisecs;
		this.threadExecute = true;
		
		this.thread = new Thread() {
			public void run() {
				threadLoop();
			}
		};
		
		if (startPaused)
			pause();
		
		threadStart();
	}



	/**
	 * Halts callback execution and joins the thread.
	 * If the repeater is paused, it is unpaused.
	 * The calling thread waits until this operation completes.
	 * Don't call this from inside the callback!
	 */
	public synchronized void join() {
		softStop();
		
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
	 * Causes the repeater to stop executing.
	 * If already paused there is no effect.
	 */
	public void pause() {
		synchronized (thread) {
			if (threadPause)
				return;
			
			threadPause = true;
			thread.interrupt();
		}
	}
	
	
	
	/**
	 * Causes the repeater to resume execution.
	 * If not paused, there is no effect.
	 */
	public void unpause() {
		synchronized (thread) {
			if ( ! threadPause)
				return;
				
			threadPause = false;
			thread.notify();
		}
	}
	
	
	
	/**
	 * Causes the repeater to stop without causing the calling thread to wait.
	 * If the repeater is paused, it will be unpaused.
	 * You can safely call this from inside the callback.
	 * After calling this function the repeater's lifetime has ended and it can no longer be used.
	 */
	public synchronized void softStop() {
		threadExecute = false;
		unpause();
	}
	
	
	
	private void threadStart() {
		thread.start();
	}
	
	
	
	private void threadLoop() {
		while (threadExecute) {
			threadWait();
			callback.execute();
		    threadSleep( interval );
		}
	}
	
	
	
	private void threadWait() {
		synchronized (thread) {
			while (threadPause) {
				try {
					thread.wait();
				}
				catch (InterruptedException ex) {
					Thread.interrupted();
				}
			}
		}
	}
	
	
	
	public static void threadSleep( long millis ) {
		try {
			Thread.sleep( millis );
		}
		catch (InterruptedException ex) {
			Thread.interrupted();
		}
	}
	
	
	
	
	
	public static void main( String[] args ) {
		final CallbackRepeater rep = new CallbackRepeater( 1000, true, new Callback() {
			public void execute() {
				System.out.println( "*** callback ***" );
			}
		});
		
		
		for (int i=0; i<16384; i++) {
			rep.pause();
			rep.unpause();
			rep.pause();
			rep.unpause();
			rep.pause();
			rep.pause();
			rep.unpause();
			rep.unpause();
		}
		
		System.out.println( "SLEEPING BRO" );
		threadSleep( 5000 );
		
		System.out.println( "UNPAUSING BRO" );
		rep.unpause();
		
		System.out.println( "JOINING BRO" );
		rep.join();
		
		System.out.println( "Done!" );
		
		rep.pause();
		
		rep.unpause();
		
		rep.join();
	}
}




































