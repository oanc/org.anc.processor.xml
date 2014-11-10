package org.anc.processor.xml

import org.anc.conf.AnnotationConfig
import org.anc.conf.AnnotationType
import org.anc.index.api.Index
import org.anc.index.IndexFactory

/**
 * Created by danmccormack on 10/7/14.
 */

Index index = IndexFactory.getMasc3Index()
index.keys().each { key ->
    File file = index.get(key)
    println file.path
}

AnnotationConfig config = new AnnotationConfig()
/*PTB & FrameNet
config.add(AnnotationType.PENN)
config.add(AnnotationType.FN)
config.add(AnnotationType.FNTOK)
*/
//Annotations that rely on Penn tokens
config.add(AnnotationType.SENTENCES)
config.add(AnnotationType.BIBER)
config.add(AnnotationType.C5)
config.add(AnnotationType.C7)
config.add(AnnotationType.CB)
config.add(AnnotationType.CONTENT)
config.add(AnnotationType.EVENT)
config.add(AnnotationType.HEPPLE)
config.add(AnnotationType.LOGICAL)
config.add(AnnotationType.MPQA)
config.add(AnnotationType.NC)
config.add(AnnotationType.NE)
config.add(AnnotationType.PTB)
config.add(AnnotationType.NONE)
config.add(AnnotationType.PTBTOK)
config.add(AnnotationType.SLATE_COREF)
config.add(AnnotationType.VC)

