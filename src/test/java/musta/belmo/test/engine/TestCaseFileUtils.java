package musta.belmo.test.engine;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TestCaseFileUtils {
	
	protected String getOutputFileContent(String location, int testCaseNumber, boolean isZip) throws IOException {
		final String outputStirngFromFile;
		if (isZip) {
			final InputStream inputFileFromZip = getInputFileFromZip(location, Constants.OUTPUT_FILE_FORMAT,
                    testCaseNumber);
			outputStirngFromFile = IOUtils.toString(inputFileFromZip, "UTF-8");
		} else {
			String outFile = String.format(Constants.OUTPUT_FILE_FORMAT, testCaseNumber);
			File file = new File(location, outFile);
			outputStirngFromFile = FileUtils.readFileToString(file, "UTF-8");
		}
		return outputStirngFromFile;
	}
	
	protected InputStream getInputStreamFromInputFile(String location, int testCaseNumber, boolean isZip) throws IOException {
		final InputStream fileInputStream;
		if (isZip) {
			fileInputStream = getInputFileFromZip(location, Constants.INPUT_FILE_FORMAT, testCaseNumber);
			
		} else {
			String inFile = String.format(Constants.INPUT_FILE_FORMAT, testCaseNumber);
			File file = new File(location, inFile);
			fileInputStream = new FileInputStream(file);
		}
		return fileInputStream;
	}
	
	protected int countTestCases(String location, boolean isZip) throws IOException {
		final int count;
		if (isZip) {
			count = countZipEntries(location) / 2;
		} else {
			Collection<File> files = FileUtils.listFiles(new File(location), new String[]{"txt"}, false);
			count = files.size() / 2;
		}
		return count;
	}
	
	private InputStream getInputFileFromZip(String location, String pattern, int testCaseNumber) throws IOException {
		
		final ZipFile zipFile = getSingleZipFileInDirectory(location);
		final Enumeration<? extends ZipEntry> entries = zipFile.entries();
		InputStream stream = null;
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (entry.getName().endsWith(String.format(pattern, testCaseNumber))) {
				stream = zipFile.getInputStream(entry);
				break;
			}
		}
		return stream;
	}
	
	private ZipFile getSingleZipFileInDirectory(String location) throws IOException {
		File directory = new File(location);
        Collection<File> files = FileUtils.listFiles(directory, new String[]{"zip"}, false);
        if(files.isEmpty()){
            throw new RuntimeException("No zip file found at " + directory.getAbsolutePath());
        }
        return new ZipFile(files.iterator().next());
	}
	
	private int countZipEntries(String location) throws IOException {
		final ZipFile zipFile = getSingleZipFileInDirectory(location);
		return zipFile.size();
	}
}
