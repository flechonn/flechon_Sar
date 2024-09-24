# Design Documentation

## 1. Initialization

During the initialization phase:
- Tasks and brokers are created.
- Brokers are registered with the **BrokerManager**, which manages all brokers in the system
- Each **Broker** is responsible for managing its own list of **Rendezvous (rdv)** . The broker that calls `accept()` stores the rendezvous until the connection is completed in a list.
- The **Rendezvous** mechanism handles the establishment of two full-duplex channels between brokers. These channels allow communication in both directions between two brokers.

## 2. Architecture

### Components:
- **BrokerManager**: Manages brokers and provides access to them through a static `getBroker()` method.
- **Broker**: Facilitates communication by managing its own list of **rendezvous** for establishing connections. The broker that calls `accept()` stores the **Rendezvous** and handles the connection process. Once the connection is established, the rendezvous is removed from the broker's list.
- **Rendezvous (rdv)**: Managed by the broker that calls `accept()`, the rendezvous handles the matching of two brokers (one calling `accept`, the other `connect`) and the creation of two channels.

### Process:
- A broker uses `connect()` to initiate a connection or `accept()` to wait for an incoming connection on a specified port.
- The **Broker Manager** ensures that each broker knows all the other brokers
- The broker that calls `accept()` is responsible for storing the **Rendezvous** and managing the connection process until a second broker connects. If another broker attempts to accept on an already-used port, an exception is thrown.
- Once connected, the rendezvous is removed from the broker's list, and two full-duplex channels are created, one for each direction of communication between the brokers.

## 3. Methods

### BrokerManager
- **getBroker()**: Static method to retrieve any broker without needing to pass references.

### Broker
- **connect(broker, port)**: Initiates a blocking connection request.
- **accept(port)**: Waits for and accepts a connection on a specified port. The broker calling `accept()` stores the rendezvous in its list, which is removed once the connection is established.

### Channel
- **write(byte[], offset, length)**: Writes data to the channel, blocking if no space is available.
- **read(byte[], offset, length)**: Reads data from the channel, blocking if no data is available.
- **disconnect()**: Initiates disconnection, preventing further reads or writes.

### Rendezvous
- **connect(broker)**: Establishes a channel from a broker to another broker. Manages the blocking mechanism if no matching `accept()` is found.
- **accept(broker)**: Accepts an incoming connection. Blocks until a matching `connect()` is initiated. The broker calling `accept()` stores the rendezvous in its list, which is removed once the connection is established.

## 4. Thread Safety

- Channels allow concurrent reading and writing by two separate tasks but do not support concurrent reads or writes by multiple tasks on the same endpoint.
- Brokers are shared among tasks and are thread-safe.

## 5. Disconnecting

When either broker initiates a disconnection, the following occurs:
- Each channel must be gracefully disconnected. If one channel initiates the disconnection, the other channel ensures that all remaining data has been read before completing its disconnection.
- The system ensures that any remaining data in transit is fully read before marking the channels as fully disconnected.
- If any task is currently blocked on a `read` or `write` operation, the operation is interrupted, and an **Exception** is thrown.
