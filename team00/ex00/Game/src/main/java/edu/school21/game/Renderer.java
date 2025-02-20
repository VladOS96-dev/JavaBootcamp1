package edu.school21.game;

import com.diogonunes.jcdp.color.api.Ansi;
import com.diogonunes.jcdp.color.ColoredPrinter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import  java.awt.Color;

public class Renderer {
    private final MapData mapData;
    private final ColoredPrinter printer;

    public Renderer(MapData mapData) {
        this.mapData = mapData;
        this.printer = new ColoredPrinter.Builder(1, false).build();
//        mapData.addPropertyChangeListener(this);
    }

//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        if ("mapUpdate".equals(evt.getPropertyName())) {
////            draw();
//        }
//    }

    public void draw() {
        System.out.println("New step=>");
        if (ArgsParser.getInstance().getProfile().equals("dev")) {
            System.out.print("\033[H");
        } else {
            System.out.print("\033[H\033[J");
        }

        for (int i = 0; i < mapData.getSize(); i++) {
            for (int j = 0; j < mapData.getSize(); j++) {
                printCell(mapData.getCell(i, j));
            }
            System.out.println();
        }

    }

    private void printCell(MapElement element) {
        printer.print(element.getSymbol(), Ansi.Attribute.NONE,Ansi.FColor.BLACK , convertToAnsiColor(element.getColor()));
    }

    private Ansi.BColor convertToAnsiColor(Color color) {
        if (color.equals(Color.RED)) return Ansi.BColor.RED;
        if (color.equals(Color.GREEN)) return Ansi.BColor.GREEN;
        if (color.equals(Color.MAGENTA)) return Ansi.BColor.MAGENTA;
        if (color.equals(Color.BLUE)) return Ansi.BColor.BLUE;
        if (color.equals(Color.BLACK)) return Ansi.BColor.BLACK;
        if (color.equals(Color.CYAN)) return Ansi.BColor.CYAN;
        if (color.equals(Color.YELLOW)) return Ansi.BColor.YELLOW;
        return Ansi.BColor.WHITE;
    }
}
