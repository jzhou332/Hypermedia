import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.*;
import java.util.*;
import java.math.*;



public class MouseMotionEvents extends JPanel implements MouseListener, MouseMotionListener {
    Point p;
    Point r;
    AuthoringTool.Video video;
//    Map<Integer, ArrayList<Rect>> linkmapper;
    ArrayList<Rect> list;
    public MouseMotionEvents(AuthoringTool.Video video,  LayoutManager layout) {
        super(layout);
        addMouseListener(this);
        addMouseMotionListener(this);
        this.video = video;
//        this.linkmapper = linkmapper;
        // setBorder(BorderFactory.createLineBorder(Color.red));
        // setPreferredSize(new Dimension(300, 300));
    }
    public MouseMotionEvents(LayoutManager layout) {
        super(layout);
        addMouseListener(this);
        addMouseMotionListener(this);
        // setBorder(BorderFactory.createLineBorder(Color.red));
        // setPreferredSize(new Dimension(300, 300));
    }

    public void mouseClicked(MouseEvent me) {
        // p = me.getPoint();
        // repaint();
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {
        p = me.getPoint();

//        System.out.println("mousePressed: " + p.getX()+", "+p.getY());
        // repaint();
    }

    public void mouseReleased(MouseEvent me) {
        r = me.getPoint();
        int frameNum = video.getFrameNum();
        if(AuthoringTool.primaryVideoLinkmapper.get(AuthoringTool.primary_frame_num) == null){
            ArrayList<Rect> list = new ArrayList<>();
            Rect rect = new Rect(p, r);
            list.add(rect);
            rect.printPoints();
            AuthoringTool.primaryVideoLinkmapper.put(AuthoringTool.primary_frame_num, list);
        }else{
            AuthoringTool.primaryVideoLinkmapper.get(AuthoringTool.primary_frame_num).add(new Rect(p, r));
        }
        System.out.println("Frame number is "+AuthoringTool.primary_frame_num);
//        System.out.println("mouseReleased: " + r.getX()+", "+r.getY());

        repaint();
    }

    public void mouseDragged(MouseEvent me) {
        r = me.getPoint();
        repaint();
    }

    public void mouseMoved(MouseEvent me) {
    }

    public void paint(Graphics g) {


        super.paint(g);
        int frameNum = AuthoringTool.primary_frame_num;
        if(AuthoringTool.primaryVideoLinkmapper.get(frameNum) != null){
            for(int i = 0; i<AuthoringTool.primaryVideoLinkmapper.get(frameNum).size(); i++){
                Rect rectangle = AuthoringTool.primaryVideoLinkmapper.get(frameNum).get(i);
                int width = Math.abs(rectangle.cor1.x - rectangle.cor2.x);
                int height = Math.abs(rectangle.cor1.y - rectangle.cor2.y);
                if(rectangle.cor1.x<rectangle.cor2.x && rectangle.cor1.y<rectangle.cor2.y){
                    g.drawRect(rectangle.cor1.x, rectangle.cor1.y, width, height);
                }
                if(rectangle.cor1.x<rectangle.cor2.x && rectangle.cor1.y>rectangle.cor2.y){
                    g.drawRect(rectangle.cor1.x, rectangle.cor2.y, width, height);
                }
                if(rectangle.cor1.x>rectangle.cor2.x && rectangle.cor1.y<rectangle.cor2.y){
                    g.drawRect(rectangle.cor2.x, rectangle.cor1.y, width, height);
                }
                if(rectangle.cor1.x>rectangle.cor2.x && rectangle.cor1.y>rectangle.cor2.y){
                    g.drawRect(rectangle.cor2.x, rectangle.cor2.y, width, height);
                }

            }
        }



        // int width = (int)linkmapper.get(frameNum).get(2)-(int)linkmapper.get(frameNum).get(0);
        // int height = (int)linkmapper.get(frameNum).get(3)-(int)linkmapper.get(frameNum).get(1);
        // g.drawRect((int)linkmapper.get(frameNum).get(0), (int)linkmapper.get(frameNum).get(1), width, height);
    }
    public static void main(String[] args) {
        JFrame aWindow = new JFrame();
        aWindow.setBounds(600, 600, 600, 600);
        aWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout gLayout = new GridBagLayout();
        MouseMotionEvents l = new MouseMotionEvents(gLayout);
        // JPanel content = new JPanel();
        // content.add(new MouseMotionEvents());
        aWindow.add(l);

        aWindow.setVisible(true);
    }
}
