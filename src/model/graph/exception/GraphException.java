package model.graph.exception;


public class GraphException extends RuntimeException{

    final static String TYPE = "Error: incorrect element type.";
    final static String IDENTIFIER = "Error: unknown identifier flag.";
    final static String DUPLICATE = "Error: duplicate items should not exist.";
    final static String NOTFOUND = "Error: element not found: ";
    
    public static GraphException identifier() {
        return new GraphException(IDENTIFIER);}
    public static GraphException type() {
        return new GraphException(TYPE);}
    public static GraphException duplicate() {
        return new GraphException(DUPLICATE);}
    public static GraphException notFound(String str) {
        return new GraphException(NOTFOUND + str);}
    
    public GraphException (String s) {
        super(s);
    }
}
