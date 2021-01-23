package musta.belmo.test.engine;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractTestClass extends TestCaseFileUtils {
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private final PrintStream sysOut = System.out;
	private final InputStream sysIn = System.in;
	private Map<Integer, Duration> durations = new LinkedHashMap<>();
	private Map<Integer, String> successOrFailures = new LinkedHashMap<>();
	
	private int totalTest;
	private int failedTests;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	
	protected abstract <T> Class<T> getClassToBeTested();
	
	protected abstract String getTestCasesFilesLocation();
	
	@Before
	public void beforeTest() {
		System.setOut(new PrintStream(outputStream));
	}
	
	@Test
	public void test() throws Exception {
		boolean isZip = useZipAsInputOutPutFiles();
		String testCasesLocation = getTestCasesFilesLocation();
		totalTest = countTestCases(testCasesLocation, isZip);
		
		for (int i = 1; i <= totalTest; i++) {
			System.setIn(getInputStreamFromInputFile(testCasesLocation, i, isZip));
			Class<Object> classToBeTested = getClassToBeTested();
			Method main = classToBeTested.getMethod(Constants.MAIN_METHOD, String[].class);
			Instant now = Instant.now();
			main.invoke(null, new Object[]{null});
			Instant then = Instant.now();
			Duration res = Duration.between(now, then);
			String result = outputStream.toString("UTF-8").trim();
			String expected = getOutputFileContent(testCasesLocation, i, isZip).trim();
			
			try {
				Assert.assertEquals(expected, result);
				durations.put(i, res);
				successOrFailures.put(i, "SUCCESS");
			} catch (Throwable b) {
				successOrFailures.put(i, "FAILURE");
				String reason = String.format("test %d didn't pass for the class [%s]", i, classToBeTested.getName());
				ComparisonFailure exception = new ComparisonFailure(reason, expected, result);
				collector.addError(exception);
				failedTests++;
			}
			outputStream.reset();
		}
	}
	
	@After
	public void afterTest() {
		System.setOut(sysOut);
		outputStream = new ByteArrayOutputStream();
		System.setIn(sysIn);
		printDurations();
		System.out.println("total tests " + totalTest);
		System.out.println("passed tests " + (totalTest - failedTests));
		
	}
	
	public String format(Duration d) {
		long days = d.toDays();
		d = d.minusDays(days);
		long hours = d.toHours();
		d = d.minusHours(hours);
		long minutes = d.toMinutes();
		d = d.minusMinutes(minutes);
		long seconds = d.getSeconds();
		d = d.minusSeconds(seconds);
		long millis = d.toMillis();
		d = d.minusMillis(millis);
		long nanos = d.toNanos();
		return
				(days == 0 ? "" : days + " jours,") +
						(hours == 0 ? "" : hours + " heures,") +
						(minutes == 0 ? "" : minutes + " minutes,") +
						(seconds == 0 ? "" : seconds + " secondes,") +
						(millis == 0 ? "" : millis + " millis, ");
	}
	
	private void printDurations() {
		for (Map.Entry<Integer, Duration> integerDurationEntry : durations.entrySet()) {
			System.out.printf("input %d took %s %s%n", integerDurationEntry.getKey(),
					format(integerDurationEntry.getValue()),
					successOrFailures.get(integerDurationEntry.getKey()));
		}
	}
	
	protected boolean useZipAsInputOutPutFiles() {
		return false;
	}
}