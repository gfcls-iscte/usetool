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

package org.tzi.use.gen.assl.statics;

import org.tzi.use.uml.mm.MModel;
import org.tzi.use.uml.mm.MAssociation;
import org.tzi.use.uml.mm.MAssociationEnd;
import org.tzi.use.uml.ocl.type.Type;
import org.tzi.use.uml.ocl.type.SequenceType;
import org.tzi.use.uml.ocl.type.TypeFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @see org.tzi.use.gen.assl.statics
 * @author  Joern Bohling
 */
class GMatcherTry_Assoc_LinkendSeqs implements IGInstructionMatcher {
    public String name() {
        return "Try";
    }

    public GInstruction createIfMatches( List param, MModel model ) {
        // param is a list over Strings or GValueInstructions.
        // A containing string is a classname or associationname.

        if (param.isEmpty() )
            return null;

        Object first = param.get(0);
        List rest = param.subList(1, param.size());
    
        if (!matches(first, rest, model))
            return null;

        List ends = new ArrayList();
        Iterator it = rest.iterator();
        while (it.hasNext())
            ends.add(  (GValueInstruction) it.next()  );

        return new GInstrTry_Assoc_LinkendSeqs(
                                               model.getAssociation((String) first), ends );
    }

    private boolean matches( Object first, List rest, MModel model ) {
        if (!(first instanceof String)) 
            return false;
        MAssociation assoc = model.getAssociation((String) first);
        if (assoc == null )
            return false;

        List ends = assoc.associationEnds();

        if (ends.size() != rest.size() )
            return false;
    
        Iterator endsIt = ends.iterator();
        Iterator restIt = rest.iterator();

        while (endsIt.hasNext()) {
            Type endtype = TypeFactory.mkObjectType(
                                                    ((MAssociationEnd) endsIt.next()).cls() );
            Object r = restIt.next();

            if (! (r instanceof GValueInstruction) ||
                ! ((GValueInstruction) r).type()
                .isSubtypeOf( new SequenceType(endtype) ) )
                return false;
        }    

        return true;
    }
}
