package org.anc.processor.xml

import org.anc.conf.AnnotationConfig
import org.anc.index.api.Index
import org.anc.processor.xml.i18n.Messages
import org.anc.tool.api.IProcessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xces.graf.io.dom.ResourceHeader

//maven include too.api


import javax.ws.rs.GET
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Response
/**
 * Created by danmccormack on 12/9/14.
 */
class AbstractProcessor {
    private static final Logger logger = LoggerFactory.getLogger(XMLProcessor)

    private static final Messages MESSAGES = new Messages()

    //The annotations that can be handled by xml processor are mutually
    // exclusive either PTB or non PTB annotations
    def PTB_FN_Antns = ["f.penn","f.fn","f.fntok","f.ptb","f.ptbtok"] as HashSet<String>

    def nonPTB_FN_Antns = [ "f.sentences","f.biber","f.c5","f.c7","f.cb",
                            "f.content","f.event","f.hepple","f.logical",
                            "f.mpqa","f.nc","f.ne","f.none",
                            "f.slate_coref","f.vc"] as HashSet<String>

    /** Processor object used to convert GrAF files into the CONLL format. */
    XMLProcessor processor
    ResourceHeader header
    Index index

    void setAcceptable (Set<String> accepted){
        Acceptable = accepted
    }

    /**
     * Check if the annotations provided are acceptable for conll processing
     * @param antnArray - ArrayList<String> of annotations
     * @return A boolean response if the annotations passed in are acceptable for
     * conll processing.
     */
    boolean validAnnotations (ArrayList<String> annotations, HashSet<String> acceptable) {
        for (String annotation : annotations)
        {
            if (!acceptable.contains(annotation)) {
                return false
            }
        }
        return true
    }

    /**
     * Split the comma separated string into an ArrayList<String>
     * @param antnString - The comma separated string of selected annotations
     * @return An ArrayList<String> of the selected annotations
     */
    List<String> parseAnnotations(String antnString)
    {
        if (antnString == "")
        {
            return Acceptable.toList()
        } else {
            // The collect closure will prepend the string 'f.' to every element in the
            // list.
            def retArray = antnString.split(',').collect { "f." + it.trim() }
            return retArray
        }
    }

    /**
     * Function called to process a document
     * @param annotations The list of annotations to process the doc with
     * @param docID The document ID
     * @return An error indicating an invalid parameter or the document processed with
     *         the annotations
     */
    @GET
    Response process(@QueryParam('annotations') String annotations,
                     @QueryParam('id') String docID)
    {
        if (annotations == null) {
            return Response.serverError().entity(MESSAGES.NO_ANNOTATIONS)
        }
        if (docID == null) {
            return Response.serverError().entity(MESSAGES.NO_DOC_ID)
        }

        logger.debug("Attempting to process {}", docID)



        List<String> selectedAnnotations = parseAnnotations(annotations)
        File inputFile = index.get(docID)
        if (inputFile == null) {
            logger.debug("No document with id {}", docID)
            return Response.serverError().entity(MESSAGES.INVALID_ID).build();
        }

        if (!validAnnotations(selectedAnnotations, Acceptable)) {
            logger.debug("Invalid annotations selected.")
            return Response.serverError().entity(MESSAGES.INVALID_TYPE).build()
        }

        // TODO The output file should be placed in a known location (e.g. /tmp/conll-processor) that
        // can be cleaned/deleted on startup to prevent filling the disk if the service crashes.

        File outputFile = File.createTempFile("conll-rs", "txt");

        try {
            processor.reset()
            processor.resourceHeader = header
            processor.annotationTypes = new AnnotationConfig(selectedAnnotations)
            processor.initialize()
            processor.process(inputFile, outputFile);
            return Response.ok(outputFile.text).build();
        }
        catch (ProcessorException e)
        {
            logger.error("Unable to process document.", e)
            return Response.status(500).entity(e.message).build()
        }
        finally {
            // Delete the temporary output file. This is best done in a finally block
            // so it gets deleted regardless of how execution exits the try block.
            if (!outputFile.delete()) {
                logger.error("Unable to delete temporary file {}", outputFile.path)
                // Hopefully we can delete the file when the service exits.
                outputFile.deleteOnExit();
            }
        }
    }
}
