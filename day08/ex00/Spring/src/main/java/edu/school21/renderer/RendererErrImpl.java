package edu.school21.renderer;

import edu.school21.interfaces.PreProcessor;
import edu.school21.interfaces.Renderer;

public class RendererErrImpl implements Renderer {
    private final PreProcessor preProcessor;

    public RendererErrImpl(PreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    @Override
    public void render(String message) {
        System.err.println(preProcessor.preProcess(message));
    }
}