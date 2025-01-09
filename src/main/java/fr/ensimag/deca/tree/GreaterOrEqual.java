package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SGE;


/**
 * Operator "x >= y"
 * 
 * @author gl24
 * @date 01/01/2025
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

    
    @Override
    public  void generateSpecificComparison(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new SGE(register));
    }
    
}


    /* @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister register) {
        // Allocate temporary register for comparison
        GPRegister tempReg = compiler.getMangerR().allocateRegister();
        
        try {
            // Evaluate left operand
            getLeftOperand().codeGenVal(compiler, register);
            
            // Evaluate right operand
            getRightOperand().codeGenVal(compiler, tempReg);
            
            // Compare: sets condition codes
            compiler.addInstruction(new CMP(tempReg, register));
            
            // Load result based on comparison
            compiler.addInstruction(new SGT(register)); // Set 1 if greater, 0 otherwise
            
        } finally {
            // Free temporary register
            compiler.getMangerR().freeRegister(tempReg);
        }
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Label label, boolean jump) {
        // Allocate temporary register for comparison
        GPRegister tempReg = compiler.getMangerR().allocateRegister();
        
        try {
            // Evaluate operands
            getLeftOperand().codeGenVal(compiler, tempReg);
            GPRegister reg2 = compiler.getMangerR().allocateRegister();
            getRightOperand().codeGenVal(compiler, reg2);
            
            // Compare operands
            compiler.addInstruction(new CMP(reg2, tempReg));
            
            // Branch based on comparison
            if (jump) {
                compiler.addInstruction(new BGT(label)); // Branch if greater
            } else {
                compiler.addInstruction(new BLT(label)); // Branch if less or equal
            }
            
            compiler.getMangerR().freeRegister(reg2);
        } finally {
            compiler.getMangerR().freeRegister(tempReg);
        }
    }
 */
