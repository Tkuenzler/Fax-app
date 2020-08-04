package framelisteners.fax.frames;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;


public class ViewFax extends JFrame {
	JScrollPane scrollPane;
	JPanel imagePanel = new JPanel();
	public ViewFax() {
		super("VIEW PDF");
		scrollPane = new JScrollPane(imagePanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setOpaque(false);
		getContentPane().add(scrollPane,BorderLayout.CENTER);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		imagePanel.setLayout(new GridLayout(0,1));
		File file = new File("C:\\Users\\tkuen\\Desktop\\BAA Agreement.pdf");
		
		DisplayPDF(file);
	}
	private void DisplayPDF(File file) {
		try {
			PDDocument doc = PDDocument.load(file);
		    PDFRenderer renderer = new PDFRenderer(doc);
		    for (int i = 0; i < doc.getNumberOfPages(); i++) {
		    	BufferedImage image = renderer.renderImage(i);
		    	imagePanel.add(new JLabel(new ImageIcon(image)),BorderLayout.CENTER);
		     }
		     doc.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.repaint();
	}
}

