package ex02.ImagesToChar.src.java.edu.school21.printer.logic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import com.diogonunes.jcdp.color.api.Ansi;
import com.diogonunes.jcdp.color.ColoredPrinter;
import  java.io.InputStream;
public class Logic {
    private final BufferedImage image;
    private final String white;
    private final String black;

    public Logic(String filename, ParamJComander param) throws IOException {
        this.white = param.getWhiteColor();
        this.black = param.getBlackColor();
        image = ImageIO.read( Logic.class.getResource(filename));
    }

    public void printImageWithColors() {
        ColoredPrinter printer = new ColoredPrinter.Builder(1, false).build();

        for (int yPixel = 0; yPixel < image.getHeight(); yPixel++) {
            for (int xPixel = 0; xPixel < image.getWidth(); xPixel++) {
                int color = image.getRGB(xPixel, yPixel);
                if (color == Color.BLACK.getRGB()) {
                    printer.print(" ", Ansi.Attribute.NONE, Ansi.FColor.NONE, Ansi.BColor.valueOf(black));
                } else {
                    printer.print(" ", Ansi.Attribute.NONE, Ansi.FColor.NONE, Ansi.BColor.valueOf(white));
                }
            }
            System.out.println();
        }
    }

}
