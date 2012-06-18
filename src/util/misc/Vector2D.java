package util.misc;

public class Vector2D {
    
    static public Vector2D calcNet(Vector2D ... vectors) {
        float netX = 0,netY = 0;
        char type = vectors[0].getType();
        for (Vector2D vector : vectors) {
            netX += vector.x;
            netY += vector.y;
            assert(type == vector.type):"Error: mismatched vectors.";
        }
        return new Vector2D(netX,netY,CARTESIAN,type);
    }
    
    static public Vector2D toUnitVector(Vector2D vec) {
        float magn = vec.calcMagnitude();
        Vector2D v = new Vector2D(vec.x / magn, vec.y / magn, CARTESIAN, vec.type);
        return v;
    }
    
    public final static int CARTESIAN = 0;
    public final static int POLAR = 1;
    
    public final static char FORCE = 'F';
    public final static char VELOCITY = 'v';
    public final static char POSITION = 'r';
    public final static char ACCELERATION = 'a';
    public final static char MOMENTUM = 'p';
    public final static char DEFAULT = POSITION;
    
    float x;
    float y;
    char type;
    
    public Vector2D(float arg1, float arg2, int form, char type) {
        update(arg1,arg2,form);
        this.type = type;
    }
    
    public float x() {
        return x;
    }
    public float y() {
        return y;
    }
    
    public boolean isValid() {
        return !(Double.isNaN(x) || Double.isNaN(y));
    }
    
    final public void update(float arg1, float arg2, int form) {
        if (form == CARTESIAN) {
            this.x = arg1;
            this.y = arg2;
        } else if (form == POLAR) {
            this.x = arg1 * (float)Math.cos(arg2);
            this.y = arg1 * (float)Math.sin(arg2);
        }
    }
    
    public void setType(char type) {
        this.type = type;
    }
    
    public Vector2D newVector(float scale,char type) {
        return new Vector2D(x*scale, y*scale, CARTESIAN, type);
    }
    
    public boolean isZero() {
        return x == 0 && y == 0;
    }
    
    public void setZero() {
        x = 0;
        y = 0;
    }
    
    public void add(Vector2D vec) {
        x += vec.x;
        y += vec.y;
    }
    
    public Vector2D addTo(Vector2D vec) {
        return new Vector2D(x + vec.x,y + vec.y,CARTESIAN,type);
    }
    
    public boolean isZero(float error) {
        return (-error < x && x < error) &&
                (-error < y && y < error);
    }
    
    public void scale(float c) {
        x *= c;
        y *= c;
    }
    
    public void decrease(float amount) {
        if (x < 0) {
            x += amount;
            if (x > 0) {
                x = 0;
            }
        } else if (x > 0) {
            x -= amount;
            if (x < 0) {
                x = 0;
            }
        }
        if (y < 0) {
            y += amount;
            if (y > 0) {
                y = 0;
            }
        } else if (y > 0) {
            y -= amount;
            if (y < 0) {
                y = 0;
            }
        }
    }
    
    public Vector2D scaleTo(float c) {
        return new Vector2D(x*c,y*c,CARTESIAN,type);
    }
    
    public float calcMagnitude() {
        return (float)(Math.pow(Math.pow(x,2) + Math.pow(y,2),0.5));
    }
    
    public char getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return type + "<" + x + "," + y + ">";
    }
    
}
