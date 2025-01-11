package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SLT;


/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<";
    }


    @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel, boolean isWhileOrIf) {
        // Get registers for operands
        GPRegister leftReg = compiler.getMangerR().allocateRegister();
        GPRegister rightReg = compiler.getMangerR().allocateRegister();
        
        try {
            // Generate code for operands
            getLeftOperand().codeGenVal(compiler, leftReg);
            getRightOperand().codeGenVal(compiler, rightReg);
            
            // Compare left < right
            compiler.addInstruction(new CMP(rightReg, leftReg));
            
            // Branch if not less than (>=)
            // For if/while, we branch to false label if condition is false
            // For other contexts, we branch if condition is true
            compiler.addInstruction(isWhileOrIf ? new BLT(falseLabel) : new BGE(falseLabel));
            //compiler.addInstruction(isWhileOrIf ? new BGE(falseLabel) : new BLT(falseLabel));
            
        } finally {
            // Free registers
            compiler.getMangerR().freeRegister(leftReg);
            compiler.getMangerR().freeRegister(rightReg);
        }
    }

    /* @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        // Get register for right operand
        GPRegister rightReg = compiler.getMangerR().allocateRegister();
        
        try {
            // Generate code for operands
            getLeftOperand().codeGenVal(compiler, destReg);
            getRightOperand().codeGenVal(compiler, rightReg);
            
            // Compare operands
            compiler.addInstruction(new CMP(rightReg, destReg));
            
            // Set result based on comparison
            compiler.addInstruction(new SLT(destReg));  // Set if Less Than
            
        } finally {
            compiler.getMangerR().freeRegister(rightReg);
        }
    } */



    @Override
    public  void generateSpecificComparison(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new SLT(register));
    }
    
}
