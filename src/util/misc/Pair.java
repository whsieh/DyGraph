package util.misc;

public class Pair <T1,T2>{
    
    T1 first;
    T2 second;
    
    public Pair(T1 first, T2 second){
        this.first = first;
        this.second = second;
    }
    
    public T1 getFirst(){
        return first;
    }
    
    public T2 getSecond(){
        return second;
    }
    
    public boolean isIn(Object value){
        if (first.equals(value) || second.equals(value)){
            return true;
        }
        boolean isInFirst = false, isInSecond = false;
        
        if(first instanceof Pair){
            isInFirst = ((Pair)first).isIn(value);
        }else if(second instanceof Pair){
            isInSecond = ((Pair)second).isIn(value);
        }
        return (isInFirst || isInSecond);
    }
    
    @Override
    public String toString(){
        return "(" + first.toString() + " , " + second.toString() + ")";
        
    }
}
