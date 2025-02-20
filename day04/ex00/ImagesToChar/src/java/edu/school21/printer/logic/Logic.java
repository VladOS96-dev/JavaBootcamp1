package ex00.ImagesToChar.src.java.edu.school21.printer.logic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Logic {
    private final BufferedImage image;
    private final char white;
    private final char black;

    public Logic(String fileName, char white, char black) throws IOException {
        this.white = white;
        this.black = black;
        image = ImageIO.read(new File(fileName));
    }

    public char[][] convertImageToCharArray() {
        char[][] array2D = new char[image.getHeight()][image.getWidth()];
        for (int yPixel = 0; yPixel < image.getHeight(); yPixel++) {
            for (int xPixel = 0; xPixel < image.getWidth(); xPixel++) {
                int color = image.getRGB(xPixel, yPixel);
                if (color == Color.BLACK.getRGB()) {
                    array2D[yPixel][xPixel] = black;
                } else {
                    array2D[yPixel][xPixel] = white;
                }
            }
        }
        return array2D;
    }
}
