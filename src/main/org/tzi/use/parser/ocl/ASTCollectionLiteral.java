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

/* $ProjectHeader: use 0.393 Wed, 16 May 2007 14:10:28 +0200 opti $ */

package org.tzi.use.parser.ocl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tzi.use.parser.Context;
import org.tzi.use.parser.MyToken;
import org.tzi.use.parser.SemanticException;
import org.tzi.use.uml.ocl.expr.ExpBagLiteral;
import org.tzi.use.uml.ocl.expr.ExpInvalidException;
import org.tzi.use.uml.ocl.expr.ExpSequenceLiteral;
import org.tzi.use.uml.ocl.expr.ExpSetLiteral;
import org.tzi.use.uml.ocl.expr.Expression;
import org.tzi.use.util.StringUtil;

/**
 * Node of the abstract syntax tree constructed by the parser.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public class ASTCollectionLiteral extends ASTExpression {
    private MyToken fToken;
    private List fItems;    // (ASTCollectionItem)
    private boolean fHasRanges;

    public ASTCollectionLiteral(MyToken token) {
        fToken = token;
        fItems = new ArrayList();
    }

    public void addItem(ASTCollectionItem item) {
        fItems.add(item);
        if (item.fSecond != null )
            fHasRanges = true;
    }

    public Expression gen(Context ctx) throws SemanticException {
        String opname = "mk" + fToken.getText();
        if (fHasRanges )
            opname += "Range";

        // produce argument list
        ArrayList args = new ArrayList();
        Iterator itemIter = fItems.iterator();
        while (itemIter.hasNext() ) {
            ASTCollectionItem item = (ASTCollectionItem) itemIter.next();
            args.add(item.fFirst);
            // if there is at least one range item, we generate all
            // arguments as ranges
            if (fHasRanges ) {
                if (item.fSecond == null )
                    args.add(item.fFirst);
                else
                    args.add(item.fSecond);
            }
        }

        Expression[] eArgs = new Expression[args.size()];
        for (int i = 0; i < args.size(); i++)
            eArgs[i] = ((ASTExpression) args.get(i)).gen(ctx);
        try {
            if (opname.equals("mkSet") )
                return new ExpSetLiteral(eArgs);
            else if (opname.equals("mkBag") )
                return new ExpBagLiteral(eArgs);
            else if (opname.equals("mkSequence") )
                return new ExpSequenceLiteral(eArgs);
            else
                return genStdOperation(ctx, fToken, opname, eArgs);
        } catch (ExpInvalidException ex) {
            throw new SemanticException(fToken, ex);
        }
    }

    public String toString() {
        return "(" + fToken + " " + 
            StringUtil.fmtSeq(fItems.iterator(), " ") + ")";
    }
}
