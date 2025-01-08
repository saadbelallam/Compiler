package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel,boolean isWhileOrIf) {
        // Generate code for operands
        GPRegister leftReg = compiler.getMangerR().allocateRegister();
        getLeftOperand().codeGenVal(compiler, leftReg);
        
        GPRegister rightReg = compiler.getMangerR().allocateRegister();
        getRightOperand().codeGenVal(compiler, rightReg);
        

        compiler.addInstruction(new CMP(rightReg, leftReg));
        // Compare and branch if not equal
        if (isWhileOrIf) {
            compiler.addInstruction(new BNE(falseLabel));
        } else {
            compiler.addInstruction(new BEQ(falseLabel));
        }            
        // Free registers
        compiler.getMangerR().freeRegister(leftReg);
        compiler.getMangerR().freeRegister(rightReg);
}

}
