import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AuthoringTool {
    JFrame frame;


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

    private void showImg(BufferedImage img, JLabel lbIm, int frameNum, JPanel panel, JSlider slider1) {

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

        String framePath = "Y:\\cs576project\\AIFilmOne\\AIFilmOne\\AIFilmOne" + frameNumString + ".rgb";
        framePath = framePath.replace("\\", "/");
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


        panel.add(slider1);
        panel.add(lbIm, c);
        frame.setContentPane(panel);


    }
    private void showFrame() {
        frame = new JFrame("Hyper-Linking Video Authoring Tool");
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        // 创建一个滑块，最小值、最大值、初始值 分别为 0、20、10
        final JSlider slider1 = new JSlider(1, 10, 1);

        // 设置主刻度间隔
        slider1.setMajorTickSpacing(4);

        // 设置次刻度间隔
        slider1.setMinorTickSpacing(1);

        // 绘制 刻度 和 标签
        slider1.setPaintTicks(true);
        slider1.setPaintLabels(true);



        GridBagLayout gLayout = new GridBagLayout();
        frame.getContentPane().setLayout(gLayout);

        BufferedImage frameOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage frameTwo = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        JLabel lbIm1 = new JLabel(new ImageIcon(frameOne));
        JLabel lbIm2 = new JLabel(new ImageIcon(frameTwo));


        // 添加刻度改变监听器
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("slider1的当前值: " + slider1.getValue());
                panel.removeAll();
                showImg(frameOne, lbIm1, slider1.getValue(), panel, slider1);
            }
        });

        showImg(frameOne, lbIm1, 1, panel, slider1);
        // 添加滑块到内容面板
        panel.add(slider1);
        frame.setContentPane(panel);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        AuthoringTool authoringTool = new AuthoringTool();
        authoringTool.showFrame();

    }
}
