package flechon_sar;

import java.util.HashMap;
import java.util.Map;
public class BrockerManager {
    
    private static Map<String, Broker> brokers = new HashMap<>();

    private static BrockerManager self;

    static BrockerManager getInstance(){
        if(self == null){
            self = new BrockerManager();
        }
        return self;
    }

    private BrockerManager(){
        brokers = new HashMap<String,Broker>();
    }

    public synchronized static void addBroker(Broker broker){
        String name = broker.name;
        Broker b = brokers.get(name);
        if(b != null){
            throw new IllegalArgumentException("Broker with name " + name + " already exists");
        }
        brokers.put(broker.name, broker);
    }

    public synchronized static void removeBroker(String name){
        brokers.remove(name);
    }

    public synchronized static Broker getBroker(String name){
        return brokers.get(name);
    }
}
