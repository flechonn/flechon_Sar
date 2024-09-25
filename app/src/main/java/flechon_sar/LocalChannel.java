package flechon_sar;

public class LocalChannel extends Channel {
    private int position = 0;
    private boolean isDisconnected = false;
    public CircularBuffer circularBuffer = new CircularBuffer(1024);

    LocalChannel readChannel;
    
    public LocalChannel(Channel rChannel) {
        super();
        this.readChannel = (LocalChannel) rChannel;
    }

    @Override
    synchronized int read(byte[] bytes, int offset, int length) {
        // Wait until there is some data to read
        while (this.readChannel.circularBuffer.empty() && !disconnected()) {
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
        for (int i = 0; i < length && !this.readChannel.circularBuffer.empty(); i++) {
            bytes[offset + i] = this.readChannel.circularBuffer.pull();
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
            // Wait while the buffer is full, but check for disconnection
            while (circularBuffer.full()) {
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
            circularBuffer.push(bytes[offset + bytesWritten]);
            bytesWritten++;
            // Notify any waiting readers that data is available
            notifyAll();
        }
    
        return bytesWritten; // Return the total number of bytes written
    }

    @Override
    void disconnect() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disconnect'");
    }

    @Override
    boolean disconnected() {
        return this.isDisconnected;
    }
    
}
