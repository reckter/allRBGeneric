package me.reckter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

/**
 * Created by hannes on 16/12/14.
 */
public class Picture {

    public static final int SIZE = 4096;

    public static File file;

    private static byte[] pixel = null;

    public static byte[] getPixel() {
        if(pixel == null) {
            loadPixel();
        }
        return pixel;
    }

    public static void setFile(String file) {
        Picture.file = new File(file);
    }

    public static void loadPixel() {
        pixel = loadPixelByteArray(file);
    }

    public static byte[] loadPixelByteArray(File file) {
        byte[] ret = new byte[SIZE * SIZE * 3];

        try {
            BufferedImage image = ImageIO.read(file);
            if(image.getAlphaRaster() == null) {
                ret = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            } else {
                byte[] pixelWithAlpha;
                pixelWithAlpha = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
                for(int i = 0; i < SIZE * SIZE; i++) {
                    ret[i * 3] = pixelWithAlpha[i * 4];
                    ret[i * 3 + 1] = pixelWithAlpha[i * 4 + 1];
                    ret[i * 3 + 2] = pixelWithAlpha[i * 4 + 2];
                }
            }
        } catch (IOException e) {
            System.out.println(file.getName());
            e.printStackTrace();
        }
        return ret;
    }
}
