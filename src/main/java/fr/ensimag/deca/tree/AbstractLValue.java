package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.ima.pseudocode.GPRegister;
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
    protected void codeGenStore(DecacCompiler compiler, GPRegister srcReg){

    }
     @Override
     public int getMaxExpressionDepth() {
        // Literals just need space for their value
        return 1;
    }    
     
}
