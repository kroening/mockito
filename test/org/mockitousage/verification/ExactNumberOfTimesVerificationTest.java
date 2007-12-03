/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.NumberOfInvocationsError;
import org.mockito.util.RequiresValidState;

@SuppressWarnings("unchecked")
public class ExactNumberOfTimesVerificationTest extends RequiresValidState {

    private LinkedList mock;
    
    @Before
    public void setup() {
        mock = Mockito.mock(LinkedList.class); 
    }
    
    @Test
    public void shouldVerifyActualNumberOfInvocationsSmallerThanWanted() throws Exception {
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, 3).clear();
        try {
            Mockito.verify(mock, 100).clear();
            fail();
        } catch (NumberOfInvocationsError e) {
            String expected = 
                "\n" +
                "LinkedList.clear()" +
        		"\n" +
        		"Wanted 100 times but was 3";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void shouldVerifyActualNumberOfInvocationsLargerThanWanted() throws Exception {
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, 3).clear();
        try {
            Mockito.verify(mock, 1).clear();
            fail();
        } catch (NumberOfInvocationsError e) {
            String expected = 
                "\n" +
                "LinkedList.clear()" +
                "\n" +
                "Wanted 1 time but was 3";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void shouldVerifyProperlyIfMethodWasNotInvoked() throws Exception {
        Mockito.verify(mock, 0).clear();
        try {
            Mockito.verify(mock, 15).clear();
            fail();
        } catch (NumberOfInvocationsError e) {
            assertTrue(e.getMessage().endsWith("Wanted 15 times but was 0"));
        }
    }
    
    @Test
    public void shouldVerifyProperlyIfMethodWasInvokedOnce() throws Exception {
        mock.clear();
        
        Mockito.verify(mock, 1).clear();
        try {
            Mockito.verify(mock, 15).clear();
            fail();
        } catch (NumberOfInvocationsError e) {
            assertTrue(e.getMessage().endsWith("Wanted 15 times but was 1"));
        }
    }
    
    @Test
    public void shouldFailWhenWantedNumberOfInvocationIsZero() throws Exception {
        mock.clear();
        
        try {
            Mockito.verify(mock, 0).clear();
            fail();
        } catch (NumberOfInvocationsError e) {}
    }
    
    @Test
    public void shouldVerifyWhenWantedNumberOfInvocationIsZero() throws Exception {
        Mockito.verify(mock, 0).clear();
    }
    
    @Test
    public void shouldNotCountInStubbedInvocations() throws Exception {
        Mockito.stub(mock.add("test")).andReturn(false);
        Mockito.stub(mock.add("test")).andReturn(true);
        
        mock.add("test");
        mock.add("test");
        
        Mockito.verify(mock, 2).add("test");
    }
}
