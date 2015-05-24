package org.anc.processor.xml

import org.anc.processor.xml.i18n.Messages
import org.junit.*

import javax.ws.rs.core.Response

import static org.junit.Assert.*

/**
 * Created by danmccormack on 12/12/14.
 */
class ProcessorTest {
    private static final Messages MESSAGES = new Messages()

    XMLProcessor processor

    @Before
    void setup() {
        processor = new XMLProcessor()
    }

    @After
    void cleanup() {
        processor = null
    }

    /**
     * Cases to test: passing (1, all, a few)
     *                failing (none, one wrong in a list of right, all wrong, capital)
     */
    @Test
    void testValidAnnotations()
    {
        //PASSING

        //ONE
        def pass1 = ["f.penn"] as List<String>
        assertTrue(processor.validAnnotations(pass1))

        //FEW
        def pass2 = ["f.penn", "f.sentences"] as List<String>
        assertTrue(processor.validAnnotations(pass2))


        //FAILING
        def fail2 = ["thing1", "thing2", "red", "blue"] as List<String>
        assertFalse(processor.validAnnotations(fail2))

        //ONE WRONG IN LIST OF RIGHT
        def fail3 = ["penn", "f.cb", "f.penn", "f.ne"] as List<String>
        assertFalse(processor.validAnnotations(fail3))

        //Capitals
        def fail4 = ["F.PENN"] as List<String>
        assertFalse(processor.validAnnotations(fail4))

    }
    @Test
    /**
     *  Cases to test: empty string, one word, two words, n-words
     */
    void testParseAnnotations()
    {
        //EMPTY STRING
        // The empty string should result in ALL annotation types being selected
        assertTrue("Empty string should return all acceptable annotations.", processor.parseAnnotations("") == processor.Acceptable.toList())

        //ONE WORD
        List expected = ["f.penn"]
        List actual = processor.parseAnnotations("penn")
        assertTrue "Actual is " + actual, actual == expected

        //TWO WORDS
        expected = ["f.penn", "f.cb"]
        assertTrue(processor.parseAnnotations("penn,cb") == expected)

        //N WORDS
        // Use Groovy range operator to populate the collection, then add the 'f.' prefix.
        expected = (1..9).collect { "f." + it }
        actual = processor.parseAnnotations("1,2,3,4,5,6,7,8,9")
        assertTrue("Expected is " + expected + " Actual is " + actual, actual == expected)
    }

    @Test
    void testProcess() {
        Response response = processor.process("penn, s", "MASC3-0202")
        assertTrue response.entity, response.status == 200
    }

    @Test
    void testInvalidDocId() {
        Response response = processor.process("penn, s", "Invalid ID")
        assertTrue response.entity, response.status == 500
        assertTrue "Wrong error message returned", response.entity == MESSAGES.INVALID_ID
    }

    @Test
    void testInvalidAnnotationType() {
        Response response = processor.process("invalid,annotations", "MASC3-0202")
        assertTrue response.entity, response.status == 500
        assertTrue "Wrong error message returned.", response.entity == MESSAGES.INVALID_TYPE
    }
}
