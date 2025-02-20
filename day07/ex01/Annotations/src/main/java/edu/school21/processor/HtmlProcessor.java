package edu.school21.processor;

import com.google.auto.service.AutoService;
import edu.school21.annotations.HtmlForm;
import edu.school21.annotations.HtmlInput;


import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"edu.school21.annotations.HtmlForm", "edu.school21.annotations.HtmlInput"})

public class HtmlProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                continue;
            }

            HtmlForm formAnnotation = element.getAnnotation(HtmlForm.class);
            String fileName = formAnnotation.fileName();
            String action = formAnnotation.action();
            String method = formAnnotation.method();

            StringBuilder html = new StringBuilder();
            html.append("<form action=\"").append(action).append("\" method=\"").append(method).append("\">\n");

            for (Element enclosed : element.getEnclosedElements()) {
                if (enclosed.getKind() == ElementKind.FIELD && enclosed.getAnnotation(HtmlInput.class) != null) {
                    HtmlInput inputAnnotation = enclosed.getAnnotation(HtmlInput.class);
                    html.append("\t<input type=\"").append(inputAnnotation.type()).append("\" ")
                            .append("name=\"").append(inputAnnotation.name()).append("\" ")
                            .append("placeholder=\"").append(inputAnnotation.placeholder()).append("\">\n");
                }
            }

            html.append("\t<input type=\"submit\" value=\"Send\">\n");
            html.append("</form>");

            try {
                createHtmlFile(fileName, html.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void createHtmlFile(String fileName, String content) throws IOException {
        PrintWriter writer = new PrintWriter(
                processingEnv.getFiler().createResource(
                                javax.tools.StandardLocation.CLASS_OUTPUT, "", fileName)
                        .openWriter());
        writer.write(content);
        writer.close();
    }
}