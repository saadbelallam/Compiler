package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUB;


/**
 * @author gl24
 * @date 01/01/2025
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        // Case where e2 is undefined and n=MAX
        if (getRightOperand().getType().isVoid() && compiler.getMangerR().getnbOfUsedRegisiters() == 13) {
            // Generate code for left operand
            getLeftOperand().codeGenVal(compiler, destReg);
            // Save result
            compiler.addInstruction(new PUSH(destReg));
            // Generate code for right operand
            GPRegister Rn = Register.getR(14);
            getRightOperand().codeGenVal(compiler, Rn);
            // Restore saved value
            compiler.addInstruction(new POP(destReg));
            // Perform subtraction
            compiler.addInstruction(new SUB(Rn, destReg));
        } 
        // Normal case
        else {
            // Generate code for left operand
            getLeftOperand().codeGenVal(compiler, destReg);
            // Generate code for right operand in next register
            GPRegister rightReg = compiler.getMangerR().allocateRegister();
            getRightOperand().codeGenVal(compiler, rightReg);
            // Perform subtraction
            compiler.addInstruction(new SUB(rightReg, destReg));
            // Free the right register
            compiler.getMangerR().freeRegister(rightReg);
        }
    }

    
}
