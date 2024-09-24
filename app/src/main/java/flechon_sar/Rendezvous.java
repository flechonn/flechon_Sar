package flechon_sar;

public class Rendezvous {
    private Broker connectedBroker = null;
    private Broker acceptingBroker = null;
    private boolean isChannelEstablished = false;

    public synchronized Channel connect(Broker broker) throws InterruptedException {
        if (isChannelEstablished) {
            throw new IllegalStateException("A channel is already established, unable to connect.");
        }
        return null;
    }

    public synchronized Channel accept(Broker broker) throws InterruptedException {
        if (isChannelEstablished) {
            throw new IllegalStateException("Un canal est déjà établi, impossible d'accepter.");
        }
        return null;
    }
}
