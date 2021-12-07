import java.awt.Point;

public class Rect implements Comparable<Rect>, java.io.Serializable {
    public Point cor1;
    public Point cor2;

    public int primaryFrameNum;

    private String secondaryVideoName;
    private int secondaryFrameNum;

    public boolean isOriginalFrame;

    public Rect(Point a, Point b){
        this.cor1 = a;
        this.cor2 = b;
    }
    public void setPrimaryFrameNum(int n){
        this.primaryFrameNum = n;
    }
    public void setSecondaryVideoName(String s){
        this.secondaryVideoName = s;
    }
    public String getSecondaryVideoName(){
        return secondaryVideoName;
    }
    public void setSecondaryFrameNum(int n){
        this.secondaryFrameNum = n;
    }
    public int getSecondaryFrameNum(){
        return secondaryFrameNum;
    }

    public boolean isInside(Point pt){
        if((pt.getX() >= Math.min(cor1.getX(), cor2.getX()) || pt.getX() <= Math.max(cor1.getX(), cor2.getX()))
                && (pt.getY() >= Math.min(cor1.getY(), cor2.getY()) || pt.getY() <= Math.max(cor1.getY(), cor2.getY()))){
            return true;
        }
        return false;
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