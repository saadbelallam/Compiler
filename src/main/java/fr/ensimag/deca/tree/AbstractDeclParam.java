package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Class declaration.
 *
 * @author gl24
 * @date 01/01/2025
 */


// this class was made to deal at the first place with params of the methode ! this is not the final implementation

public abstract class AbstractDeclParam extends Tree {

    // this functions are subject to change at any time !!! i's just a prototype inspired by the other functions 
    
    protected abstract void verifyParamBody(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError;
    
    
    // Attribute LB to param
    protected abstract void codeGenParam(DecacCompiler compiler);
}