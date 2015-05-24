package org.anc.processor.xml

import org.anc.index.core.IndexImpl
import org.xces.graf.io.dom.ResourceHeader
import org.anc.processor.Abstract.AbstractProcessor

import javax.ws.rs.Path

/**
 * Created by danmccormack on 12/9/14.
 */
@Path("/xml")
class XMLProcessor extends AbstractProcessor{


    public XMLProcessor() {
        super(["f.penn", "f.sentences", "f.biber", "f.c5", "f.c7", "f.cb",
               "f.content", "f.event", "f.hepple", "f.logical",
               "f.mpqa", "f.nc", "f.ne", "f.none","f.slate_coref",
               "f.vc", "f.ptb", "f.ptbtok", "f.fn", "f.fntok", "f.s"] as List<String>)
        processor = new org.anc.tool.xml.XMLProcessor();
    }
}
