package org.anc.processor.xml

import org.anc.index.core.IndexImpl
import org.xces.graf.io.dom.ResourceHeader

import javax.ws.rs.Path

/**
 * Created by danmccormack on 12/9/14.
 */
@Path("/xml")
class XMLProcessor extends AbstractProcessor{


    public XMLProcessor() {
        processor = new org.anc.tool.xml.XMLProcessor();
        //TODO This path should not be hard coded.
        header = new ResourceHeader(new File("/var/corpora/MASC-3.0.0/resource-header.xml"))
        index = new IndexImpl().loadMasc3Index()
    }
}
