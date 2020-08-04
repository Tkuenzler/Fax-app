package Log;

public enum Log {
	SQLLog("Log\\SQLLog.txt"),
	EmdeonLog("Log\\EmdeonLog.txt"),
	PdfLog("Log\\PdfLog.txt"),
	FaxLog("Log\\FaxLog.txt");
	public String file;

    Log(String file) {
        this.file = file;
    }
    public String file() {
        return file;
    }
}
