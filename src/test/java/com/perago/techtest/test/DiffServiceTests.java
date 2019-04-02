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
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang3.SerializationUtils;
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
    public void nullOriginalAndNoneNullModified_ShowAsCreated_Example_1() throws DiffException {
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
    public void noneNullOriginalAndNullModified_ShowAsDeleted_Example_2() throws DiffException {
        Person originalPerson = new Person();
        originalPerson.setFirstName("Fred");
        originalPerson.setSurname("Smith");
        Diff<Person> personDiff = diffEngine.calculate(originalPerson, null);
        assertNotNull(personDiff);
        assertNotNull(personDiff.getDeletedInformation());
        assertNotNull(personDiff.getDeletedInformation());
        assertNull(personDiff.getUpdatedInformation());
        assertNull(personDiff.getCreatedInformation());

        //Check renderer
        String renderResult = diffRenderer.render(personDiff);
        
        Person personDiffApplied = diffEngine.apply(originalPerson, personDiff);
        assertNull(personDiffApplied);

        System.out.println(renderResult);

    }

    @Test
    public void clonePersonAndUpdateClone_ShowAsUpdated_Example_3() throws DiffException {
        Person originalPerson = new Person();
        originalPerson.setFirstName("Fred");
        originalPerson.setSurname("Smith");

        Person modifiedPerson = SerializationUtils.clone(originalPerson);
        modifiedPerson.setSurname("Jones");

        Diff<Person> personDiff = diffEngine.calculate(originalPerson, modifiedPerson);
        assertNotNull(personDiff);
        assertNull(personDiff.getDeletedInformation());
        assertNotNull(personDiff.getUpdatedInformation());
        assertNull(personDiff.getCreatedInformation());

        //Check renderer
        String renderResult = diffRenderer.render(personDiff);

        System.out.println(renderResult);
    }

    @Test
    public void clonePersonAndUpdateCloneWithInnerPerson_ShowAsUpdatedAndCreated_Example_4() throws DiffException {
        Person originalPerson = new Person();
        originalPerson.setFirstName("Fred");
        originalPerson.setSurname("Smith");

        Person modifiedPerson = SerializationUtils.clone(originalPerson);
        Person firendPerson = new Person();
        firendPerson.setFirstName("Tom");
        firendPerson.setSurname("Brown");
        modifiedPerson.setFriend(firendPerson);

        Diff<Person> personDiff = diffEngine.calculate(originalPerson, modifiedPerson);
        assertNotNull(personDiff);
        assertNull(personDiff.getDeletedInformation()); 
        assertNotNull(personDiff.getUpdatedInformation());
        assertNull(personDiff.getCreatedInformation());

        //Check renderer
        String renderResult = diffRenderer.render(personDiff);

        System.out.println(renderResult);
    }

    @Test
    public void clonePersonWithPetAndPersonAndThenUpdateCloneWithInnerPerson_ShowAsUpdatedForAllChildUpdates_Example_5() throws DiffException {
        Person originalPerson = new Person();
        originalPerson.setFirstName("Fred");
        originalPerson.setSurname("Smith");
        //original pet
        Pet pet = new Pet();
        pet.setName("Rover");
        pet.setType("Spot");

        originalPerson.setPet(pet);

        // original friend
        Person originalFriendPerson = new Person();
        originalFriendPerson.setFirstName("Tom");
        originalFriendPerson.setSurname("Brown");

        originalPerson.setFriend(originalFriendPerson);

        Person modifiedPerson = SerializationUtils.clone(originalPerson);
        modifiedPerson.setSurname("Jones");
        Person modifiedfriendPerson = modifiedPerson.getFriend();
        modifiedfriendPerson.setFirstName("Jim");

        Pet modifiedPet = modifiedPerson.getPet();
        modifiedPet.setName("Spot");

        Diff<Person> personDiff = diffEngine.calculate(originalPerson, modifiedPerson);
        assertNotNull(personDiff);
        assertNull(personDiff.getDeletedInformation());
        assertNotNull(personDiff.getUpdatedInformation());
        assertNull(personDiff.getCreatedInformation());

        //Check renderer
        String renderResult = diffRenderer.render(personDiff);
        System.out.println(renderResult);
    }

    @Test
    public void clonePersonWithNoPetAndWithPersonAndThenUpdateClonePersonWithUpdatedInnerPersonFirstname_ShowAsUpdatedFirstname_Example_6() throws DiffException {
        Person originalPerson = new Person();
        originalPerson.setFirstName("Fred");
        originalPerson.setSurname("Smith");

        // original friend
        Person originalFriendPerson = new Person();
        originalFriendPerson.setFirstName("Tom");
        originalFriendPerson.setSurname("Brown");

        originalPerson.setFriend(originalFriendPerson);

        Person modifiedPerson = SerializationUtils.clone(originalPerson);
        Person modifiedfriendPerson = modifiedPerson.getFriend();
        modifiedfriendPerson.setFirstName("Jim");

        Diff<Person> personDiff = diffEngine.calculate(originalPerson, modifiedPerson);
        assertNotNull(personDiff);
        assertNull(personDiff.getDeletedInformation());
        assertNotNull(personDiff.getUpdatedInformation());
        assertNull(personDiff.getCreatedInformation());

        //Check renderer
        String renderResult = diffRenderer.render(personDiff);
        System.out.println(renderResult);
    }

    @Test
    public void clonePersonWithNoPetAndWithPersonAndThenUpdateClonePersonWithNullFriend_ShowAsUpdatedAndDeletedPerson_Example_7() throws DiffException {
        Person originalPerson = new Person();
        originalPerson.setFirstName("Fred");
        originalPerson.setSurname("Smith");

        // original friend
        Person originalFriendPerson = new Person();
        originalFriendPerson.setFirstName("Tom");
        originalFriendPerson.setSurname("Brown");

        originalPerson.setFriend(originalFriendPerson);

        Person modifiedPerson = SerializationUtils.clone(originalPerson);
        modifiedPerson.setFirstName("John");
        modifiedPerson.setFriend(null);

        Diff<Person> personDiff = diffEngine.calculate(originalPerson, modifiedPerson);
        assertNotNull(personDiff);
        assertNull(personDiff.getDeletedInformation());
        assertNotNull(personDiff.getUpdatedInformation());
        assertNull(personDiff.getCreatedInformation());

        //Check renderer
        String renderResult = diffRenderer.render(personDiff);
        System.out.println(renderResult);
    }

    @Test
    public void clonePersonWithNicknamesAndThenUpdateClonePersonNickname_ShowAsUpdated_Example_8() throws DiffException {
        Person originalPerson = new Person();
        originalPerson.setFirstName("Fred");
        originalPerson.setSurname("Smith");

        Set<String> nicknames = new LinkedHashSet<>();
        nicknames.add("scooter");
        nicknames.add("biff");

        originalPerson.setNickNames(nicknames);

        Person modifiedPerson = SerializationUtils.clone(originalPerson);
        Set<String> updatedNicknames = new LinkedHashSet<>();
        updatedNicknames.add("scooter");
        updatedNicknames.add("polly");
        modifiedPerson.setNickNames(updatedNicknames);

        Diff<Person> personDiff = diffEngine.calculate(originalPerson, modifiedPerson);
        assertNotNull(personDiff);
        assertNull(personDiff.getDeletedInformation());
        assertNotNull(personDiff.getUpdatedInformation());
        assertNull(personDiff.getCreatedInformation());

        //Check renderer
        String renderResult = diffRenderer.render(personDiff);
        System.out.println(renderResult);
    }

}
