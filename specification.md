
# Overview: Broker / Channel

A channel is a communication channel, a point-to-point stream of bytes.
Full-duplex, each end point can be used to read or write.
A connected channel is FIFO and lossless, see Section "Disconnecting"
for details about disconnection.

The typical use of channels is by two tasks to establish a full-duplex communication. However, there is no ownership between channel and tasks, any task may read or write in any channel it has the reference to. The following rules apply:

- It is entirely thread-safe for the two tasks to read or write at   either end point of the channels concurrently. 
- Locally, at one end point, two tasks, one reading and the other writing, operating concurrently is safe also. 
- However, concurrent read operations or concurrent write operations are not safe on the same end point.  

A channel is either connected or disconnected. It is created connected and it becomes disconnected when either side requests a disconnect. There is no notion of the end of stream for a connected stream. To mark the end of a stream, the corresponding channel is simply disconnected.

# Connecting

A channel is established, in a fully connected state, when a connect 
matches an accept. When connecting, the given name is the one of the remote broker, the given port is the one of an accept on that remote broker.

There is no precedence between connect and accept, this is a symmetrical rendez-vous: the first operation waits for the second one. Both accept and connect operations are therefore blocking calls, blocking until the rendez-vous happens, both returning a fully connected and usable full-duplex channel.

When connecting, we may want to distinguish between two cases:
(i) there is no accept yet and (ii) there is not such broker. 
When the named broker does not exist, the connect returns null, 
but if the remote broker is found, the connect blocks until 
there is a matching accept otherwise so that a channel can be
constructed and returned. 

Note: we could consider introducing a timeout here, limiting the wait for the rendez-vous to happen.