package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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
    GPRegister tempReg = compiler.getMangerR().allocateRegister();
    
    try {
        // Evaluate operands
        getLeftOperand().codeGenVal(compiler, register);
        getRightOperand().codeGenVal(compiler, tempReg);
        
        Label divByZeroError = new Label("div_by_zero_error");
        
        if (getType().isFloat()) {
            
            float EPSILON = 1e-6f;  
            compiler.addInstruction(new LOAD(new ImmediateFloat(EPSILON), compiler.getMangerR().getScratchRegister(0)));
            compiler.addInstruction(new CMP(compiler.getMangerR().getScratchRegister(0), tempReg));
            compiler.addInstruction(new BLE(divByZeroError)); 
        } else {
            
            compiler.addInstruction(new CMP(0, tempReg));
            compiler.addInstruction(new BEQ(divByZeroError));
        }


        if (getType().isFloat()) {
            compiler.addInstruction(new DIV(tempReg, register));
        } else {
            compiler.addInstruction(new QUO(tempReg, register));
        }
        
        // Add error handling label
        compiler.addLabel(divByZeroError);
        // Add error handling code here
        
    } finally {
        compiler.getMangerR().freeRegister(tempReg);
    }
}
}
