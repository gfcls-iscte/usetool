/*
 * USE - UML based specification environment
 * Copyright (C) 1999-2004 Mark Richters, University of Bremen
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
package org.tzi.use.parser.use;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tzi.use.parser.Context;
import org.tzi.use.parser.SemanticException;
import org.tzi.use.uml.al.ALAction;
import org.tzi.use.uml.al.ALActionList;



/**
 * @author green
 */
public class ASTALActionList extends ASTALAction {

    private List fActions = new ArrayList();
    
    public void add(ASTALAction action) {
        fActions.add(action);
    }
    
    public ALAction gen(Context ctx) throws SemanticException {
        ALActionList list = new ALActionList();
        for (Iterator it = fActions.iterator(); it.hasNext();) {
            ASTALAction astAction = (ASTALAction) it.next();
            ALAction action = astAction.gen(ctx);
            list.add(action);
        }
        return list;
    }

}
