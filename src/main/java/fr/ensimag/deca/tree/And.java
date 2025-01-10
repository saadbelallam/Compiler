package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel, boolean isWhileOrIf) {
        if (isWhileOrIf) {
        // Generate code for first condition
        // If first condition is false, jump directly to falseLabel
        Label endLabel = new Label("E_Fin_" + compiler.getMangerL().newLabel());
        getLeftOperand().codeGenCondition(compiler, endLabel, false);
        
        // If we get here, first condition was true
        // Evaluate second condition
        getRightOperand().codeGenCondition(compiler, falseLabel, true);
        compiler.addLabel(endLabel);


        } else {

             getLeftOperand().codeGenCondition(compiler, falseLabel, false);
            getRightOperand().codeGenCondition(compiler, falseLabel, false);
        }
    }

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        Label endLabel = new Label("and_end_" + compiler.getMangerL().newLabel());
        
        // Evaluate first operand
        getLeftOperand().codeGenVal(compiler, destReg);
        
        // If first operand is false (0), skip second operand
        compiler.addInstruction(new CMP(0, destReg));
        compiler.addInstruction(new BEQ(endLabel));
        
        // Evaluate second operand if first was true
        getRightOperand().codeGenVal(compiler, destReg);
        
        // Result is now in destReg
        compiler.addLabel(endLabel);
    }   


    @Override
    protected void branchInstruction(DecacCompiler compiler, Label label) {
        
        compiler.addInstruction(new BEQ(label));
    }
    
    @Override
    protected int getShortCircuitValue() {
        return 0; 
    } 
   
}
