package flechon_sar;
/**
 * A task is a thread that can be executed by a broker.
 */
public class Task extends Thread {
    Broker broker;
    /**
     * Constructs a Task object with the given Broker and Runnable.
     *
     * @param b The Broker object associated with the task.
     * @param r The Runnable object representing the task to be executed.
     */
    public Task(Broker b, Runnable task){
        super(task);
        this.broker = b;
    }

    /**
     * Returns the broker that belong to this task.
     *
     * @return the broker.
     */
    public static Broker getBroker(){
        Thread currentThread = Thread.currentThread();
        if(currentThread instanceof Task){
            return ((Task)currentThread).broker;
        }
        throw new UnsupportedOperationException("this is not a task");
    }
}