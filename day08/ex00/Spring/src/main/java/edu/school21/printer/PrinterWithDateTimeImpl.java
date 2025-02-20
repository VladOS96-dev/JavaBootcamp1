package edu.school21.printer;

import edu.school21.interfaces.Printer;
import edu.school21.interfaces.Renderer;

import java.time.LocalDateTime;

public class PrinterWithDateTimeImpl implements Printer {
    private final Renderer renderer;

    public PrinterWithDateTimeImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void print(String message) {
        renderer.render(LocalDateTime.now() + " " + message);
    }
}