/**
 * A resource with an associated lock that can be held by only one transaction at a time.
 */
class Resource
{
  static final int NOT_LOCKED = -1;

  /**
   * The transaction currently holding the lock to this resource
   */
  private int lockOwner;

  /**
   * Creates a new resource.
   */
  Resource()
  {
    lockOwner = NOT_LOCKED;
  }

  /**
   * Gives the lock of this resource to the requesting transaction. Blocks
   * the caller until the lock could be acquired.
   *
   * @param transactionId The ID of the transaction that wants the lock.
   * @return Whether or not the lock could be acquired.
   */
  synchronized Integer lock(int transactionId)
  {
      System.out.println(transactionId);
      System.out.println(lockOwner);
    if (lockOwner == transactionId) {
      System.err.println("Error: Transaction " + transactionId + " tried to lock a resource it already has locked!");
      return -1;
    }
    if(!Globals.PROBING_ENABLED){
        boolean haveWaited = false;
        while (lockOwner != NOT_LOCKED) {
            if (haveWaited){
                return -1;
            }
            try {
                wait(3000);

                haveWaited = true;
            } catch (InterruptedException ie) {
            }
        }
    }
    else {
        if (lockOwner != NOT_LOCKED) {
            System.out.println("lock owner");
            System.out.println(lockOwner);
            System.out.println(NOT_LOCKED);
            return 0;
        }
    }

    lockOwner = transactionId;
    return 1;
  }

  /**
   * Releases the lock of this resource.
   *
   * @param transactionId The ID of the transaction that wants to release lock.
   *                      If this transaction doesn't currently own the lock an
   *                      error message is displayed.
   * @return Whether or not the lock could be released.
   */
  synchronized boolean unlock(int transactionId)
  {
    if (lockOwner == NOT_LOCKED || lockOwner != transactionId) {
      System.err.println("Error: Transaction " + transactionId + " tried to unlock a resource without owning the lock!");
      return false;
    }

    lockOwner = NOT_LOCKED;
    // Notify a waiting thread that it can acquire the lock
      System.out.println("Im notifying my ass off!");
    notifyAll();
    return true;
  }

  /**
   * Gets the current owner of this resource's lock.
   *
   * @return An Integer containing the ID of the transaction currently
   * holding the lock, or NOT_LOCKED if the resource is unlocked.
   */
  synchronized int getLockOwner()
  {
    return lockOwner;
  }

  /**
   * Unconditionally releases the lock of this resource.
   */
  synchronized void forceUnlock()
  {
    lockOwner = NOT_LOCKED;
    // Notify a waiting thread that it can acquire the lock
    notifyAll();
  }

  /**
   * Checks if this resource's lock is held by a transaction running on the specified server.
   *
   * @param serverId The ID of the server.
   * @return Whether or not the current lock owner is running on that server.
   */
  synchronized boolean isLockedByServer(int serverId)
  {
    return lockOwner != NOT_LOCKED && ServerImpl.getTransactionOwner(lockOwner) == serverId;
  }

    public synchronized void doWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
