package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }   

    /* @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel,boolean isWhileOrIf) {
        // Generate code for operands
        GPRegister leftReg = compiler.getMangerR().allocateRegister();
        getLeftOperand().codeGenVal(compiler, leftReg);
        
        GPRegister rightReg = compiler.getMangerR().allocateRegister();
        getRightOperand().codeGenVal(compiler, rightReg);
        
        // Compare and branch if equal
        compiler.addInstruction(new CMP(rightReg, leftReg));
        if (isWhileOrIf) {
            compiler.addInstruction(new BEQ(falseLabel));
        } else {
            compiler.addInstruction(new BNE(falseLabel));
        }  
        // Free registers
        compiler.getMangerR().freeRegister(leftReg);
        compiler.getMangerR().freeRegister(rightReg);} */
    

    /* @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
       
        GPRegister rightReg = compiler.getMangerR().allocateRegister();
        
        try {
            
            getLeftOperand().codeGenVal(compiler, destReg);
            getRightOperand().codeGenVal(compiler, rightReg);
            
            compiler.addInstruction(new CMP(rightReg, destReg));
            compiler.addInstruction(new SEQ(destReg));
        } finally {
            compiler.getMangerR().freeRegister(rightReg);
        }
    } */


    /* @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel, boolean jump) {
        GPRegister leftReg = compiler.getMangerR().allocateRegister();
        GPRegister rightReg = compiler.getMangerR().allocateRegister();
        
        try {
            // Generate code for operands
            getLeftOperand().codeGenVal(compiler, leftReg);
            getRightOperand().codeGenVal(compiler, rightReg);
            
            // Compare and branch if not equal
            compiler.addInstruction(new CMP(rightReg, leftReg));
            compiler.addInstruction(jump ? new BEQ(falseLabel) : new BNE(falseLabel));
        } finally {
            compiler.getMangerR().freeRegister(leftReg);
            compiler.getMangerR().freeRegister(rightReg);
        }
    }  */

   /* @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel, boolean jump) {
        ManagerOfRegisters regManager = compiler.getMangerR();
        System.err.println("regManager.getnbOfUsedRegisiters()  == "+ regManager.getnbOfUsedRegisiters());
            System.err.println("regManager.has  == "+ compiler.getMangerR().hasAvailableRegisters());
        
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
                compiler.addInstruction(jump ? new BEQ(falseLabel) : new BNE(falseLabel));
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
            //compiler.addInstruction(jump ? new BEQ(falseLabel) : new BNE(falseLabel));
        }
    } */
    
    /* @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        GPRegister rightReg = compiler.getMangerR().allocateRegister();
        
        try {
            // Generate code for operands
            getLeftOperand().codeGenVal(compiler, destReg);
            getRightOperand().codeGenVal(compiler, rightReg);
            
            // Compare values
            compiler.addInstruction(new CMP(rightReg, destReg));
            compiler.addInstruction(new SEQ(destReg));  // Set if Equal
        } finally {
            compiler.getMangerR().freeRegister(rightReg);
        }
    } */

    @Override
    public void addBranchInstruction(DecacCompiler compiler, Label destLabel,boolean  jump){
        compiler.addInstruction(jump ? new BEQ(destLabel) : new BNE(destLabel));
    }
    }

    
