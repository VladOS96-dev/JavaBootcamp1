package ex02.ImagesToChar.src.java.edu.school21.printer.logic;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
@Parameters(separators = "=")
public class ParamJComander
{
    @Parameter(names = "--white", description = "Color for white pixels", required = true)
    private  String whiteColor = "WHITE";

    @Parameter(names = "--black", description = "Color for black pixels", required = true)
    private  String blackColor = "BLACK";
    public String getWhiteColor()
    {
        return whiteColor;
    }
    public String getBlackColor()
    {
        return blackColor;
    }
}