package ramsey;

public class GraphRotator {
	private Vertex[] cayleyGraphArray;
	private Config config = new Config();
	
	/**
	 * This is the publicly exposed rotate method which determines which type
	 * of rotation to use and calls the appropriate method.
	 * 
	 * @return void
	 */
	public void rotate(CayleyGraph cayleyGraph) {
		this.cayleyGraphArray = cayleyGraph.getCayleyGraphArray();
		for (int count = 0; count < config.ROTATION_COUNT; count++) {

			if (config.ROTATION_METHOD == ROTATION_TYPE.SERIAL) {
				if (config.ROTATION_DIRECTION == DIRECTION.LEFT) {
					rotateSerialLeft();
				}
				else if (config.ROTATION_DIRECTION == DIRECTION.RIGHT) {
					rotateSerialRight();	
				}
			} else if (config.ROTATION_METHOD == ROTATION_TYPE.PARALLEL) {
				rotateParallel();
			} else if (config.ROTATION_METHOD == ROTATION_TYPE.NONE) {
				return;
			}
		}
	}
			
	/**
	 * Rotate all the vertices then reassign all vertexIDs. This will be done
	 * serially, all vertices will be shifted to the right one position.
	 * 
	 * @return void
	 */
	private void rotateSerialLeft() {
		Vertex swap;

		// Shift all vertices
		swap = this.cayleyGraphArray[0];
		System.arraycopy(this.cayleyGraphArray, 1, this.cayleyGraphArray, 0, config.NUM_OF_ELEMENTS - 1);
		this.cayleyGraphArray[config.NUM_OF_ELEMENTS - 1] = swap;

		for (int i = 0; i < config.NUM_OF_ELEMENTS; i++) {
			this.cayleyGraphArray[i].rotateEdgesLeft();
		}

		// Update IDs so that this.CayleyGraph[x] still has vertexId x
		for (int i = 0; i < config.NUM_OF_ELEMENTS; i++) {
			this.cayleyGraphArray[i].updateId(i);
		}
	}
	
	/**
	 * Rotate all the vertices then reassign all vertexIDs. This will be done
	 * serially, all vertices will be shifted to the right one position.
	 * 
	 * @return void
	 */
	private void rotateSerialRight() {
		Vertex swap;

		// Shift all vertices
		swap = this.cayleyGraphArray[config.NUM_OF_ELEMENTS - 1];
		System.arraycopy(this.cayleyGraphArray, 0, this.cayleyGraphArray, 1, config.NUM_OF_ELEMENTS - 1);
		this.cayleyGraphArray[0] = swap;

		for (int i = 0; i < config.NUM_OF_ELEMENTS; i++) {
			this.cayleyGraphArray[i].rotateEdgesRight();
		}

		// Update IDs so that this.CayleyGraph[x] still has vertexId x
		for (int i = 0; i < config.NUM_OF_ELEMENTS; i++) {
			this.cayleyGraphArray[i].updateId(i);
		}
	}
	
	/**
	 * Rotate all the vertices then reassign all vertexIDs. This will be done in
	 * parallel with the number of concurrent threads being defined by the input
	 * maxThreads variable.
	 * 
	 * @param maxThreads This is the number of threads that will execute the
	 *        rotation concurrently.
	 * @return void
	 */
	private void rotateParallel() {
		Vertex swap;
		RotateEdgeThread[] threads = new RotateEdgeThread[config.ROTATION_THREAD_COUNT];

		// Shift all vertices
		swap = this.cayleyGraphArray[0];
		System.arraycopy(this.cayleyGraphArray, 1, this.cayleyGraphArray, 0, config.NUM_OF_ELEMENTS - 1);
		this.cayleyGraphArray[config.NUM_OF_ELEMENTS - 1] = swap;

		// Shift all edges for each vertex
		// Initialize Threads
		for (int j = 0; j < config.ROTATION_THREAD_COUNT; j++) {
			threads[j] = new RotateEdgeThread(this.cayleyGraphArray, j, config.ROTATION_THREAD_COUNT);
		}

		// Sync up threads
		for (int j = 0; j < config.ROTATION_THREAD_COUNT; j++) {
			try {
				threads[j].join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Update IDs so that this.CayleyGraph[x] still has vertexId x
		for (int i = 0; i < config.NUM_OF_ELEMENTS; i++) {
			this.cayleyGraphArray[i].updateId(i);
		}
	}
	
	class RotateEdgeThread extends Thread{
		int threadId;
		int maxThreads;
		Vertex[] vertexArray;
		
		RotateEdgeThread(Vertex[] vertexArray, int threadId, int maxThreads){
			this.threadId = threadId;
			this.maxThreads = maxThreads;
			this.vertexArray = vertexArray;
			start();
		}
		
		public void run() {
			for (int i = this.threadId; i < vertexArray.length; i += this.maxThreads) {
				this.vertexArray[i].rotateEdgesLeft();
			}
		}
	}
	
	
	
	
	
}
