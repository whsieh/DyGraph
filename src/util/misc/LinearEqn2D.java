package util.misc;

public class LinearEqn2D {
    
    final static float DEFAULT_MARGIN = 8.0f;
    
    float m;
    float b;
    float[] x;
    float[] y;
    float margin;
    
    public static Vector2D toUnitVector(LinearEqn2D eqn, char type) {
        return Vector2D.toUnitVector(new Vector2D(eqn.x[1] - eqn.x[0],
                eqn.y[1] - eqn.y[0],Vector2D.CARTESIAN,type));
    }
    
    public LinearEqn2D(float m, float b){
        this.m = m;
        this.b = b;
        this.x = new float[] {(float)Double.NEGATIVE_INFINITY,(float)Double.POSITIVE_INFINITY};
        this.y = new float[] {(float)Double.NEGATIVE_INFINITY,(float)Double.POSITIVE_INFINITY};
        this.margin = 7.5f;
    }
    
    public LinearEqn2D(float[] xBounds, float[] yBounds){
        this.m = (yBounds[1] - yBounds[0]) / (xBounds[1] - xBounds[0]);
        this.b = ((yBounds[0] - this.m * xBounds[0]) + (yBounds[1] - this.m * xBounds[1])) / 2.0f;
        this.x = xBounds;
        this.y = yBounds;
        this.margin = DEFAULT_MARGIN;
    }
    
    public LinearEqn2D(float x0, float y0, float x1, float y1){
        
        //System.out.println("Initializing new equation from point (" + x0 + "," + y0 + ") to point (" + x1 + "," + y1 + ")");
        this.m = (y1 - y0) / (x1 - x0);
        this.b = y0 - this.m * x0;
        this.x = new float[] {x0, x1};
        this.y = new float[] {y0, y1};
        this.margin = 5.0f;
    }
    
    public float calcY(float x, boolean checkBounds){
        if(checkBounds){
            if(this.x[0] < x && x < this.x[1])
                return (m*x) + b;
            return (float)Double.NaN;
        }else{
            return (m*x) + b;
        }
    }
    
    public float calcX(float y, boolean checkBounds){
        if (checkBounds) {
            if (this.y[0] < y && y < this.y[1]) {
                return (y - b) / m;
            }
            return (float)Double.NaN;
        } else {
            return (y - b) / m;
        }
    }
    
    public void updateBounds(float[] xBounds,float[] yBounds){
        this.x = xBounds;
        this.y = yBounds;
        this.m = 1.0f * (yBounds[1] - yBounds[0]) / (xBounds[1] - xBounds[0]);
        this.b = ((yBounds[0] - this.m * xBounds[0]) + (yBounds[1] - this.m * xBounds[1])) / 2.0f;
    }
    
    public void updateBounds(float x0, float y0, float x1, float y1){
        this.m = 1.0f * (y1 - y0) / (x1 - x0);
        this.b = (y0 - this.m * x0);
        this.x = new float[] {x0, x1};
        this.y = new float[] {y0, y1};
    }

    public boolean contains(float x,float y){
        if (m < 0.05 && m > -0.05) {
            if ((this.x[0] <= x && x <= this.x[1]) || (this.x[0] >= x && x >= this.x[1])) {
                float _y_ = y - ((this.y[0] + this.y[1]) / 2);
                return -margin < _y_ && _y_ < margin;
            }
        } else if (m > 15 || m < -15){
            if ((this.y[0] <= y && y <= this.y[1]) || (this.y[0] >= y && y >= this.y[1])) {
                float _x_ = x - ((this.x[0] + this.x[1]) / 2);
                return -margin < _x_ && _x_ < margin;
            }
        }else {
            //System.out.println("Does " + x + " lie between " + xBounds[0] + " and " + xBounds[1] + "?");
            if((this.x[0] <= x && x <= this.x[1]) || (this.x[0] >= x && x >= this.x[1])){
                //System.out.println("Does " + y + " lie between " + yBounds[0] + " and " + yBounds[1] + "?");
                if((this.y[0] <= y && y <= this.y[1]) || (this.y[0] >= y && y >= this.y[1])){
                    float calcY = calcY(x,false);
                    //System.out.println("Calculated value of y: " + calcY);
                    return ((y - margin) < calcY && calcY < (y + margin));
                }
            }
            /*
            if((this.x[0] <= x && x <= this.x[1]) || (this.x[0] >= x && x >= this.x[1])){
                //System.out.println("Does " + y + " lie between " + yBounds[0] + " and " + yBounds[1] + "?");
                if((this.y[0] <= y && y <= this.y[1]) || (this.y[0] >= y && y >= this.y[1])){
                    float area = (this.x[0]*(this.y[1]-y)) - (this.y[0]*(this.x[1]-x)) + (this.x[1]*y) - (this.y[1]*x);
                    return  -7 < area && area < 7; 
                }
            }
            */

        }
        return false;
    }
    
    @Override
    public String toString(){
        return "y = " + this.m + "x + " + this.b;
    }
    
    
    public static void main(String[] args){
        LinearEqn2D l = new LinearEqn2D(400,144,20,31);
        System.out.println(l.contains(210,87));
    }
    
}
