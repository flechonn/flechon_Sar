package flechon_sar;

import java.util.HashMap;
import java.util.Map;

public class LocalBrocker extends Broker {
    private static Map<String, RendezVous> HashRendezVous = new HashMap<>();

    public LocalBrocker(String name) {
        super(name);
        BrockerManager.addBroker(this);

        this.HashRendezVous = new HashMap<>();
    }

    @Override
    public Channel accept(int port) {
        RendezVous RDV = HashRendezVous.get(String.valueOf(port));
        if(RDV == null){
            RDV = new RendezVous();
            HashRendezVous.put(String.valueOf(port), RDV);
        }

        Channel c = null;
        try {
            c = RDV.accept(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
        HashRendezVous.remove(String.valueOf(port));
        return c;
    }

    @Override
    public Channel connect(String host, int port) {
        return null;
    }
    
}
