import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.Point;

import static java.awt.Component.LEFT_ALIGNMENT;

public class AuthoringTool {
    public static int primary_frame_num = 0;
    public static int secondary_frame_num = 0;
    final int width = 352;
    final int height = 288;
    final String rgbFileExtension = new String(".rgb");
    Video primaryVideo = new Video();
    Video secondaryVideo = new Video();
    public static Map<Integer, ArrayList<Rect>> primaryVideoLinkmapper = new HashMap<>();
    public static Map<Integer, ArrayList<Rect>> secondaryVideoLinkmapper = new HashMap<>();
    public static Map<JTextField, int[]> linkstoragemap = new HashMap<>();
    //this hashmap stores how many links exist on each frame_num, this is used to track the ordering of adding link to a specific frame
//    public static Map<Integer, Integer> frame_rectnum = new HashMap<>();

    public static int cur_fram_num = 0;
    public static int link_order_num = 0;
    //get the primary file path
    public static String primary_file_path = "";

    //track how many rects we have created
    public static ArrayList<Rect> rectList = new ArrayList<>();
    public static ArrayList<Link> linkList = new ArrayList<>();
    // from csci576 hw1 start code
    private static void readImageRGB(int width, int height, String imgPath, BufferedImage img) {
        try
        {
            int frameLength = width*height*3;

            File file = new File(imgPath);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(0);

            long len = frameLength;
            byte[] bytes = new byte[(int) len];

            raf.read(bytes);

            int ind = 0;
            for(int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    byte a = 0;
                    byte r = bytes[ind];
                    byte g = bytes[ind+height*width];
                    byte b = bytes[ind+height*width*2];

                    int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                    //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                    img.setRGB(x,y,pix);
                    ind++;
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static double rgbToY(int pixel){
        double r1 = (pixel>>16)&0xff;
        double g1 = (pixel>>8)&0xff;
        double b1 = pixel & 0xff;
        double y = 0.299 * r1 + 0.587 * g1 + 0.114 * b1;

        return y;
    }
    //this ethod will do modifications to "primaryVideoLinkmapper"
    //frameNum是调用这个方法的当前的frameNumber
    public static void predictMiddleRect(Link a){
        Rect start = a.start;
        Rect end = a.end;
        int startFrame = start.primaryFrameNum;
        int endFrame = end.primaryFrameNum;

        int upperleftX = 0;
        int upperleftY = 0;
        int width1 = Math.abs(start.cor1.x - start.cor2.x);
        int height1 = Math.abs(start.cor1.y - start.cor2.y);
        if(start.cor1.x<start.cor2.x && start.cor1.y<start.cor2.y){
            upperleftX = start.cor1.x ;
            upperleftY = start.cor1.y ;
        }
        if(start.cor1.x<start.cor2.x && start.cor1.y>start.cor2.y){
            upperleftX = start.cor1.x ;
            upperleftY = start.cor2.y ;
//            g.drawRect(rectangle.cor1.x, rectangle.cor2.y, width, height);
        }
        if(start.cor1.x>start.cor2.x && start.cor1.y<start.cor2.y){
            upperleftX = start.cor2.x ;
            upperleftY = start.cor1.y ;
//            g.drawRect(rectangle.cor2.x, rectangle.cor1.y, width, height);
        }
        if(start.cor1.x>start.cor2.x && start.cor1.y>start.cor2.y){
            upperleftX = start.cor2.x ;
            upperleftY = start.cor2.y ;
        }

        int upperleftX2 = 0;
        int upperleftY2 = 0;
        if(end.cor1.x<end.cor2.x && end.cor1.y<end.cor2.y){
            upperleftX2 = end.cor1.x ;
            upperleftY2 = end.cor1.y ;
        }
        if(end.cor1.x<end.cor2.x && end.cor1.y>end.cor2.y){
            upperleftX2 = end.cor1.x ;
            upperleftY2 = end.cor2.y ;
//            g.drawRect(rectangle.cor1.x, rectangle.cor2.y, width, height);
        }
        if(end.cor1.x>end.cor2.x && end.cor1.y<end.cor2.y){
            upperleftX2 = end.cor2.x ;
            upperleftY2 = end.cor1.y ;
//            g.drawRect(rectangle.cor2.x, rectangle.cor1.y, width, height);
        }
        if(end.cor1.x>end.cor2.x && end.cor1.y>end.cor2.y){
            upperleftX2 = end.cor2.x ;
            upperleftY2 = end.cor2.y ;
        }
        int dx = (upperleftX2 - upperleftX)/(endFrame - startFrame);
        int dy = (upperleftY2 - upperleftY)/(endFrame - startFrame);

        for(int i = startFrame+1; i<endFrame; i++){
            BufferedImage org = new BufferedImage(352, 288, BufferedImage.TYPE_INT_RGB);
            String OrgNumString = null;
            if (i >= 1 && i < 10) {
                OrgNumString = "000" + String.valueOf(i);
            } else if (i >= 10 && i < 100) {
                OrgNumString = "00" + String.valueOf(i);
            } else if (i >= 100 && i < 1000) {
                OrgNumString = "0" + String.valueOf(i);
            } else if (i >= 1000 && i < 10000) {
                OrgNumString = String.valueOf(i);
            }
            String orgpath = primary_file_path + OrgNumString + ".rgb";
            readImageRGB(352, 288, orgpath, org);
            if(primaryVideoLinkmapper.get(i) == null){
                upperleftX = upperleftX + dx;
                upperleftY = upperleftY + dy;
                ArrayList<Rect> list = new ArrayList<>();
                Point s  = new Point(upperleftX, upperleftY);
                Point e  = new Point(upperleftX+width1, upperleftY+height1);
                Rect add = new Rect(s, e);
                list.add(add);
                primaryVideoLinkmapper.put(i, list);
            }else{
                upperleftX = upperleftX + dx;
                upperleftY = upperleftY + dy;
                ArrayList<Rect> list = new ArrayList<>();
                Point s  = new Point(upperleftX, upperleftY);
                Point e  = new Point(upperleftX+width1, upperleftY+height1);
                Rect add = new Rect(s, e);
                primaryVideoLinkmapper.get(i).add(add);
            }
        }


    }
    public static void predictfutureRect(int framenumber, Rect rectangle){
        int frameNum = framenumber;
        //判断当前长方形的width, height, 和左上角的顶点
        int upperleftX = 0;
        int upperleftY = 0;
        int width1 = Math.abs(rectangle.cor1.x - rectangle.cor2.x);
        int height1 = Math.abs(rectangle.cor1.y - rectangle.cor2.y);
        if(rectangle.cor1.x<rectangle.cor2.x && rectangle.cor1.y<rectangle.cor2.y){
            upperleftX = rectangle.cor1.x ;
            upperleftY = rectangle.cor1.y ;
        }
        if(rectangle.cor1.x<rectangle.cor2.x && rectangle.cor1.y>rectangle.cor2.y){
            upperleftX = rectangle.cor1.x ;
            upperleftY = rectangle.cor2.y ;
//            g.drawRect(rectangle.cor1.x, rectangle.cor2.y, width, height);
        }
        if(rectangle.cor1.x>rectangle.cor2.x && rectangle.cor1.y<rectangle.cor2.y){
            upperleftX = rectangle.cor2.x ;
            upperleftY = rectangle.cor1.y ;
//            g.drawRect(rectangle.cor2.x, rectangle.cor1.y, width, height);
        }
        if(rectangle.cor1.x>rectangle.cor2.x && rectangle.cor1.y>rectangle.cor2.y){
            upperleftX = rectangle.cor2.x ;
            upperleftY = rectangle.cor2.y ;
//            g.drawRect(rectangle.cor2.x, rectangle.cor2.y, width, height);
        }
        //this is the original picture where we draw a rect
//        BufferedImage org = new BufferedImage(352, 288, BufferedImage.TYPE_INT_RGB);
//        String OrgNumString = null;
//        if (frameNum >= 1 && frameNum < 10) {
//            OrgNumString = "000" + String.valueOf(frameNum);
//        } else if (frameNum >= 10 && frameNum < 100) {
//            OrgNumString = "00" + String.valueOf(frameNum);
//        } else if (frameNum >= 100 && frameNum < 1000) {
//            OrgNumString = "0" + String.valueOf(frameNum);
//        } else if (frameNum >= 1000 && frameNum < 10000) {
//            OrgNumString = String.valueOf(frameNum);
//        }
//        String orgpath = primary_file_path + OrgNumString + ".rgb";
//        readImageRGB(352, 288, orgpath, org);
//        BufferedImage[] imageSet = new BufferedImage[60];

        for(int i = 0; i<120; i++){
            BufferedImage org = new BufferedImage(352, 288, BufferedImage.TYPE_INT_RGB);
            String OrgNumString = null;
            if (frameNum >= 1 && frameNum < 10) {
                OrgNumString = "000" + String.valueOf(frameNum);
            } else if (frameNum >= 10 && frameNum < 100) {
                OrgNumString = "00" + String.valueOf(frameNum);
            } else if (frameNum >= 100 && frameNum < 1000) {
                OrgNumString = "0" + String.valueOf(frameNum);
            } else if (frameNum >= 1000 && frameNum < 10000) {
                OrgNumString = String.valueOf(frameNum);
            }
            String orgpath = primary_file_path + OrgNumString + ".rgb";
            readImageRGB(352, 288, orgpath, org);
            frameNum = frameNum + 1;
            BufferedImage bufferImg = new BufferedImage(352, 288, BufferedImage.TYPE_INT_RGB);
            String frameNumString = null;
            if (frameNum >= 1 && frameNum < 10) {
                frameNumString = "000" + String.valueOf(frameNum);
            } else if (frameNum >= 10 && frameNum < 100) {
                frameNumString = "00" + String.valueOf(frameNum);
            } else if (frameNum >= 100 && frameNum < 1000) {
                frameNumString = "0" + String.valueOf(frameNum);
            } else if (frameNum >= 1000 && frameNum < 10000) {
                frameNumString = String.valueOf(frameNum);
            }
            String path = primary_file_path + frameNumString + ".rgb";
            readImageRGB(352, 288, path, bufferImg);
//            imageSet[i] = bufferImg;

            int startX = upperleftX - 5;
            if(startX<=0){
                startX = 0;
            }
            int startY = upperleftY - 5;
            if(startY<=0){
                startY = 0;
            }

            int endy = startY+10;
            int endx = startX+10;
            if(endy>=288){
                endy = 288;
            }
            if(endx>=352){
                endx = 352;
            }
            boolean isBreak = false;
            int[] coord = new int[2];
            double min = Integer.MAX_VALUE;
            for(int y = startY; y<endy; y++){
                for(int x = startX; x<endx; x++){
                    if(getdifferernce(org, bufferImg, upperleftX, upperleftY, x, y, height1, width1)<min){
                        min = getdifferernce(org, bufferImg, upperleftX, upperleftY, x, y, height1, width1);
                        coord[0] = x;
                        coord[1] = y;
                    }
                    //if the return value is smaller than a specified value, then create a rect and store this rect
                }

            }
            if(min<100000){
                if(primaryVideoLinkmapper.get(frameNum) == null){
                    upperleftX = coord[0];
                    upperleftY = coord[1];
                    ArrayList<Rect> list = new ArrayList<>();
                    Point a  = new Point(coord[0], coord[1]);
                    Point b  = new Point(coord[0]+width1, coord[1]+height1);
                    Rect add = new Rect(a, b);
                    list.add(add);
                    primaryVideoLinkmapper.put(frameNum, list);
                }else{
                    upperleftX = coord[0];
                    upperleftY = coord[1];
                    Point a  = new Point(coord[0], coord[1]);
                    Point b  = new Point(coord[0]+width1, coord[1]+height1);
                    Rect add = new Rect(a, b);
                    primaryVideoLinkmapper.get(frameNum).add(add);
                }

            }else{
                break;
            }
        }//end for
    }

    //org代表初始添加长方形的frame， ref是后续对比的frame，
    public static double getdifferernce(BufferedImage org, BufferedImage ref, int uppleftX, int upprightY, int searchX, int searchY, int pheight, int pwidth){
        double ret1 = 0;
        for(int y = 0; y<pheight; y++){
            for(int x = 0; x<pwidth; x++){
                double f1 = rgbToY(org.getRGB(uppleftX+x, upprightY+y));
                double f2 = rgbToY(ref.getRGB(searchX+x, searchY+y));
                double diff = Math.abs(f1 - f2);
                ret1 += diff;
            }
        }

//        int ret2 = 0;
//        for(int y = 0; y<pheight; y++){
//            for(int x = 0; x<pwidth; x++){
//                int f1 = org.getRGB(uppleftX+x, upprightY+y);
//                int f2 = ref.getRGB(searchX+x, searchY+y);
//                int diff = Math.abs(f1 - f2);
//                ret2 += diff;
//            }
//        }
        return ret1;
    }

    private void showImg(BufferedImage img, JLabel lbIm, JPanel panel, JSlider slider, Video video) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();

        String videoName = video.getVideoName();
        String videoPath = video.getVideoPath();
        int frameNum = video.getFrameNum();

        String frameNumString = null;
        if (frameNum >= 1 && frameNum < 10) {
            frameNumString = "000" + String.valueOf(frameNum);
        } else if (frameNum >= 10 && frameNum < 100) {
            frameNumString = "00" + String.valueOf(frameNum);
        } else if (frameNum >= 100 && frameNum < 1000) {
            frameNumString = "0" + String.valueOf(frameNum);
        } else if (frameNum >= 1000 && frameNum < 10000) {
            frameNumString = String.valueOf(frameNum);
        }
        String framePath = videoPath + "/" + videoName + frameNumString + rgbFileExtension;

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        readImageRGB(width, height, framePath, img);

        ImageIcon imageIcon = new ImageIcon(img);
        lbIm = new JLabel(imageIcon);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;

        panel.add(lbIm, c);
    }
    private void showFrame() {
        final JFrame frame = new JFrame("Hyper-Linking Video Authoring Tool");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel rootPanel = new JPanel();

        JPanel topPanel, list, middlePanel, middlePanelLeft, middlePanelRight;
        MouseMotionEvents middleVideoPanelLeft, middleVideoPanelRight;
        JPanel middleSliderPanelLeft, middleSliderPanelRight;
        JButton primaryVideoButton, secondaryVideoButton, createLinkButton, connectButton, saveButton;

        //top panel including all buttons
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));

        primaryVideoButton = new JButton("Load Primary Video");
        secondaryVideoButton = new JButton("Load Secondary Video");
        createLinkButton = new JButton("Create new hyperlink");
        connectButton = new JButton("Connect Video");
        saveButton = new JButton("Save File");

        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));  // vertically append
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(100, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel(new String("HyperLink List"));
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0,1)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        topPanel.add(primaryVideoButton);
        topPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        topPanel.add(secondaryVideoButton);
        topPanel.add(createLinkButton);
        topPanel.add(listPane);
        topPanel.add(connectButton);
        topPanel.add(saveButton);
        topPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        //main panel including two input sliders' information
        middlePanelLeft = new JPanel();
        middlePanelLeft.setLayout(new BoxLayout(middlePanelLeft, BoxLayout.Y_AXIS));
        middlePanelLeft.setPreferredSize(new Dimension(352, 350));
        middlePanelLeft.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));

        GridBagLayout gLayout = new GridBagLayout();
        middleVideoPanelLeft = new MouseMotionEvents(primaryVideo, gLayout);
        middleVideoPanelLeft.setPreferredSize(new Dimension(352, 288));
        middleSliderPanelLeft = new JPanel();
        middleSliderPanelLeft.setPreferredSize(new Dimension(352, 62));
        middlePanelLeft.add(middleSliderPanelLeft);
        middlePanelLeft.add(middleVideoPanelLeft);


        middlePanelRight = new JPanel();
        middlePanelRight.setLayout(new BoxLayout(middlePanelRight, BoxLayout.Y_AXIS));

        middlePanelRight.setPreferredSize(new Dimension(352, 350));
        middlePanelRight.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
        middleSliderPanelRight = new JPanel();
        middleSliderPanelRight.setPreferredSize(new Dimension(352, 62));
        
        middleVideoPanelRight = new MouseMotionEvents(secondaryVideo, gLayout);
        middleVideoPanelRight.setPreferredSize(new Dimension(352, 288));

        middlePanelRight.add(middleSliderPanelRight);
        middlePanelRight.add(middleVideoPanelRight);

        middlePanel = new JPanel(new GridLayout(1, 2));
        middlePanel.add(middlePanelLeft);
        middlePanel.add(middlePanelRight);


        BufferedImage frameOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage frameTwo = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        JLabel lbIm1 = new JLabel();
        JLabel lbIm2 = new JLabel();
        JLabel frameOneLabel = new JLabel();
        JLabel frameTwoLabel = new JLabel();

        // 创建一个滑块，最小值、最大值、初始值 分别为 0、20、10
        final JSlider slider1 = new JSlider(JSlider.HORIZONTAL,1, 9000, 1);
        final JSlider slider2 = new JSlider(JSlider.HORIZONTAL,1, 30, 1);

        slider1.setPaintTicks(true);
        slider1.setPaintLabels(true);

        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);

        // 添加刻度改变监听器
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                middleVideoPanelLeft.removeAll();
                middleVideoPanelLeft.revalidate();
                middleVideoPanelLeft.repaint();
                primary_frame_num = slider1.getValue();
                String primaryFrameNum = "Frame " +  slider1.getValue();
                frameOneLabel.setText(primaryFrameNum);
                slider1.setValue(slider1.getValue());

                primaryVideo.setFrameNum(slider1.getValue());
                showImg(frameOne, lbIm1, middleVideoPanelLeft, slider1, primaryVideo);
            }
        });

        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                middleVideoPanelRight.removeAll();
                middleVideoPanelRight.revalidate();
                middleVideoPanelRight.repaint();

                String secondaryFrameNum = "Frame " +  slider2.getValue();
                frameTwoLabel.setText(secondaryFrameNum);
                slider2.setValue(slider2.getValue());

                secondaryVideo.setFrameNum(slider2.getValue());
                showImg(frameTwo, lbIm2, middleVideoPanelRight, slider2, secondaryVideo);

            }
        });

        primaryVideoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == primaryVideoButton)
                {
                    middleVideoPanelLeft.removeAll();
                    middleVideoPanelLeft.revalidate();
                    middleVideoPanelLeft.repaint();
                    middleSliderPanelLeft.removeAll();
                    middleSliderPanelLeft.revalidate();
                    middleSliderPanelLeft.repaint();
                    list.removeAll();
                    list.revalidate();
                    list.repaint();


                    primaryVideoLinkmapper = new HashMap<>();
                    linkstoragemap = new HashMap<>();

                    JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home"))); //Downloads Directory as default
                    int result = chooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = chooser.getSelectedFile();
                        System.out.println("Selected primary video file path: " + selectedFile.getAbsolutePath());
                        String videoName = parseVideoName(selectedFile.getName());
//                        primary_file_path = selectedFile.getAbsolutePath();
                        StringBuilder filepath = new StringBuilder(selectedFile.getAbsolutePath());
                        filepath.reverse();
                        String temp = filepath.toString();
                        temp = temp.substring(8);
                        StringBuilder buffer = new StringBuilder(temp);
                        buffer.reverse();
                        primary_file_path = buffer.toString();
                        System.out.println(primary_file_path);
                        int videoFrameNum = parseVideoFrameNum(selectedFile.getName());
                        primaryVideo = new Video(videoName, selectedFile.getParent(), videoFrameNum);
                        primary_frame_num = 1;

                        try {
                            FileInputStream fileIn = new FileInputStream(primaryVideo.getVideoPath() + "/primaryVideoLinkmapper.ser");
                            ObjectInputStream in = new ObjectInputStream(fileIn);
                            primaryVideoLinkmapper = (Map<Integer, ArrayList<Rect>>) in.readObject();
                            in.close();
                            fileIn.close();
                        } catch (IOException i) {
                            primaryVideoLinkmapper = new HashMap<>();
                            i.printStackTrace();

                        } catch (ClassNotFoundException c) {
                            System.out.println("primaryVideoLinkmapper class not found");
                            primaryVideoLinkmapper = new HashMap<>();
                            c.printStackTrace();
                        }

                        try {
                            FileInputStream fileIn = new FileInputStream(primaryVideo.getVideoPath() + "/linkstoragemap.ser");
                            ObjectInputStream in = new ObjectInputStream(fileIn);
                            linkstoragemap = (Map<JTextField, int[]>) in.readObject();
                            in.close();
                            fileIn.close();
                            for (JTextField newLink : linkstoragemap.keySet()) {
                                list.add(newLink);
                                list.revalidate();
                                list.repaint();

                                // TODO: not sure if need this code block
                                if(frame_rectnum.get(primary_frame_num) == null){
                                    frame_rectnum.put(primary_frame_num, 1);
                                }else{
                                    int a = frame_rectnum.get(primary_frame_num);
                                    frame_rectnum.put(primary_frame_num, a+1);
                                }

                                newLink.addMouseListener(new MouseListener() {

                                    @Override
                                    public void mouseReleased(MouseEvent e) {// 鼠标松开
                                    }

                                    @Override
                                    public void mousePressed(MouseEvent e) {// 鼠标按下
                                    }

                                    @Override
                                    public void mouseExited(MouseEvent e) {// 鼠标退出组件
                                    }

                                    @Override
                                    public void mouseEntered(MouseEvent e) {// 鼠标进入组件
                                    }

                                    @Override
                                    public void mouseClicked(MouseEvent e) {// 鼠标单击事件
                                        cur_fram_num = linkstoragemap.get(newLink)[0];
                                        link_order_num = linkstoragemap.get(newLink)[1];
                                        slider1.setValue(linkstoragemap.get(newLink)[0]);
                                        middleVideoPanelLeft.revalidate();
                                        middleVideoPanelLeft.repaint();

                                    }
                                });
                            }

                        } catch (IOException i) {
                            linkstoragemap = new HashMap<>();
                            i.printStackTrace();

                        } catch (ClassNotFoundException c) {
                            System.out.println("linkstoragemap class not found");
                            linkstoragemap = new HashMap<>();
                            c.printStackTrace();
                        }


                        middleSliderPanelLeft.add(frameOneLabel);
                        frameOneLabel.setText("Frame " + videoFrameNum);

                        middleSliderPanelLeft.add(slider1);
                        slider1.setValue(videoFrameNum);

                        showImg(frameOne, lbIm1, middleVideoPanelLeft, slider1, primaryVideo);
                    } else if (result == JFileChooser.CANCEL_OPTION) {
                        System.out.println("No file selected");
                    }

                }
            }
        });

        secondaryVideoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == secondaryVideoButton)
                {
                    middleVideoPanelRight.removeAll();
                    middleVideoPanelRight.revalidate();
                    middleVideoPanelRight.repaint();
                    middleSliderPanelRight.removeAll();
                    middleSliderPanelRight.revalidate();
                    middleSliderPanelRight.repaint();

                    JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home"))); //Downloads Directory as default
                    int result = chooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = chooser.getSelectedFile();
                        System.out.println("Selected secondary video file path: " + selectedFile.getAbsolutePath());
                        String videoName = parseVideoName(selectedFile.getName());
                        int videoFrameNum = parseVideoFrameNum(selectedFile.getName());
                        secondaryVideo = new Video(videoName, selectedFile.getParent(), videoFrameNum);
                        secondaryVideoLinkmapper = new HashMap<>();
                        middleSliderPanelRight.add(frameTwoLabel);
                        frameTwoLabel.setText("Frame " + videoFrameNum);
                        middleSliderPanelRight.add(slider2);
                        slider2.setValue(videoFrameNum);
                        secondary_frame_num = 1;
                        showImg(frameTwo, lbIm2, middleVideoPanelRight, slider2, secondaryVideo);
                    } else if (result == JFileChooser.CANCEL_OPTION) {
                        System.out.println("No file selected");
                    }

                }
            }
        });

        createLinkButton.addActionListener((ActionEvent e) -> {
            String linkNameInput = JOptionPane.showInputDialog("Enter a link name");
                if(linkNameInput == null){

                }else if (linkNameInput.length()>0) {

                    JTextField newLink = new JTextField(String.valueOf(linkNameInput));

//                    if(frame_rectnum.get(primary_frame_num) == null){
//                        frame_rectnum.put(primary_frame_num, 1);
//                    }else{
//                        int a = frame_rectnum.get(primary_frame_num);
//                        frame_rectnum.put(primary_frame_num, a+1);
//                    }
                    linkstoragemap.put(newLink, new int[]{primary_frame_num, (int)primaryVideoLinkmapper.get(primary_frame_num).size()});
                    newLink.addMouseListener(new MouseListener() {

                        @Override
                        public void mouseReleased(MouseEvent e) {// 鼠标松开
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {// 鼠标按下
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {// 鼠标退出组件
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {// 鼠标进入组件
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {// 鼠标单击事件
                            cur_fram_num = linkstoragemap.get(newLink)[0];
                            link_order_num = linkstoragemap.get(newLink)[1];
                            slider1.setValue(linkstoragemap.get(newLink)[0]);
                            middleVideoPanelLeft.revalidate();
                            middleVideoPanelLeft.repaint();

                        }
                    });

                    list.add(newLink);
                    list.revalidate();
                    list.repaint();
                    System.out.println("New Link created: "  + String.valueOf(linkNameInput));
                }


        });
        //making the button's text editable

        connectButton.addActionListener((ActionEvent e) -> {
            if (secondaryVideo == null) {
                // TODO: if we didn't upload secondary video, we should do nothing
                return;
            }
            if(MouseMotionEvents.targetRectangle != null &&
                    primaryVideoLinkmapper.containsKey(primaryVideo.getFrameNum())){
                Rect targetRect = MouseMotionEvents.targetRectangle;
                targetRect.setSecondaryFrameNum(secondaryVideo.getFrameNum());
                targetRect.setSecondaryVideoName(secondaryVideo.getVideoName());
                System.out.println("targetRect: " + targetRect.cor1);
                System.out.println("targetRect: " + targetRect.cor2);
                System.out.println("targetRect: " + targetRect.getSecondaryFrameNum());
                System.out.println("targetRect: " + targetRect.getSecondaryVideoName());
            }
            MouseMotionEvents.targetRectangle = null;
        });


        saveButton.addActionListener((ActionEvent e) -> {
            try {
                FileOutputStream fileOut = new FileOutputStream(primaryVideo.getVideoPath() + "/primaryVideoLinkmapper.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(primaryVideoLinkmapper);
                out.close();
                fileOut.close();
                System.out.println("Serialized data is saved in " + primaryVideo.getVideoPath() + "/primaryVideoLinkmapper.ser");
            } catch (IOException i) {
                i.printStackTrace();
            }
            try {
                FileOutputStream fileOut = new FileOutputStream(primaryVideo.getVideoPath() + "/linkstoragemap.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(linkstoragemap);
                out.close();
                fileOut.close();
                System.out.println("Serialized data is saved in " + primaryVideo.getVideoPath() + "/linkstoragemap.ser");
            } catch (IOException i) {
                i.printStackTrace();
            }
        });

        frame.setContentPane(rootPanel);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private String parseVideoName(String fileName) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < fileName.length() && Character.isLetter(fileName.charAt(i))) {
            sb.append(fileName.charAt(i++));
        }
        return sb.toString();
    }

    private int parseVideoFrameNum(String fileName) {
        int videoFrameNum = 0;
        int i = 0;
        while (i < fileName.length()) {
            char cur = fileName.charAt(i);
            if (Character.isLetter(cur) || Character.isDigit(cur) && cur == '0') {
                i++;
            } else {
                break;
            }
        }
        while (i < fileName.length() && fileName.charAt(i) != '.') {
            videoFrameNum *= 10;
            videoFrameNum += fileName.charAt(i) - '0';
            i++;
        }
        return videoFrameNum;
    }

    public static class Video {
        private String videoName;
        private String videoPath;
        private int frameNum;

        public Video() {

        }
        public Video(String videoName, String videoPath, int frameNum) {
            this.videoName = videoName;
            this.frameNum = frameNum;
            this.videoPath = videoPath;
        }

        public String getVideoName() {
            return videoName;
        }
        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getVideoPath() {
            return videoPath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }

        public int getFrameNum() {
            return frameNum;
        }

        public void setFrameNum(int frameNum) {
            this.frameNum = frameNum;
        }
    }

    public static void main(String[] args) {
        AuthoringTool authoringTool = new AuthoringTool();
        authoringTool.showFrame();

    }
}
