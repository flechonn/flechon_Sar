# Overview
This document specifies a message delivery system where instances can connect to a channel, send small messages, and ensure that all messages are delivered and read. 

Instances can connect and disconnect from the channel at any time, with messages being delivered when they reconnect.

## Requirements

### 1. Channel
- The system includes a **channel** that manages communication between multiple **instances**.
- The channel will ensure **message delivery** to all connected instances.
- All messages must be **delivered and read** by the intended instances.

### 2. Instances
- **Instances** can connect or disconnect from the channel at any time.
- Each instance can send **small messages** with a size limit of **255 characters**.

### 3. Connectivity
- Instances can **connect** to the channel at any time.
- Instances can **disconnect** and **reconnect** 
- The channel must manage the state (connected/disconnected) of each instance.

### 4. Message Size
- The maximum size of each message is **255 characters**.
- Messages exceeding this size will be **rejected** by the channel.

## Use Cases

### 1. Sending a Message
- An instance connects to the channel
- An instance sends a message (max size: 255 characters) to the channel.
- The channel delivers it to connected instances