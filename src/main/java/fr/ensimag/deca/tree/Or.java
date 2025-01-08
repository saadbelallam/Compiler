package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        Label trueLabel = new Label("true_" + compiler.getMangerL().newLabel());
        Label endLabel = new Label("end_" + compiler.getMangerL().newLabel());
        
        // Evaluate left operandù
        getLeftOperand().codeGenCondition(compiler, trueLabel, false) ;       
        // Evaluate right operand
        getRightOperand().codeGenCondition(compiler, trueLabel, false);
        
        // If we get here, both conditions were false
        GPRegister tmpReg = compiler.getMangerR().allocateRegister();
        compiler.addInstruction(new LOAD(0, tmpReg));
        compiler.addInstruction(new BRA(endLabel));
        
        // True case
        compiler.addLabel(trueLabel);
        compiler.addInstruction(new LOAD(1, tmpReg));
        
        compiler.addLabel(endLabel);
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Label label, boolean jump) {
        if (jump) {
            // Jump if either operand is true
            getLeftOperand().codeGenCondition(compiler, label, false);
            getRightOperand().codeGenCondition(compiler, label, false);
        } else {
            // Jump if both operands are false
            Label nextTest = new Label("or_next_" + compiler.getMangerL().newLabel());
            getLeftOperand().codeGenCondition(compiler, nextTest, true);
            compiler.addInstruction(new BRA(label));
            compiler.addLabel(nextTest);
            getRightOperand().codeGenCondition(compiler, label, true);
        }
    }
}
