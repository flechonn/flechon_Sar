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
     * When reading, the given byte array will contain the bytes read,
     * starting at the given offset. The given length provides the maximum number of bytes to read. 
     * The range [offset,offset+length[ must be within the array boundaries
     * 
     * The end of stream is the same as being as the channel being disconnected, so the method will throw an exception (DisconnectedException). 
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
     * When writing, the given byte array contains the bytes to write 
     * from the given offset and for the given length.
     * The range [offset,offset+length[ must be within the array boundaries
     * 
     * @param bytes  the buffer to write from.
     * @param offset the offset in the buffer to start writing from.
     * @param length the number of bytes to write.
     * @return the number of bytes written once it succeeds, and -1 if the channel is disconnected.
     * may not be zero or negative. If zero would be return, the write operation blocks instead until it can make some progress.
     */
    abstract int write(byte[] bytes, int offset, int length);

    /**
     * Disconnects the channel.
     * 
     * A channel can be disconnected at any time, from either side. So this requires an asynchronous protocol to disconnect a channel. 
     * 
     * This method is responsible for:
     * 1. Closing all active connections associated with this channel.
     * 2. Updating the internal state to reflect that the channel is disconnected.
     * 3. Notifying any relevant brokers or systems about the disconnection.
     */

    /*
    A channel can be disconnected at any time, from either side. So this requires an asynchronous protocol to disconnect a channel. 

    The effect of disconnecting a channel must be specified for both ends, the one that called the method "disconnect" as well as the other end. In the following, we will talk about the local side versus remote side, the local side being the end where the method "disconnect" has been called.

    Note: of course, both ends may call the method "disconnect" concurrently and the protocol to disconnect the channel must still work.

    Note: since we have not asserted a strict ownership model between tasks and channels, it is possible that a channel be disconnected
    while some operations are pending locally. These operations must be interrupted, when appropriate, throwing a disconnected exception.

    The local rule is simple, once the method "disconnect" has been called on a channel, it is illegal to invoke the methods "read" or "write". Only the method "disconnected" may be called to check the status of the channel. In other words, if the method "disconnected" returns true, the methods "read" and "write" must not be invoked. If they are invoked nevertheless, the invocation will result in an disconnected
    exception being thrown.

    The remote rule is more complex to grasp, that is, when the remote side disconnects a channel, how should that be perceived locally?

    The main issue is that there may be still bytes in transit, bytes that the local side must be able to reads. By in transit, we mean bytes that were written by that remote side, before it disconnected the channel, and these bytes have not been read on a local side. 
    Therefore, if we want the local side to be able to read these last bytes, the local side should not be considered disconnected until all these bytes have been read or the channel is locally disconnected.

    This means that the local side will only become disconnected when the remote has been disconnected and there are no more in-transit bytes to read. This means that a local channel appears as not yet disconnected although its far side has already been disconnected. This means that we need to specify how should local write operations behave in 
    this half-disconnected state. The simplest is to drop the bytes silently, as if they were written, preserving the local illusion that the channel is still connected. 

    This behavior may seem counter-intuitive at first, but it is the only one that is consistent and it is in fact the easiest one on developers. First, allowing to read the last bytes in transit is mandatory since it is likely that a communication will end by writing some bytes and then disconnecting. Something like saying "bye" and 
    then hanging up.

    Second, dropping written bytes may seem wrong but it is just leveraging an unavoidable truth: written bytes may be dropped even though channels are FIFO and lossless. Indeed, it is not at all different than if the bytes were written before the other side disconnected a channel without reading all pending bytes. In both cases, the bytes would be dropped.

    Nota Bene: one should resist the temptation to adopt an immediate synchronous disconnection. Indeed, it would not be possible if our channels would not be implemented over shared memory. Disconnecting would imply sending a control message to inform the other side and thus the disconnect protocol would be asynchronous.
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