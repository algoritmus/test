package com.vogella.junit.first;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MyClassTest {

    // fields used together with @Parameter must be public
    @Parameter
    public int m1;
    @Parameter (value = 1)
    public int m2;


    // creates the test data
    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] { { 1 , 2 }, { 5, 3 }, { 1210, 4 } };
        return Arrays.asList(data);
    }

	
	@Test(expected = IllegalArgumentException.class)
	  public void testExceptionIsThrown() {
	    MyClass tester = new MyClass();
	    System.out.println("m1: " + m1 + " m2: " + m2);
	    tester.multiply(m1, m2);
	  }

	  @Test
	  public void testMultiply() {
	    MyClass tester = new MyClass();
	    System.out.println("m1: " + m1 + " m2: " + m2);
	    assertEquals("10 x 5 must be 50", 50, tester.multiply(10, 5));
	  }

}
