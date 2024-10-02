package flechon_sar;

import java.util.HashMap;
import java.util.Map;

public class LocalBrocker extends Broker {
    private static Map<Integer, RendezVous> HashRendezVous = new HashMap<>();
    BrockerManager Bm;
    public LocalBrocker(String name) {
        super(name);
        HashRendezVous = new HashMap<Integer, RendezVous>();
        Bm = BrockerManager.getInstance();
        Bm.addBroker(this);
    }

    @Override
    public Channel accept(int port) throws InterruptedException {
        RendezVous RDV = null;
        synchronized (HashRendezVous) {
            RDV = HashRendezVous.get(port);
            if(RDV == null){
                RDV = new RendezVous();
                HashRendezVous.put(port, RDV);
                HashRendezVous.notifyAll();
            } else {
                throw new IllegalArgumentException("Port " + port + " already accepting ...");
            }
        }


        Channel c = null;
        c = RDV.accept((Broker)this);
        HashRendezVous.remove(port);
        return c;
    }

    @Override
    public Channel connect(String host, int port) throws InterruptedException {
        LocalBrocker LB = (LocalBrocker) BrockerManager.getBroker(host);
        if(LB == null){
            return null;
        }
        return LB._connect(port);
    }

    private Channel _connect(int port) throws InterruptedException {
        RendezVous RDV = null;
        synchronized (HashRendezVous) {
            RDV = HashRendezVous.get(port);
            while(RDV == null){
                try {
                    HashRendezVous.wait();
                } catch (InterruptedException e) {
                    // nothing to do here
                }
                RDV = HashRendezVous.get(port);
            }
            HashRendezVous.remove(port);
        }

        Channel c = null;
        c = RDV.connect((Broker)this);
        return c;
    }
    
}
