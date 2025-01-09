package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SLE;


/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<=";
    }

    
    @Override
    public  void generateSpecificComparison(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new SLE(register));
    }
    
    

}
