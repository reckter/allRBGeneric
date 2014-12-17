package me.reckter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

/**
 * Created by hannes on 16/12/14.
 */
public class Main {

    public static void main(String[] args) {

        Generation generation = new Generation();
        System.out.print("init...");
        generation.init();
        System.out.println("done");

        System.out.println("starting with generations");


        int k = 0;
        while(true) {
            generation.nextGeneration();

            k++;
            if(k % 1 == 0){
                System.out.print(k + ": ");
                generation.output();
            }
        }
    }
}
