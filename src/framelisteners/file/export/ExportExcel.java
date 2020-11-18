package framelisteners.file.export;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import jxl.Cell;
import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import source.CSVFrame;
import subframes.FileChooser;
import table.MyTableModel;
import table.Record;


public class ExportExcel implements ActionListener {
	WritableWorkbook workBook = null;
	WritableSheet excelSheet = null;
	boolean multiple = false;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int confirm = JOptionPane.showConfirmDialog(null, "Do you want to export a row for each product?","Export with Multiple Rows",JOptionPane.YES_NO_OPTION);
		if(confirm==JOptionPane.YES_OPTION)
			multiple = true;
		File file = FileChooser.SaveXlsFile();
        try {
        	workBook = Workbook.createWorkbook(file);
        	excelSheet = workBook.createSheet("Leads", 0);
        	AddHeaders();
        	for(int row = 0;row<CSVFrame.model.getRowCount();row++) {
        		Record record = CSVFrame.model.getRowAt(row);
        		if(multiple) { 
					if(record.getProducts().length>0) {
						int count = 0;
						for(String product: record.getProducts()) {
							AddRow(record,row,count,product);
							count++;
						}
					}
					else {
						AddRow(record,row,0,"");
					}
				}
				else
					AddRow(record,row,0,"");
        	}
        	workBook.write();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (workBook != null) {
                try {
                    workBook.close();
                    JOptionPane.showMessageDialog(null, "Succesfully exported XLS file");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	private void AddHeaders() throws RowsExceededException, WriteException {
		for(int column = 0;column<MyTableModel.COLUMN_HEADERS.length;column++) {
			String header = MyTableModel.COLUMN_HEADERS[column];
			Label label = new Label(column,0,header);
			excelSheet.addCell(label);	
		}
		if(multiple)
			excelSheet.addCell(new Label(MyTableModel.COLUMN_HEADERS.length,0,"PRODUCTS"));
	}
	private void AddRow(Record record,int row,int count,String product) throws RowsExceededException, WriteException {
		for(int column = 0;column<MyTableModel.COLUMN_HEADERS.length;column++) {
			String value = CSVFrame.model.getValueAt(row, column);
			if(value==null)
				value = "";
			Label label = new Label(column,row+1+count,value.toUpperCase());
			excelSheet.addCell(label);
		}
		if(multiple)
			excelSheet.addCell(new Label(MyTableModel.COLUMN_HEADERS.length,row+1+count,product));
	}
	private void SheetAutoFitColumns(WritableSheet sheet) {
	    for (int i = 0; i < sheet.getColumns(); i++) {
	        Cell[] cells = sheet.getColumn(i);
	        int longestStrLen = -1;

	        if (cells.length == 0)
	            continue;

	        /* Find the widest cell in the column. */
	        for (int j = 0; j < cells.length; j++) {
	            if ( cells[j].getContents().length() > longestStrLen ) {
	                String str = cells[j].getContents();
	                if (str == null || str.isEmpty())
	                    continue;
	                longestStrLen = str.trim().length();
	            }
	        }

	        /* If not found, skip the column. */
	        if (longestStrLen == -1) 
	            continue;

	        /* If wider than the max width, crop width */
	        if (longestStrLen > 255)
	            longestStrLen = 255;

	        CellView cv = sheet.getColumnView(i);
	        cv.setSize(longestStrLen * 256 + 100); /* Every character is 256 units wide, so scale it. */
	        sheet.setColumnView(i, cv);
	    }
	}
}