package edu.school21.app;

import edu.school21.interfaces.PreProcessor;
import edu.school21.preprocessor.PreProcessorToLowerImpl;
import edu.school21.preprocessor.PreProcessorToUpperImpl;
import edu.school21.interfaces.Printer;
import edu.school21.printer.PrinterWithDateTimeImpl;
import edu.school21.printer.PrinterWithPrefixImpl;
import edu.school21.interfaces.Renderer;
import edu.school21.renderer.RendererErrImpl;
import edu.school21.renderer.RendererStandardImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Program {
    public static void main(String [] args)
    {
        PreProcessor preProcessorUpper = new PreProcessorToUpperImpl();
        PreProcessor preProcessorLower = new PreProcessorToLowerImpl();

        Renderer rendererStandard = new RendererStandardImpl(preProcessorUpper);
        Renderer rendererErr = new RendererErrImpl(preProcessorLower);

        Printer printerWithPrefix = new PrinterWithPrefixImpl(rendererErr);
        ((PrinterWithPrefixImpl) printerWithPrefix).setPrefix("Prefix:");

        Printer printerWithDateTime = new PrinterWithDateTimeImpl(rendererStandard);
        System.out.println("Работа без bean ");
        printerWithPrefix.print("Hello, World!");
        printerWithDateTime.print("Current Time Message!");
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");


        Printer printerWithPrefixBin = context.getBean("printerWithPrefix", Printer.class);
        Printer printerWithPrefixStdBin = context.getBean("printerWithPrefixStd", Printer.class);
        Printer printerWithDateBin = context.getBean("printerWithDate", Printer.class);
        Printer printerWithDateErrBin = context.getBean("printerWithDateErr", Printer.class);

        System.out.println("Работа с bean");
        printerWithPrefixBin.print("Hello, World!");
        printerWithPrefixStdBin.print("Spring DI!");
        printerWithDateBin.print("Current Date Message");
        printerWithDateErrBin.print("Error Renderer!");
    }


}
