package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.QUO;


/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }



    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister register){
        // allocation of the temp register
        GPRegister tempReg = compiler.getMangerR().allocateRegister();
        
        //evaluating the nemo
        getRightOperand().codeGenVal(compiler, register);

        getLeftOperand().codeGenVal(compiler, tempReg);
        
        //evaluating the num
        compiler.addInstruction(new QUO(tempReg, register));
    }
}
