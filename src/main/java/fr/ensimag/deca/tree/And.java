package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;


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
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        Label endLabel = new Label("end.bool." + compiler.getMangerL().newLabel());
        Label shortCircuitLabel = new Label("short_circuit." + compiler.getMangerL().newLabel());
        
        // Evaluate left operand
        getLeftOperand().codeGenVal(compiler, destReg);
        
        // Branch based on left operand
        branchInstruction(compiler, shortCircuitLabel);
        
        // Evaluate right operand if needed
        getRightOperand().codeGenVal(compiler, destReg);
        compiler.addInstruction(new BRA(endLabel));
        
        compiler.addLabel(shortCircuitLabel);
        // valeur de ShortCiruitValue O pour AND , and 1 for Or  
        compiler.addInstruction(new LOAD(getShortCircuitValue(), destReg));
        
        compiler.addLabel(endLabel);}
    

    @Override
    protected void branchInstruction(DecacCompiler compiler, Label label) {
        
        compiler.addInstruction(new BEQ(label));
    }
    
    @Override
    protected int getShortCircuitValue() {
        return 0; 
    }
}
