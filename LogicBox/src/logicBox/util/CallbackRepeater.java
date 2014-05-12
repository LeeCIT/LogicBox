


package logicBox.util;



/**
 * Spawns a thread which executes a callback at a set frequency.
 * @author Lee Coakley
 * @see Callback
 */
public class CallbackRepeater
{
	private final Thread   thread;
	private final Callback callback;
	private       long     interval;
	private       boolean  threadExecute;
	private       boolean  threadPause;
	
	
	
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
	 * Get the duration between calls, in milliseconds.
	 */
	public long getInterval() {
		synchronized (thread) {
			return interval;
		}
	}
	
	
	
	/**
	 * Change the sleep duration of the repeater.
	 */
	public void setInterval( long freqMillis ) {
		synchronized (thread) {
			this.interval = freqMillis;
		}
	}



	/**
	 * Halts callback execution and joins the thread.
	 * If the repeater is paused, it is unpaused.
	 * The calling thread waits until this operation completes.
	 * Don't call this from inside the callback!
	 */
	public void join() {
		checkCaller();
		
		synchronized (thread) {
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
			thread.notifyAll();
		}
	}
	
	
	
	/**
	 * Causes the repeater to stop without causing the calling thread to wait.
	 * If the repeater is paused, it will be unpaused.
	 * You can safely call this from inside the callback.
	 * After calling this function the repeater's lifetime has ended and it can no longer be used.
	 */
	public void softStop() {
		synchronized (thread) {
			threadExecute = false;
			unpause();
		}
	}
	
	
	
	private void checkCaller() {
		if (Thread.currentThread() == thread)
			throw new RuntimeException(
				"Attempting to call this function from the callback would result in a deadlock."
			);
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
			rep.pause();
			rep.pause();
			rep.unpause();
			rep.unpause();
			rep.pause();
			rep.pause();
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




































