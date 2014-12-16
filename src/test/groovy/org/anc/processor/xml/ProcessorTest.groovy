package org.anc.processor.xml

import org.anc.processor.xml.i18n.Messages
import org.junit.*

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

}
