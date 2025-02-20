package ex02.ImagesToChar.src.java.edu.school21.printer.app;


import ex02.ImagesToChar.src.java.edu.school21.printer.logic.Logic;
import ex02.ImagesToChar.src.java.edu.school21.printer.logic.ParamJComander;
import com.beust.jcommander.JCommander;

import java.io.IOException;


public class App {


    public static void main(String[] args) {
        try {
            ParamJComander param = new ParamJComander();
            JCommander jcommander = new JCommander(param);
            jcommander.parse(args);
            Logic logic = new Logic("/resources/image.bmp", param);
            logic.printImageWithColors();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
