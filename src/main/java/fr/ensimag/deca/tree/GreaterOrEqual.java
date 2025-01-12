package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;


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

    

    
   /* @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel, boolean isWhileOrIf) {
        // Get registers for operands
        GPRegister leftReg = compiler.getMangerR().allocateRegister();
        GPRegister rightReg = compiler.getMangerR().allocateRegister();
        
        try {
            // Generate code for operands
            getLeftOperand().codeGenVal(compiler, leftReg);
            getRightOperand().codeGenVal(compiler, rightReg);
            
            // Compare left >= right
            compiler.addInstruction(new CMP(rightReg, leftReg));
            
            // Branch if strictly less than
            // For if/while, we branch to false label if condition is false (i.e., <)
            // For other contexts, we branch if condition is true (i.e., >=)
            compiler.addInstruction(isWhileOrIf ? new BGE(falseLabel) : new BLT(falseLabel));
            //compiler.addInstruction(isWhileOrIf ? new BGE(falseLabel) : new BLT(falseLabel));
            
        } finally {
            // Free registers
            compiler.getMangerR().freeRegister(leftReg);
            compiler.getMangerR().freeRegister(rightReg);
        }
    } */

    /* @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel, boolean jump) {
        ManagerOfRegisters regManager = compiler.getMangerR();
        System.err.println("regManager.getnbOfUsedRegisiters()  >= "+ regManager.getnbOfUsedRegisiters());
            System.err.println("regManager.has  >= "+ compiler.getMangerR().hasAvailableRegisters());
        
        if (regManager.getnbOfUsedRegisiters() <= 12) {
            // Allocate registers normally if two are available
            GPRegister leftReg = regManager.allocateRegister();
            GPRegister rightReg = regManager.allocateRegister();
            
            try {
                // Generate code for operands
                getLeftOperand().codeGenVal(compiler, leftReg);
                getRightOperand().codeGenVal(compiler, rightReg);

                // Compare and branch
                compiler.addInstruction(new CMP(rightReg, leftReg));
                compiler.addInstruction(jump ? new BGE(falseLabel) : new BLT(falseLabel));


            } finally {
                // Free allocated registers
                regManager.freeRegister(leftReg);
                regManager.freeRegister(rightReg);
            }
        } else {
            // Fallback: Use MaxRegCase with stack-based approach
            GPRegister rightReg = regManager.getScratchRegister(1); // Use scratch register R0
            regManager.MaxRegCase(compiler, getLeftOperand(), getRightOperand(), rightReg);
            // Perform the comparison and branching
            compiler.addInstruction(new CMP(rightReg,Register.getR(0)));
            compiler.addInstruction(jump ? new BGE(falseLabel) : new BLT(falseLabel));


        }
    }
 */
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
            compiler.addInstruction(new SGE(destReg));  // Set if Greater or Equal
            
        } finally {
            compiler.getMangerR().freeRegister(rightReg);
        }
    } */
   @Override
    public  void addBranchInstruction(DecacCompiler compiler, Label destLabel,boolean  jump){
            compiler.addInstruction(jump ? new BGE(destLabel) : new BLT(destLabel));
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
