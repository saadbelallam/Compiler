package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
/**
 * @author gl24
 * @date 01/01/2025
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }

    
    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        // Case where e2 is undefined and n=MAX
        if (getRightOperand().getType().isVoid() && compiler.getMangerR().getnbOfUsedRegisiters() == 14) {
            getLeftOperand().codeGenVal(compiler, destReg);
            // Save result
            compiler.addInstruction(new PUSH(destReg));
            // Generate code for right operand
            GPRegister Rn = Register.getR(0);
            getRightOperand().codeGenVal(compiler, Rn);
            
            // Restore saved value
            compiler.addInstruction(new POP(destReg));
            // Perform addition
            compiler.addInstruction(new ADD(Rn, destReg));
        } 
        else {
            getLeftOperand().codeGenVal(compiler, destReg);
 
            GPRegister rightReg = compiler.getMangerR().allocateRegister();
            getRightOperand().codeGenVal(compiler, rightReg);
           
            // addition
            compiler.addInstruction(new ADD(rightReg, destReg));
            compiler.getMangerR().freeRegister(rightReg);
        }
    }}




    
