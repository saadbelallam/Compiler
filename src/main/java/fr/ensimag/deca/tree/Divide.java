package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;


/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }



   @Override 
   public void codeGenVal(DecacCompiler compiler, GPRegister register) {
    // Allocate temporary register for left operand
    GPRegister tempReg = compiler.getMangerR().allocateRegister();
    
    try {
        // Evaluate left operand (numerator)
        getLeftOperand().codeGenVal(compiler, register);
        
        // Evaluate right operand (denominator)
        getRightOperand().codeGenVal(compiler, tempReg);
        
        // Check for division by zero
        compiler.addInstruction(new CMP(0, tempReg));
        Label divByZeroError = new Label("div_by_zero_error");
        compiler.addInstruction(new BEQ(divByZeroError));
        
        if (getType().isFloat()) {
            // Float division
            compiler.addInstruction(new DIV(tempReg, register));
        } else {
            // Integer division
            compiler.addInstruction(new QUO(tempReg, register));
        }
        
        // Add error handling label
        compiler.addLabel(divByZeroError);
        // Here you can add error handling code for division by zero
        
    } finally {
        // Free the temporary register
        compiler.getMangerR().freeRegister(tempReg);
    }
}}
