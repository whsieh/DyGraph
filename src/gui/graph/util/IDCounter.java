package gui.graph.util;

public class IDCounter {
    
    final static int START_ID = 0;
    final static int INCR_ID = 1;
    
    private static IDCounter counter;
    private int value;
    private int increment;
    
    public IDCounter(int initial,int incr) {
        value = initial-incr;
        increment = incr;
    }
    public Integer nextID() {
        value += increment;
        return value;
    }
    
    static public Integer next() {
        if (counter == null) {
            counter = new IDCounter(START_ID,INCR_ID);
        }
        return IDCounter.counter.nextID();
    }
    
}