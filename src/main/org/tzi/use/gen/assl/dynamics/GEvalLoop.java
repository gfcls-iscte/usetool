/*
 * This is source code of the Snapshot Generator, an extension for USE
 * to generate (valid) system states of UML models.
 * Copyright (C) 2001 Joern Bohling, University of Bremen
 *
 * About USE:
 *   USE - UML based specification environment
 *   Copyright (C) 1999,2000,2001 Mark Richters, University of Bremen
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.tzi.use.gen.assl.dynamics;

import org.tzi.use.gen.assl.statics.GLoop;
import org.tzi.use.uml.ocl.value.Value;
import org.tzi.use.uml.ocl.value.SequenceValue;

import java.util.ListIterator;
import java.util.ArrayList;

/**
 * March 22th 2001 
 * @author  Joern Bohling
 */
class GEvalLoop extends GEvalInstruction
    implements IGCaller {
    private GLoop fInstr;
    private IGCaller fCaller;
    private ListIterator fSeqIterator;

    public GEvalLoop(GLoop instr) {
        fInstr = instr;
    }

    public void eval(GConfiguration conf,
                     IGCaller caller,
                     IGCollector collector) throws GEvaluationException {
        collector.detailPrintWriter().println("evaluating `" + fInstr + "'");
        fCaller = caller;
        fSeqIterator = null;
        GCreator.createFor(fInstr.sequenceInstr()).eval( conf, this, collector );
    }

    public void feedback(GConfiguration conf,
                         Value value,
                         IGCollector collector ) throws GEvaluationException {
        if (fSeqIterator==null) {
            if (value.isUndefined())
                collector.invalid(
                                  buildCantExecuteMessage( fInstr, fInstr.sequenceInstr()) );
            else {
                fSeqIterator=((ArrayList) ((SequenceValue)value).collection())
                    .listIterator();
            }
        }
        if (fSeqIterator!=null) {
            if (fSeqIterator.hasNext()) {
                Value seqElem = (Value) fSeqIterator.next();
                collector.detailPrintWriter().println(fInstr.decl().name() + ":=" + seqElem );
                conf.varBindings().push(fInstr.decl().name(), seqElem);
                GCreator.createFor(fInstr.instructionList())
                    .eval( conf, this, collector );
                conf.varBindings().pop();
                fSeqIterator.previous();
            } else {
                fCaller.feedback( conf, null, collector );
            }
        }
    }

    public String toString() {
        return "GEvalLoop";
    }

}

