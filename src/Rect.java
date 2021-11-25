import java.awt.Point;
public class Rect implements Comparable<Rect> {
    public Point cor1;
    public Point cor2;
    public Rect(Point a, Point b){
        this.cor1 = a;
        this.cor2 = b;
    }

    @Override
    public boolean equals(Object o){
        boolean ret = false;
        if(o instanceof Rect){
            if(this.cor1==((Rect)o).cor1 && this.cor2==((Rect)o).cor2){
                ret = true;
            }else{
                ret = false;
            }
        }
        return ret;
    }

    @Override
    public int compareTo(Rect o){
        return (int)(this.cor1.getX() - o.cor1.getX());
    }

    public void printPoints() {
        System.out.println("mousePressed: x=" + cor1.getX() + ", y=" + cor1.getY());
        System.out.println("mouseReleased: x=" + cor2.getX() + ", y=" + cor2.getY());
    }

}