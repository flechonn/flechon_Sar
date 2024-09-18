package flechon_sar;
abstract class Broker {
    /* The name of the broker, must be unique */
    // un broquer peut avoir plusieur tache 
    public final String name;

    /**
     * a Broker is an interface that allows connecting to a port and accepting connections.
     */
    Broker(String name) {
        this.name = name;
    }

    /**
     * Accepts an incoming connection on the specified port.
     * 
     * @param port The port number on which to accept the connection.
     * @return The channel representing the accepted connection.
     */
    abstract Channel accept(int port);

    /**
     * Connects to a specified host and port.
     *
     * @param host the host to connect to
     * @param port the port to connect to
     * @return the channel representing the connection
     */
    abstract Channel connect(String host, int port);
}