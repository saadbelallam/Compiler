package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SNE;

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
    public void codeGenCondition(DecacCompiler compiler, Label falseLabel, boolean branchOnTrue) {
        // Create label for second condition
        //System.err.println("I got Here");
        
        if (branchOnTrue) {
                // Code(C1 || C2, true, E) ≡
                // Code(C1, true, E)
                // Code(C2, true, E) you sure ?
               //System.err.println("I got Here if true");

               getLeftOperand().codeGenCondition(compiler, falseLabel, true);
               getRightOperand().codeGenCondition(compiler, falseLabel, true);
        
        } else {
        //System.err.println("I got Here if false");
                
                // Code(C1 || C2, false, E) ≡
                // Code(!(!C1 && !C2), false, E)
                Label endLabel = new Label("E_Fin_" + compiler.getMangerL().newLabel());
                getLeftOperand().codeGenCondition(compiler, endLabel,true);
                getRightOperand().codeGenCondition(compiler,falseLabel,false);
                compiler.addLabel(endLabel);
            }
        
    }

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        Label endLabel = new Label("or_end_" + compiler.getMangerL().newLabel());
        Label trueLabel = new Label("or_true_" + compiler.getMangerL().newLabel());
        
        getLeftOperand().codeGenVal(compiler, destReg);
        
        compiler.addInstruction(new CMP(1, destReg));
        compiler.addInstruction(new BEQ(trueLabel));
        
        getRightOperand().codeGenVal(compiler, destReg);
        
        compiler.addInstruction(new CMP(0, destReg));
        compiler.addInstruction(new SNE(destReg));
        compiler.addInstruction(new BRA(endLabel));
        
        compiler.addLabel(trueLabel);
        compiler.addInstruction(new LOAD(1, destReg));
        
        compiler.addLabel(endLabel);
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
