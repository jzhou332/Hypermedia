import java.awt.Point;
public class Link implements Comparable<Link>{
    public Rect start;
    public Rect end;
    public Link(Rect a, Rect b){
        this.start = a;
        this.end = b;
    }

    @Override
    public boolean equals(Object o){
        boolean ret = false;
        if(o instanceof Link){
            if(this.start.cor1==((Link)o).start.cor1 && this.end.cor1==((Link)o).end.cor1){
                ret = true;
            }else{
                ret = false;
            }
        }
        return ret;
    }

    @Override
    public int compareTo(Link o){
        return (int)(this.start.cor1.getX() - o.start.cor1.getX());
    }

    public void printLink() {
        System.out.println("First rect is at " );
        start.printPoints();
        System.out.println("Second rect is at ");
        end.printPoints();
    }
}
