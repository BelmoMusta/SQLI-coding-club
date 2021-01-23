
package musta.belmo.keyboard;

import musta.belmo.test.engine.ZipAsTestFilesAbstractTestClass;

public class KeyboardTest extends ZipAsTestFilesAbstractTestClass {
	
	@Override
	protected Class getClassToBeTested() {
		return KeyBoard.class;
	}
	
	@Override
	protected String getTestCasesFilesLocation() {
		return new java.io.File("LET_US_CODE").getAbsolutePath();
	}
}