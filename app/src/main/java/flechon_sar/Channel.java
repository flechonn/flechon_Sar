package flechon_sar;
/**
 * This abstract class represents a channel for reading and writing data.
 * It provides methods for reading from and writing (bidrectionnal) to the channel, as well as checking its connection status.
 * A channel is FIFO lossless, meaning that the order of the bytes written to it is preserved, and that no byte is lost.
 */
public abstract class Channel {
    /**
     * This method reads up to a specified number of bytes from the channel into the given buffer.
     * It is a blocking operation, meaning it will only return once at least one byte has been read.
     *
     * @param bytes  the buffer to read into.
     * @param offset the offset in the buffer to start reading into.
     * @param length the maximum number of bytes to read.
     * @return the number of bytes read, or -1 if the channel is disconnected.
     */
    abstract int read(byte[] bytes, int offset, int length);

    /**
     * Write up to length bytes from the given buffer to the channel.
     *
     * @param bytes  the buffer to write from.
     * @param offset the offset in the buffer to start writing from.
     * @param length the number of bytes to write.
     * @return the number of bytes written once it succeeds, and -1 if the channel is disconnected.
     */
    abstract int write(byte[] bytes, int offset, int length);

    /**
     * Disconnects the channel.
     * 
     * This method is responsible for:
     * 1. Closing all active connections associated with this channel.
     * 2. Updating the internal state to reflect that the channel is disconnected.
     * 3. Notifying any relevant brokers or systems about the disconnection.
     */
    abstract void disconnect();

    /**
     * Checks if the channel is disconnected.
     * 
     * This method should return the current state of the channel's connection.
     *
     * @return true if the channel is disconnected, false otherwise.
     */
    abstract boolean disconnected();
}