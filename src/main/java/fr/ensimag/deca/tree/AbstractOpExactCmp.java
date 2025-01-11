package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ManagerOfRegisters;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;


/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel, boolean jump) {
        ManagerOfRegisters regManager = compiler.getMangerR();
    
        if (regManager.getnbOfUsedRegisiters()<= 12) {
            
            // Allocate registers normally if two are available
            GPRegister leftReg = regManager.allocateRegister();
            GPRegister rightReg = regManager.allocateRegister();
            
            try {
                // Generate code for operands
                getLeftOperand().codeGenVal(compiler, leftReg);
                getRightOperand().codeGenVal(compiler, rightReg);

                // Compare and branch
                compiler.addInstruction(new CMP(rightReg, leftReg));
                //compiler.addInstruction(jump ? new BNE(falseLabel) : new BEQ(falseLabel));
                addBranchInstruction(compiler, falseLabel, jump);
                
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
            //compiler.addInstruction(jump ? new BNE(falseLabel) : new BEQ(falseLabel));
            addBranchInstruction(compiler, falseLabel, jump);
        }
    }
   // actually we don't use it but what if
    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        // Create labels for control flow
        Label falseLabel = new Label("exact_cmp_false_" + compiler.getMangerL().newLabel());
        Label endLabel = new Label("exact_cmp_end_" + compiler.getMangerL().newLabel());
        
        // Load true (1) by default
        compiler.addInstruction(new LOAD(1, destReg));
        
        // Use codeGenCondition - if comparison is false, jump to falseLabel
        codeGenCondition(compiler, falseLabel, false);
        
        // Skip the false case if comparison was true
        compiler.addInstruction(new BRA(endLabel));
        
        // False case - load 0
        compiler.addLabel(falseLabel);
        compiler.addInstruction(new LOAD(0, destReg));
        
        compiler.addLabel(endLabel);
    }

}
