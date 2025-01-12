package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ManagerOfRegisters;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;


/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
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
}
