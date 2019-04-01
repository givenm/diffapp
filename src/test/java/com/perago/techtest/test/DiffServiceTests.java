/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.perago.techtest.test;

import com.perago.techtest.Diff;
import com.perago.techtest.DiffEngine;
import com.perago.techtest.DiffException;
import com.perago.techtest.DiffRenderer;
import com.perago.techtest.Impl.DiffRendererImpl;
import com.perago.techtest.Impl.DiffServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Given Nyauyanga
 */
public class DiffServiceTests {

    private DiffEngine diffEngine;
    private DiffRenderer diffRenderer;

    @Before
    public void setUp() {
        diffEngine = new DiffServiceImpl();
        diffRenderer = new DiffRendererImpl();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void nullOriginalAndNoneNullModified_ShowAsCreated() throws DiffException {
        Person modifiedPerson = new Person();
        modifiedPerson.setFirstName("Fred");
        modifiedPerson.setSurname("Smith");
        Diff<Person> personDiff = diffEngine.calculate(null, modifiedPerson);
        assertNotNull(personDiff);
        assertNull(personDiff.getDeletedInformation());
        assertNull(personDiff.getUpdatedInformation());
        assertNotNull(personDiff.getCreatedInformation());
        assertFalse(personDiff.getCreatedInformation().isEmpty());

        //Check renderer
        String renderResult = diffRenderer.render(personDiff);

        System.out.println(renderResult);
    }

    @Test
    public void noneNullOriginalAndNullModified_ShowAsDeleted() throws DiffException {
        Person originalPerson = new Person();
        originalPerson.setFirstName("Fred");
        originalPerson.setSurname("Smith");
        Diff<Person> personDiff = diffEngine.calculate(originalPerson, null);
        assertNotNull(personDiff);
        assertNotNull(personDiff.getDeletedInformation());
        assertTrue(personDiff.getDeletedInformation());
        assertNull(personDiff.getUpdatedInformation());
        assertNull(personDiff.getCreatedInformation());
        
        //Check renderer
        String renderResult = diffRenderer.render(personDiff);

        System.out.println(renderResult);
        
    }
}
