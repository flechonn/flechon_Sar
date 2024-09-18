package flechon_sar;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {

    @Test 
    void SimpleServerTest(){		
        int clientCount = 5;
		int port = 5000;
        var broker = new TestBroker("TestBroker");
        for (int i = 0; i < clientCount; i++) {
            Channel channel = broker.connect("localhost", port);

            byte[] sendBytes = new byte[255];
            for (int j = 0; j < 255; j++) {
                sendBytes[j] = (byte) (j + 1);
            }

            int bytesWritten = channel.write(sendBytes, 0, sendBytes.length);
            assertEquals(255, bytesWritten, "Failed to write all bytes to the channel");

            byte[] receivedBytes = new byte[255];

            int bytesRead = channel.read(receivedBytes, 0, receivedBytes.length);
            assertEquals(255, bytesRead, "Failed to read all bytes from the channel");

            assertArrayEquals(sendBytes, receivedBytes, "The echoed bytes do not match the sent bytes");

            channel.disconnect();
            assertTrue(channel.disconnected(), "Channel should be disconnected");
        }
    }
}

class TestBroker extends Broker {
    public TestBroker(String name) {
        super(name);
    }

    @Override
    public Channel accept(int port) {
        return null;
    }

    @Override
    public Channel connect(String host, int port) {
        return null;
    }
}