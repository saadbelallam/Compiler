package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.OPP;
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
        if (getType().isFloat()) {
            
            getLeftOperand().codeGenVal(compiler, destReg);
            
    
            GPRegister rightReg = compiler.getMangerR().allocateRegister();
            
            try {
                getRightOperand().codeGenVal(compiler, rightReg);
                
                // Negate right operand
                compiler.addInstruction(new OPP(rightReg, rightReg));
                
                // Add negated value to left operand
                compiler.addInstruction(new ADD(rightReg, destReg));
            } finally {
                compiler.getMangerR().freeRegister(rightReg);
            }
        } else {
            // SUB
            getLeftOperand().codeGenVal(compiler, destReg);
            GPRegister rightReg = compiler.getMangerR().allocateRegister();
            
            try {
                getRightOperand().codeGenVal(compiler, rightReg);
                compiler.addInstruction(new SUB(rightReg, destReg));
            } finally {
                compiler.getMangerR().freeRegister(rightReg);
            }
        }
    }
    
}
