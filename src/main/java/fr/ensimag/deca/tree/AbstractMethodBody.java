package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;

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
   // public abstract int getStackSpace();
    
    /**
     * Gets the number of local variables declared in this method
     * @return Number of local variables
     */
    //public abstract int getLocalVarCount();
    
    /**
     * Checks if this method body contains a return statement
     * @return true if method contains return, false otherwise
     */
    //public abstract boolean hasReturn();
}
