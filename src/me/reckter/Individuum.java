package me.reckter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hannes on 16/12/14.
 */
public class Individuum implements Comparable<Individuum>{

    static Random random = new Random();

    File file;

    long fitness;
    boolean needsToCalculateFittness;


    public Individuum() {
        createFile();
        needsToCalculateFittness = true;
    }

    public Individuum(Individuum other) {
        createFile();
        try {
            Files.copy(other.file.toPath(), file.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fitness = other.fitness;
        needsToCalculateFittness = other.needsToCalculateFittness;
    }

    private void createFile() {
        long value = random.nextLong();

        file = new File("pictures/guesses/" + Generation.generation + "_" + Long.toHexString(value) + ".png");
    }

    public void mutate(int mutations) {
        try {
            BufferedImage image = ImageIO.read(file);
            mutate(image, mutations);
            ImageIO.write(image, "png" , file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mutate(BufferedImage image, int mutations) {
        for(int i = 0; i < mutations; i++) {
            mutateOnePixel(image);
        }
    }

    public long getFitness() {
        if(needsToCalculateFittness) {
            calculateFittnes();
        }
        return fitness;
    }

    private void calculateFittnes() {
        needsToCalculateFittness = false;
        fitness = 0;
        byte[] originPixel = Picture.getPixel();

        byte[] pixel = Picture.loadPixelByteArray(file);

        List<Thread> threads = new ArrayList<>();
        for(int x = 0; x < Picture.SIZE; x++){
            for(int y = 0; y < Picture.SIZE; y++) {
                int pos = x * Picture.SIZE + y;
                int difR = originPixel[pos] - pixel[pos];
                int difG = originPixel[pos + 1] - pixel[pos + 1];
                int difB = originPixel[pos + 2] - pixel[pos + 2];
                int diff = difR * difR + difG * difG + difB * difB;
                fitness += diff;
            }
        }
    }

    private void mutateOnePixel(BufferedImage image) {
        int x1 = random.nextInt(Picture.SIZE);
        int y1 = random.nextInt(Picture.SIZE);

        int x2 = random.nextInt(Picture.SIZE);
        int y2 = random.nextInt(Picture.SIZE);

        Graphics g = image.createGraphics();

        int[] tmpPixel1 = new int[3];
        int[] tmpPixel2 = new int[3];
        image.getData().getPixel(x1, y1, tmpPixel1);
        image.getData().getPixel(x2, y2, tmpPixel2);

        g.setColor(new Color(tmpPixel1[0], tmpPixel1[1], tmpPixel1[2]));
        g.drawLine(x2, y2, x2, y2);

        g.setColor(new Color(tmpPixel2[0], tmpPixel2[1], tmpPixel2[2]));
        g.drawLine(x1, y1, x1, y1);
        needsToCalculateFittness = true;
    }

    static public Individuum createRandomPicture() {
        Individuum ret = new Individuum();
        int[] pixel = new int[Picture.SIZE * Picture.SIZE * 4];

        int r = 0;
        int g = 0;
        int b = 0;

        for(int i = 0; i < Picture.SIZE * Picture.SIZE * 4; i += 4) {
            pixel[i] = r;
            pixel[i + 1] = g;
            pixel[i + 2] = b;

            r++;
            if(r == 256) {
                r = 0;
                g++;
                if(g == 256){
                    g = 0;
                    b++;
                }
            }
        }

        for(int i = (Picture.SIZE * Picture.SIZE - 1) * 4; i > 0; i -= 4) {
            int j = random.nextInt(i);

            r = pixel[j];
            g = pixel[j + 1];
            b = pixel[j + 2];

            pixel[j] = pixel[i];
            pixel[j + 1] = pixel[i + 1];
            pixel[j + 2] = pixel[i + 2];

            pixel[i] = r;
            pixel[i + 1] = g;
            pixel[i + 2] = b;
        }

        BufferedImage image = new BufferedImage(Picture.SIZE, Picture.SIZE, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = ((WritableRaster) image.getData());

        raster.setPixels(0, 0, Picture.SIZE, Picture.SIZE, pixel);
        image.setData(raster);

        try {
            ImageIO.write(image, "png", ret.file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;

    }

    @Override
    public int compareTo(Individuum o) {
        int tmp =  Long.compare(getFitness(), o.getFitness());
        return tmp;
    }

    public void output() {
        System.out.println("individum " + file.getName() + ": " + getFitness());
    }

    @Override
    public String toString() {
        return "Individuum{" +
                "fitness=" + getFitness() +
                '}';
    }
}
