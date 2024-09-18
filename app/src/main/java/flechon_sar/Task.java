package flechon_sar;
/**
 * A task is a thread that can be executed by a broker.
 */
abstract class Task extends Thread {
    Broker broker;
    /**
     * Constructs a Task object with the given Broker and Runnable.
     *
     * @param b The Broker object associated with the task.
     * @param r The Runnable object representing the task to be executed.
     */
    Task(Broker b, Runnable r){
        throw new UnsupportedOperationException("Should be overridden");
    }

    /**
     * Returns the broker that belong to this task.
     *
     * @return the broker.
     */
    static Broker getBroker(){
        throw new UnsupportedOperationException("Should be overridden");
    }
}