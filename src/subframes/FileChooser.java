package subframes;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import source.CSVFrame;

public class FileChooser {
	public static File OpenTxtFile(String title) {
		JFileChooser fc = new JFileChooser(".");
		fc.setDialogTitle(title);
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "Text Files";
		        }
		    });
		int r = fc.showOpenDialog(new JFrame());
		if (r == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		}
		else
			return null;
	}
	public static File OpenCsvFile(String title) {
		JFileChooser fc = null;
		if(CSVFrame.title.toString().length()==0)
			fc = new JFileChooser(".");
		else
			fc = new JFileChooser(new File(CSVFrame.title.toString()).getParent());
		fc.setDialogTitle(title);
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "CSV Files";
		        }
		    });
		int r = fc.showOpenDialog(new JFrame());
		if (r == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		}
		else
			return null;
	}
	public static File OpenInfoFile(String title) {
		JFileChooser fc = null;
		if(CSVFrame.title.toString().length()==0)
			fc = new JFileChooser(".");
		else
			fc = new JFileChooser(new File(CSVFrame.title.toString()).getParent());
		fc.setDialogTitle(title);
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".json") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "JSON File";
		        }
		    });
		int r = fc.showOpenDialog(new JFrame());
		if (r == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		}
		else
			return null;
	}
	public static File OpenXlsFile(String title) {
		JFileChooser fc = new JFileChooser(".");
		fc.setDialogTitle(title);
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".xls") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "Excel Files";
		        }
		    });
		int r = fc.showOpenDialog(new JFrame());
		if (r == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		}
		else
			return null;
	}
	public static File OpenXlsxFile(String title) {
		JFileChooser fc = new JFileChooser(".");
		fc.setDialogTitle(title);
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".xlsx") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "Excel Files";
		        }
		    });
		int r = fc.showOpenDialog(new JFrame());
		if (r == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		}
		else
			return null;
	}
	public static File[] OpenMultipleCsvFiles() {
		JFileChooser fc = new JFileChooser(".");
		fc.setMultiSelectionEnabled(true);
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "CSV Files";
		        }
		    });
		int r = fc.showOpenDialog(new JFrame());
		if (r == JFileChooser.APPROVE_OPTION) 
			return fc.getSelectedFiles();
		else
			return null;
	}
	public static File OpenIniFile() {
		JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".ini") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "INI Files";
		        }
		      });
		int r = fc.showOpenDialog(new JFrame());
	    if (r == JFileChooser.APPROVE_OPTION) 
	    	return fc.getSelectedFile();
	    else
	    	return null;
	}
	public static String OpenPdfFile(String title) {
		JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".pdf") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "PDF Files";
		        }
		      });
		fc.setDialogTitle(title);
		int r = fc.showOpenDialog(new JFrame());
	    if (r == JFileChooser.APPROVE_OPTION) 
	    	return fc.getSelectedFile().getAbsolutePath();
	    else
	    	return null;
	}
	public static File SaveIniFile() {
		JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".ini") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "Ini File";
		        }
		      });
		int r = fc.showSaveDialog(new JFrame());
	    if (r == JFileChooser.APPROVE_OPTION) {
	    	String savedName;
	    	File file = fc.getSelectedFile();
	    	if(!file.getAbsolutePath().toLowerCase().endsWith(".ini"))
		    	savedName = file.getAbsolutePath().concat(".ini");
		    else 
		    	savedName = file.getAbsolutePath();
	    	file = new File(savedName);
	    	if (!file.exists())
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else {
				int confirm = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to overwrite this file");
				if(confirm==1)
					return null;
			}
	    	return file;
	    }
	    else 
	    	return null;
	}
	public static File SaveCsvFile() {
		JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "CSV Files";
		        }
		      });
		int r = fc.showSaveDialog(null);
	    if (r == JFileChooser.APPROVE_OPTION) {
	    	File file = fc.getSelectedFile();
	    	String savedName = null;
	    	if(file==null)
				return null;
		    if(!file.getAbsolutePath().toLowerCase().endsWith(".csv"))
		    	savedName = file.getAbsolutePath().concat(".csv");
		    else 
		    	savedName = file.getAbsolutePath();
		    file = new File(savedName);
		    if (!file.exists())
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else {
				int confirm = JOptionPane.showConfirmDialog(new JFrame("Export file"), "Are you sure you want to overwrite this file", "Export", JOptionPane.YES_NO_OPTION);
				if(confirm==JOptionPane.YES_OPTION)
					return file;
				else
					return null;
			}
	    	return file;
	    }
	    else
	    	return null;
	}
	public static File SaveTxtFile() {
		JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "TXT Files";
		        }
		      });
		int r = fc.showSaveDialog(null);
	    if (r == JFileChooser.APPROVE_OPTION) {
	    	File file = fc.getSelectedFile();
	    	String savedName = null;
	    	if(file==null)
				return null;
		    if(!file.getAbsolutePath().toLowerCase().endsWith(".txt"))
		    	savedName = file.getAbsolutePath().concat(".txt");
		    else 
		    	savedName = file.getAbsolutePath();
		    file = new File(savedName);
		    if (!file.exists())
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else {
				int confirm = JOptionPane.showConfirmDialog(new JFrame("Export file"), "Are you sure you want to overwrite this file", "Export", JOptionPane.YES_NO_OPTION);
				if(confirm==JOptionPane.YES_OPTION)
					return file;
				else
					return null;
			}
	    	return file;
	    }
	    else
	    	return null;
	}
	public static File SaveXlsFile() {
		JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".xls") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "XLS Files";
		        }
		      });
		int r = fc.showSaveDialog(null);
	    if (r == JFileChooser.APPROVE_OPTION) {
	    	File file = fc.getSelectedFile();
	    	String savedName = null;
	    	if(file==null)
				return null;
		    if(!file.getAbsolutePath().toLowerCase().endsWith(".xls"))
		    	savedName = file.getAbsolutePath().concat(".xls");
		    else 
		    	savedName = file.getAbsolutePath();
		    file = new File(savedName);
		    if (!file.exists())
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else {
				int confirm = JOptionPane.showConfirmDialog(new JFrame("Export file"), "Are you sure you want to overwrite this file", "Export", JOptionPane.YES_NO_OPTION);
				if(confirm==JOptionPane.YES_OPTION)
					return file;
				else
					return null;
			}
	    	return file;
	    }
	    else
	    	return null;
	}
	public static File AppendCsvFile() {
		JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	        public boolean accept(File f) {
		          return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
		        }
		        public String getDescription() {
		          return "CSV Files";
		        }
		      });
		int r = fc.showSaveDialog(null);
	    if (r == JFileChooser.APPROVE_OPTION) {
	    	File file = fc.getSelectedFile();
	    	String savedName = null;
	    	if(file==null)
				return null;
		    if(!file.getAbsolutePath().toLowerCase().endsWith(".csv"))
		    	savedName = file.getAbsolutePath().concat(".csv");
		    else 
		    	savedName = file.getAbsolutePath();
		    file = new File(savedName);
		    if (!file.exists())
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else {
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to append this file","Append File",JOptionPane.YES_NO_OPTION);
				if(confirm==JOptionPane.OK_OPTION)
					return file;
				else
					return null;
			}
		    return file;
	    	
	    }
	    else
	    	return null;
	}
	public static String OpenFolder(String title) {
		JFileChooser fc = new JFileChooser(".");
		fc.setDialogTitle("Choose folder");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setDialogTitle(title);
		int r = fc.showOpenDialog(new JFrame());
		if (r == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile().getAbsolutePath();
		}
		else
			return null;
	}
}
