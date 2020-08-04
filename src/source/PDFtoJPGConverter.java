package source;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFtoJPGConverter {
	
	public static File convertPdfToImage(File file, String destination) throws Exception {
		PDDocument doc = PDDocument.load(file);
	    PDFRenderer renderer = new PDFRenderer(doc);
	    File fileTemp = new File(destination+"\\temp.jpg");
	    if(!fileTemp.exists())
	    	fileTemp.createNewFile();
	    for (int i = 0; i < doc.getNumberOfPages(); i++) {
	        // default image files path: original file path
	           // if necessary, file.getParent() + "/" => another path
	           BufferedImage image = renderer.renderImageWithDPI(i, 200);
	           // 200 is sample dots per inch.
	           // if necessary, change 200 into another integer.
	           ImageIO.write(image, "JPEG", fileTemp); // JPEG or PNG
	       }
	       doc.close();
	      
	       return fileTemp;
	}
}    
