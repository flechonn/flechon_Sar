package flechon_sar;

public class LocalChannel extends Channel {
    private int port; // for debugging purposes
    private boolean isDisconnected = false;
    public boolean isDangeling = false;
    public CircularBuffer data = new CircularBuffer(1024);

    LocalChannel OppositeChannel;
    
    public LocalChannel(Channel oChannel, int port) {
        super();
        this.port = port;
        this.OppositeChannel = (LocalChannel) oChannel;
    }

    @Override
    synchronized int read(byte[] bytes, int offset, int length) {
        // Wait until there is some data to read
        while (this.OppositeChannel.data.empty() && !disconnected()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (disconnected()) { // Return -1 if the channel is disconnected
            return -1;
        }

        // Calculate how many bytes we can actually read
        int bytesRead = 0;
        for (int i = 0; i < length && !this.OppositeChannel.data.empty(); i++) {
            bytes[offset + i] = this.OppositeChannel.data.pull();
            bytesRead++;
        }

        // Notify any waiting threads that the buffer has space
        notifyAll();
        return bytesRead;
    }

    @Override
    synchronized int write(byte[] bytes, int offset, int length) {
        if (disconnected()) {
            return -1; // Return -1 if the channel is disconnected
        }
    
        int bytesWritten = 0;
    
        while (bytesWritten < length) {

            if(disconnected()) {
               return bytesWritten; 
            }
            // Wait while the buffer is full, but check for disconnection
            while (this.OppositeChannel.data.full()) {
                if (disconnected()) {
                    return -1; // Return -1 if the channel is disconnected
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return bytesWritten; // Return the number of bytes written so far
                }
            }
    
            // Write a single byte at a time to the buffer
            this.OppositeChannel.data.push(bytes[offset + bytesWritten]);
            bytesWritten++;
            // Notify any waiting readers that data is available
            notifyAll();
        }
    
        return bytesWritten; // Return the total number of bytes written
    }

    @Override
    void disconnect() {
        synchronized (this) {
            if (this.isDisconnected) {
                return;
            }
            this.isDisconnected = true;
            notifyAll();
        }
        synchronized (OppositeChannel) {
            OppositeChannel.notifyAll();
        }
    }

    @Override
    boolean disconnected() {
        return this.isDisconnected || this.OppositeChannel.isDisconnected;
    }
    
}
