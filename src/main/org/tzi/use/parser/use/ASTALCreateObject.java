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

import org.tzi.use.parser.Context;
import org.tzi.use.parser.MyToken;
import org.tzi.use.parser.SemanticException;
import org.tzi.use.uml.al.ALAction;
import org.tzi.use.uml.al.ALCreateObject;
import org.tzi.use.uml.mm.MClass;

import antlr.Token;

/**
 * @author green
 */
public class ASTALCreateObject extends ASTALAction {

    private MyToken fVar;
    private MyToken fCls;
    
    public ASTALCreateObject(Token var, Token cls) {
        fVar = (MyToken)var;
        fCls = (MyToken)cls;
    }

    public ALAction gen(Context ctx) throws SemanticException {
        MClass cls = ctx.model().getClass(fCls.getText());
        if (cls == null) {
            throw new SemanticException(fCls,"no such class");
        }
        return new ALCreateObject(fVar.getText(),cls);
    }
}
