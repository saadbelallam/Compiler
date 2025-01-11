package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;


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


    /* @Override
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
    } */


    /* @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel, boolean jump) {
        ManagerOfRegisters regManager = compiler.getMangerR();
            System.err.println("regManager.getnbOfUsedRegisiters()  < "+ regManager.getnbOfUsedRegisiters());
            System.err.println("regManager.has  < "+ compiler.getMangerR().hasAvailableRegisters());
        
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
                            compiler.addInstruction(jump ? new BLT(falseLabel) : new BGE(falseLabel));


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
            //compiler.addInstruction(jump ? new BLT(falseLabel) : new BGE(falseLabel));


        }
    } */

    

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
    public  void addBranchInstruction(DecacCompiler compiler, Label destLabel,boolean  jump){
        compiler.addInstruction(jump ? new BLT(destLabel) : new BGE(destLabel));

    }
    
}
