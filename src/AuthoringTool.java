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
    JFrame frame;

    JButton importPrimaryBtn;
    JButton importSecondaryBtn;
    JButton createHyperlinkBtn;

    int width = 352;   
    int height = 288;
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


    private void showImg(BufferedImage img, JLabel lbIm, JPanel panel, JSlider slider, String framePath) {
        int frameNum = slider.getValue();
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
        if (framePath == null) {
            framePath = "Y:\\cs576project\\AIFilmOne\\AIFilmOne\\AIFilmOne" + frameNumString + ".rgb";
        }

//        String framePath = "Y:\\cs576project\\AIFilmOne\\AIFilmOne\\AIFilmOne" + frameNumString + ".rgb";
        String framePath = "/Users/Yueting/Desktop/Hypermedia/AIFilmOne/AIFilmOne/AIFilmOne"+frameNumString+".rgb";
//        framePath = framePath.replace("\\", "/");
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        readImageRGB(width, height, framePath, img);


        lbIm = new JLabel(new ImageIcon(img));

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;



//        panel.add(slider);
        panel.add(lbIm, c);
//        frame.setContentPane(panel);


    }
    private void showFrame() {
        frame = new JFrame("Hyper-Linking Video Authoring Tool");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel rootPanel = new JPanel();

        JPanel topPanel, middlePanelLeft, middlePanelRight, list;
        JButton primaryVideoButton, secondaryVideoButton, createLinkButton, connectButton, saveButton;

        //top panel including all buttons
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));

        primaryVideoButton = new JButton("Load Primary Video");
        secondaryVideoButton = new JButton("Load Secondary Video");
        createLinkButton = new JButton("Create new hyperlink");
        connectButton = new JButton("Connect Video");
        saveButton = new JButton("Save File");

        String labelText = "HyperLink List";
        list = new JPanel();
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(100, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel(labelText);
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
        middlePanelLeft.setLayout(new BoxLayout(middlePanelLeft, BoxLayout.PAGE_AXIS));
        middlePanelRight = new JPanel();
        middlePanelRight.setLayout(new BoxLayout(middlePanelRight, BoxLayout.PAGE_AXIS));

        BufferedImage frameOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage frameTwo = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        JLabel lbIm1 = new JLabel(new ImageIcon(frameOne));
        JLabel lbIm2 = new JLabel(new ImageIcon(frameTwo));

        // 创建一个滑块，最小值、最大值、初始值 分别为 0、20、10
        final JSlider slider1 = new JSlider(JSlider.HORIZONTAL,1, 9000, 1);
        final JSlider slider2 = new JSlider(JSlider.HORIZONTAL,1, 10, 1);

//        // 设置主刻度间隔
//        slider1.setMajorTickSpacing(4);
//        slider2.setMajorTickSpacing(4);
        // 设置次刻度间隔
//        slider1.setMinorTickSpacing(1);
//        slider2.setMinorTickSpacing(1);
        // 绘制 刻度 和 标签
        slider1.setPaintTicks(true);
        slider1.setPaintLabels(true);


        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);


        // 添加刻度改变监听器
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                middlePanelLeft.removeAll();
                middlePanelLeft.revalidate();
                middlePanelLeft.repaint();
                String primaryFrameNum = "Frame " +  slider1.getValue();
                JLabel labelFrame1 = new JLabel(primaryFrameNum);
                middlePanelLeft.add(labelFrame1);
                middlePanelLeft.add(slider1);
                showImg(frameOne, lbIm1, slider1.getValue(), middlePanelLeft, slider1);
            }
        });

        String primaryFrameNum = "Frame 1";
        JLabel labelFrame1 = new JLabel(primaryFrameNum);
        middlePanelLeft.add(labelFrame1);
        middlePanelLeft.add(slider1);
        showImg(frameOne, lbIm1, 1, middlePanelLeft, slider1);

        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                middlePanelLeft.removeAll();
                middlePanelLeft.revalidate();
                middlePanelLeft.repaint();
                String primaryFrameNum = "Frame " +  slider1.getValue();
                JLabel labelFrame1 = new JLabel(primaryFrameNum);
                middlePanelLeft.add(labelFrame1);
                middlePanelLeft.add(slider1);
                showImg(frameOne, lbIm1, slider1.getValue(), middlePanelLeft, slider1);
            }
        });
        String primaryFrameNum = "Frame 1";
        JLabel labelFrame1 = new JLabel(primaryFrameNum);
        middlePanelLeft.add(labelFrame1);
        middlePanelLeft.add(slider1);
        showImg(frameOne, lbIm1, 1, middlePanelLeft, slider1);

        // 添加滑块到内容面板
        panel.add(slider1);
        panel.add(slider2);




        importPrimaryBtn = new JButton("Import Primary video");
        importPrimaryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == importPrimaryBtn)
                {
                    JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home"))); //Downloads Directory as default
                    int result = chooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = chooser.getSelectedFile();
                        String path = selectedFile.getAbsolutePath();
                        System.out.println("Selected file path: " + path);
                        showImg(frameOne, lbIm1, panel, slider1, path);
                    } else if (result == JFileChooser.CANCEL_OPTION) {
                        System.out.println("No file selected");
                    }

                }
            }
        });
        panel.add(importPrimaryBtn);

        importSecondaryBtn = new JButton("Import Secondary video");
        importSecondaryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == importSecondaryBtn)
                {
                    JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home"))); //Downloads Directory as default
                    int result = chooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = chooser.getSelectedFile();
                        String path = selectedFile.getAbsolutePath();
                        System.out.println("Selected file path: " + path);
                        showImg(frameTwo, lbIm2, panel, slider2, path);
                    } else if (result == JFileChooser.CANCEL_OPTION) {
                        System.out.println("No file selected");
                    }

                }
            }
        });
        panel.add(importSecondaryBtn);

        createHyperlinkBtn = new JButton("Create new hyperlink");
        createHyperlinkBtn.addActionListener((ActionEvent e) -> {
            String linkNameInput;
            String defaultLinkName = "new link";
            linkNameInput = JOptionPane.showInputDialog(null, "Enter a link name", "Set link name", JOptionPane.OK_CANCEL_OPTION);
            {
                JButton newLinkBtn = new JButton(String.valueOf(linkNameInput));
                panel.add(newLinkBtn);
                System.out.println("New Link created: "  + String.valueOf(linkNameInput));
            }
        });
        panel.add(createHyperlinkBtn);


        frame.setContentPane(panel);


        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                middlePanelRight.removeAll();
                middlePanelRight.revalidate();
                middlePanelRight.repaint();
                String secondaryFrameNum = "Frame " +  slider2.getValue();
                JLabel labelFrame2 = new JLabel(secondaryFrameNum);
                middlePanelRight.add(labelFrame2);
                middlePanelRight.add(slider2);
                showImg(frameTwo, lbIm2, slider2.getValue(), middlePanelRight, slider2);

            }
        });
        String secondaryFrameNum = "Frame 1";
        JLabel labelFrame2 = new JLabel(secondaryFrameNum);
        middlePanelRight.add(labelFrame2);
        middlePanelRight.add(slider2);
        showImg(frameTwo, lbIm2, 1, middlePanelRight, slider2);


        frame.setContentPane(rootPanel);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlePanelLeft,BorderLayout.WEST);
        frame.add(middlePanelRight,BorderLayout.EAST);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        AuthoringTool authoringTool = new AuthoringTool();
        authoringTool.showFrame();

    }
}
