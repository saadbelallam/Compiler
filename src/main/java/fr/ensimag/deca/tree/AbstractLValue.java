package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.ima.pseudocode.RegisterOffset;






/**
 * Left-hand side value of an assignment.
 * 
 * @author gl24
 * @date 01/01/2025
 */
public abstract class AbstractLValue extends AbstractExpr {
     public abstract RegisterOffset getMemoryLocation();
     public abstract ExpDefinition getExpDefinition();     
     
}
