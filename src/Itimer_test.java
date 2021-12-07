import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import javax.sound.sampled.*;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.MouseAdapter;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Itimer_test extends JFrame {

    private JPanel contentPane;
    public static String imgsdir;
    //player counter
    public static int index = 0;
    public static ImageIcon[] img = new ImageIcon[900];//声明数组用来存放要播放的图片
    JLabel label;//声明为全局变量用来显示图片
    private javax.swing.Timer time;//声明的计数器
    private boolean istime;//用来标记自动播放 是否

    public static Map<Integer, ArrayList<Rect>> primaryVideoLinkmapper;
    public static Map<JTextField, int[]> linkstoragemap;

    public static SourceDataLine line = null;
    public static SimpleAudioPlayer audioPlayer;
    public static int isPlaying = -1;

    static String parentPath = "/Users/aaronwenhaoge/Downloads";
    static String videoName = "/AIFilmOne";
    static String videoPath = "/Users/aaronwenhaoge/Downloads/AIFilmOne/AIFilmOne";
    static String audioPath = new String();

    /**
     * Launch the application.
     */
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        audioPath = parentPath + videoName + videoName + ".wav";
        System.out.println(audioPath);

        readFile();

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
//                    Itimer_test frame = new Itimer_test("/Users/congkaizhou/Desktop/AIFilmOne/AIFilmOne");
//                    Itimer_test frame = new Itimer_test(args[0]);
                    Itimer_test frame = new Itimer_test(parentPath + videoName + videoName);
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        audioPlayer = new SimpleAudioPlayer();
        audioPlayer.play();
    }

    private static void readFile() {
        //read from file
        try {
            File toRead = new File(parentPath + videoName +"/primaryVideoLinkmapper.ser");
            FileInputStream fis=new FileInputStream(toRead);
            ObjectInputStream ois=new ObjectInputStream(fis);

            primaryVideoLinkmapper = (Map<Integer, ArrayList<Rect>>) ois.readObject();

            ois.close();
            fis.close();
            //print All data in MAP
            for(Map.Entry<Integer, ArrayList<Rect>> e : primaryVideoLinkmapper.entrySet()){
                System.out.println(e.getKey() + " : ");
                for (Rect rect : e.getValue()) {
                    rect.printPoints();
                }
            }
        } catch(Exception e) {

        }

        try {
            File toRead = new File(parentPath + videoName +"/linkstoragemap.ser");
            FileInputStream fis=new FileInputStream(toRead);
            ObjectInputStream ois=new ObjectInputStream(fis);

            linkstoragemap = (Map<JTextField, int[]>) ois.readObject();

            ois.close();
            fis.close();
            //print All data in MAP
//            for(Map.Entry<JTextField, int[]> e : linkstoragemap.entrySet()){
//
//                for (int i : e.getValue()) {
//                    System.out.print(i + " ");
//                }
//                System.out.println();
//            }
        } catch(Exception e) {

        }
    }

    /**
     * Create the frame.
     */
    public Itimer_test(String imgdir) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.imgsdir = imgdir;
        this.istime=true;
        // String path = imgsdir;

        addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me)
                {
                    //minus the border width 5
                    int xx = me.getX() - 5;
                    int yy = me.getY() - 30 - 24;
                    System.out.println("Mouse at "+xx+", " + yy + " " + "at frame " + (index+1));

                    if(true){
//                        imgsdir = "/Users/congkaizhou/Desktop/AIFilmTwo/AIFilmTwo";
//                        imgsdir = videoPath;
                        index = 0;
                        for(int i=0;i<900;i++){
        
                            String OrgNumString = null;
                            if ((i+1) >= 1 && (i+1) < 10) {
                                OrgNumString = "000" + String.valueOf((i+1));
                            } else if ((i+1) >= 10 && (i+1) < 100) {
                                OrgNumString = "00" + String.valueOf((i+1));
                            } else if ((i+1) >= 100 && (i+1) < 1000) {
                                OrgNumString = "0" + String.valueOf((i+1));
                            } else if ((i+1) >= 1000 && (i+1) < 10000) {
                                OrgNumString = String.valueOf((i+1));
                            }
                            String orgpath = imgsdir + OrgNumString + ".rgb";
                            BufferedImage org = new BufferedImage(352, 288, BufferedImage.TYPE_INT_RGB);
//                            ImageDisplay.readImageRGB(352, 288, orgpath, org);
                            AuthoringTool.readImageRGB(352, 288, orgpath, org);
                            img[i] = new ImageIcon(org);
      
                        }
                    }
                    
                    
                    
                    repaint();
                }
            });
        for(int i=0;i<900;i++){
        
            String OrgNumString = null;
            if ((i+1) >= 1 && (i+1) < 10) {
                OrgNumString = "000" + String.valueOf((i+1));
            } else if ((i+1) >= 10 && (i+1) < 100) {
                OrgNumString = "00" + String.valueOf((i+1));
            } else if ((i+1) >= 100 && (i+1) < 1000) {
                OrgNumString = "0" + String.valueOf((i+1));
            } else if ((i+1) >= 1000 && (i+1) < 10000) {
                OrgNumString = String.valueOf((i+1));
            }
            String orgpath = imgsdir + OrgNumString + ".rgb";
            BufferedImage org = new BufferedImage(352, 288, BufferedImage.TYPE_INT_RGB);
//            ImageDisplay.readImageRGB(352, 288, orgpath, org);
            AuthoringTool.readImageRGB(352, 288, orgpath, org);
            this.img[i] = new ImageIcon(org);
      
      }
        
        // this.img = new ImageIcon[] { new ImageIcon("images/addStu.png"),
        //         new ImageIcon("images/back.png"),
        //         new ImageIcon("images/delstu.png"),
        //         new ImageIcon("images/option.png"),
        //         new ImageIcon("images/png_1.png") };
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(300, 300, 352, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        //计时器的声明
        this.time = new javax.swing.Timer(33, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (index == img.length - 1) {
                    index = 0;
                } else {
                    index++;
                }
                label.setIcon(img[index]);
            }
        });

        //上一张播放的按钮事件
        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.SOUTH);

        

        //自动播放的按钮事件
        JButton btngo = new JButton("Play/Stop");
        btngo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(istime){
                    time.start();
                    istime=false;
                }else{
                    time.stop();
                    istime=true;
                }

                isPlaying *= -1;
                if (isPlaying == 1) {
                    try {
                        audioPlayer.resumeAudio();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                    }
                } else {
                    audioPlayer.pause();
                }


            }
        });
        panel.add(btngo);

        //下一张图片的按钮事件
        JButton btnnext = new JButton("Start Over");
        btnnext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // index++;
                // label = new JLabel(this.img[index]);
                index = 0;
                time.stop();
                istime=true;
                label.setIcon(img[index]);// 为label设置图片的额时需要做的是使用set.. 方法

                try {
                    isPlaying = -1;
                    audioPlayer.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.add(btnnext);

        label = new JLabel(this.img[0]);
        contentPane.add(label, BorderLayout.CENTER);

    }

}