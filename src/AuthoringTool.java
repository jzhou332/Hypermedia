import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static java.awt.Component.LEFT_ALIGNMENT;

public class AuthoringTool {
    final int width = 352;
    final int height = 288;
    final String rgbFileExtension = new String(".rgb");
    Video primaryVideo = new Video();
    Video secondaryVideo = new Video();

    private void readImageRGB(int width, int height, String imgPath, BufferedImage img) {
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
        JPanel middleVideoPanelLeft, middleSliderPanelLeft, middleVideoPanelRight, middleSliderPanelRight;
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
        middlePanelLeft.setPreferredSize(new Dimension(420, 350));
        middleVideoPanelLeft = new JPanel();
        middleSliderPanelLeft = new JPanel();
        middlePanelLeft.add(middleSliderPanelLeft);
        middlePanelLeft.add(middleVideoPanelLeft);


        middlePanelRight = new JPanel();
        middlePanelRight.setLayout(new BoxLayout(middlePanelRight, BoxLayout.Y_AXIS));
        middlePanelRight.setPreferredSize(new Dimension(420, 350));
        middleSliderPanelRight = new JPanel();
        middleVideoPanelRight = new JPanel();
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
        final JSlider slider1 = new JSlider(JSlider.HORIZONTAL,1, 30, 1);
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

                    JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home"))); //Downloads Directory as default
                    int result = chooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = chooser.getSelectedFile();
                        System.out.println("Selected file path: " + selectedFile.getAbsolutePath());
                        String videoName = parseVideoName(selectedFile.getName());
                        int videoFrameNum = parseVideoFrameNum(selectedFile.getName());
                        primaryVideo = new Video(videoName, selectedFile.getParent(), videoFrameNum);

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
                        System.out.println("Selected file path: " + selectedFile.getAbsolutePath());
                        String videoName = parseVideoName(selectedFile.getName());
                        int videoFrameNum = parseVideoFrameNum(selectedFile.getName());
                        secondaryVideo = new Video(videoName, selectedFile.getParent(), videoFrameNum);

                        middleSliderPanelRight.add(frameTwoLabel);
                        frameTwoLabel.setText("Frame " + videoFrameNum);
                        middleSliderPanelRight.add(slider2);
                        slider2.setValue(videoFrameNum);

                        showImg(frameTwo, lbIm2, middleVideoPanelRight, slider2, secondaryVideo);
                    } else if (result == JFileChooser.CANCEL_OPTION) {
                        System.out.println("No file selected");
                    }

                }
            }
        });

        createLinkButton.addActionListener((ActionEvent e) -> {
            String linkNameInput = JOptionPane.showInputDialog(null, "Enter a link name", "Set link name", JOptionPane.OK_CANCEL_OPTION);
            {
                if (String.valueOf(linkNameInput) == null) {
                    JButton newLinkBtn = new JButton("new link");
                }
                JButton newLinkBtn = new JButton(String.valueOf(linkNameInput));
                list.add(newLinkBtn);
                list.revalidate();
                list.repaint();
                System.out.println("New Link created: "  + String.valueOf(linkNameInput));
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
