# coding club
 java testing template

This template is made in order to simplify the test phase during the contests.
In order to make use of it, follow the steps below :

1. make a `zip` file that contains the input and the output files for each case, each test case input file should be named `input{n}.txt` where `{n}` is the order of the test case, for example `input1.txt`.
The same goes for the test case output files : `output{n}.txt`.
2. Create  a test class by extending the `AbstractTestClass`, and override the two methods : `getClassToBeTested`  and `getTestCasesFilesLocation` :
- The first stands for the class that contains the `main` method that solves the exercise.
- The second one stands for the prefix that you have given to the location where the test cases files are located.

## NEW :
you can put the ZIP file directly in the dedicated directory, the tests will read the inputs and the expected
outputs from that file, when using .zip files, don't forget to *override* the `useZipAsInputOutPutFiles` method to return `true`.
#### For example :
```java
public class KeyBoardTest extends AbstractTestClass {
  @Override
  protected Class getClassToBeTested() {
  return musta.belmo.musta.belmo.letuscode.KeyBoard.class;
 }

  @Override
  protected String getTestCasesFilesLocation() {
  return new java.io.File("LET_US_CODE").getAbsolutePath();
 }

 // if you want to use a zip file while testing
  @Override
     public boolean useZipAsInputOutPutFiles() {
         return true;
     }
}
```
  You can put files in every location you want, the example above requires that the `LET_US_CODE` folder is in the same directory as the java project one.

The tests are performed under Junit.
