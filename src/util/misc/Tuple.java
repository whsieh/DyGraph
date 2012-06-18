package util.misc;


import java.util.Iterator;


public class Tuple<T> implements Iterable<T>{
    
    T[] elements;
    int currentIndex;
    
    public Tuple(T ... args){
        elements = (T[])new Object[args.length];
        for(int i = 0; i < args.length; i++){
            elements[i] = args[i];
        }
        currentIndex = 0;
    }
    
    public T get(int index){
        return elements[index];
    }
    
    public int length(){
        return elements.length;
    }

    @Override
    public Iterator<T> iterator() {
        
        return new Iterator<T>(){
            
            @Override
            public boolean hasNext() {
                return currentIndex != length();
            }

            @Override
            public T next() {
                currentIndex++;
                return get(currentIndex - 1);
            }

            @Override
            public void remove() { }
            
        };
    }
    
    public static void main(String[] args){
        Tuple<Integer> tup = new Tuple(1,2,3,4,5);
        for(Integer t : tup){
            System.out.println(t);
        }
    }
    
}