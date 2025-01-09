package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
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
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        Label endLabel = new Label("end.bool." + compiler.getMangerL().newLabel());
        Label shortCircuitLabel = new Label("short_circuit." + compiler.getMangerL().newLabel());
        
        // Evaluate left operand

        System.err.println("ERROR Im HERE and my leftOp is "+ getLeftOperand() + "My right OP is " + getRightOperand());
        getLeftOperand().codeGenVal(compiler, destReg);
        
        // Branch based on left operand
        branchInstruction(compiler, shortCircuitLabel);
        
        // Evaluate right operand if needed
        System.err.println("ERROR Im HERE ");

        getRightOperand().codeGenVal(compiler, destReg);
        compiler.addInstruction(new BRA(endLabel));
        System.err.println("i went iwout  ");
        
        compiler.addLabel(shortCircuitLabel);
        // valeur de ShortCiruitValue O pour AND , and 1 for Or  
        compiler.addInstruction(new LOAD(getShortCircuitValue(), destReg));
        
        compiler.addLabel(endLabel);}
    

    @Override
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel, boolean jump) {
       
        getLeftOperand().codeGenCondition(compiler, falseLabel,jump);
       
        getRightOperand().codeGenCondition(compiler, falseLabel,jump);
    }

    @Override
    protected void branchInstruction(DecacCompiler compiler, Label label) {
        compiler.addInstruction(new BNE(label));
    }

    @Override
    protected int getShortCircuitValue() {
        return 1;    
    }


    
}
