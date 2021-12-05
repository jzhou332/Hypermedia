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

public class Itimer_test extends JFrame {

    private JPanel contentPane;
    public static String imgsdir;
    //player counter
    public static int index = 0;
    public static ImageIcon[] img = new ImageIcon[900];//声明数组用来存放要播放的图片
    JLabel label;//声明为全局变量用来显示图片
    private javax.swing.Timer time;//声明的计数器
    private boolean istime;//用来标记自动播放 是否

    public static int isPlaying = -1;
    public static SourceDataLine line = null;

    static String videoPath = "/Users/aaronwenhaoge/Downloads/AIFilmOne/AIFilmOne";
    static String audioPath = "/Users/aaronwenhaoge/Downloads/AIFilmOne/AIFilmOne.wav";

    /**
     * Launch the application.
     */
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        playAudio(audioPath);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
//                    Itimer_test frame = new Itimer_test("/Users/congkaizhou/Desktop/AIFilmOne/AIFilmOne");
                    Itimer_test frame = new Itimer_test(videoPath);
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

     private static void playAudio(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
         AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filePath));

         AudioFormat format = stream.getFormat();
         if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
             format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(),
                     format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2,
                     format.getFrameRate(), true); // big endian
             stream = AudioSystem.getAudioInputStream(format, stream);
         }

         SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat(),
                 ((int) stream.getFrameLength() * format.getFrameSize()));
         line = (SourceDataLine) AudioSystem.getLine(info);
         line.open(stream.getFormat());
         line.stop();

         int numRead = 0;
         byte[] buf = new byte[line.getBufferSize()];
         while ((numRead = stream.read(buf, 0, buf.length)) >= 0) {
             int offset = 0;
             while (offset < numRead) {
                 offset += line.write(buf, offset, numRead - offset);
             }
         }
         line.drain();
         line.stop();
     }

    /**
     * Create the frame.
     */
    public Itimer_test(String imgdir) {
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
                        imgsdir = videoPath;
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
                     line.start();
                 } else {
                     line.stop();
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
            }
        });
        panel.add(btnnext);

        label = new JLabel(this.img[0]);
        contentPane.add(label, BorderLayout.CENTER);

    }

}