package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Method body containing declarations and instructions
 */
public abstract class AbstractMethodBody extends Tree {
    
    /**
     * Generate code for method body
     * @param compiler The compiler instance
     */
    protected abstract void codeGenBody(DecacCompiler compiler);
    
    /**
     * Gets the maximum stack space needed for this method body
     * This includes:
     * - Local variables
     * - Temporary calculations
     * - Method call parameters
     * @return Number of words needed on stack
     */
   //public abstract int getStackSpace();

   
    
       

    protected abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType)
            throws ContextualError;
    
    /**
     * Gets the number of local variables declared in this method
     * @return Number of local variables
     */
    public abstract int getLocalVarCount();

    public abstract int getMaxExpressionDepth();
    
    /**
     * Checks if this method body contains a return statement
     * @return true if method contains return, false otherwise
     */
    public abstract boolean hasReturn();

    public void setLabelLabel(Label returnLabel){

    }
}
