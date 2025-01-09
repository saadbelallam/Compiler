package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.CMP;


/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister register) {
        GPRegister tempReg = compiler.getMangerR().allocateRegister();
        try {
            //System.err.println("I'm AbstractyOpineq and : " + getLeftOperand());
            //System.err.println("I'm AbstractyOpineq and : " + getRightOperand());
            getLeftOperand().codeGenVal(compiler, register);
            getRightOperand().codeGenVal(compiler, tempReg);
            compiler.addInstruction(new CMP(tempReg, register));
            generateSpecificComparison(compiler, register);
        } finally {
            compiler.getMangerR().freeRegister(tempReg);
        }
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel, boolean jump) {
       
        getLeftOperand().codeGenCondition(compiler, falseLabel,jump);
       
        getRightOperand().codeGenCondition(compiler, falseLabel,jump);
    }

    

    protected abstract void generateSpecificComparison(DecacCompiler compiler, GPRegister register);

}
