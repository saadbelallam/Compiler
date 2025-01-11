package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

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

        if (branchOnTrue) {
                // Code(C1 || C2, true, E) ≡
                // Code(C1, true, E)
                // Code(C2, true, E) you sure ?

               getLeftOperand().codeGenCondition(compiler, falseLabel, true);
               getRightOperand().codeGenCondition(compiler, falseLabel, true);
        
        } else {
      
                // Code(C1 || C2, false, E) ≡
                // Code(!(!C1 && !C2), false, E)
                Label endLabel = new Label("E_Fin_" + compiler.getMangerL().newLabel());
                getLeftOperand().codeGenCondition(compiler, endLabel,true);
                getRightOperand().codeGenCondition(compiler,falseLabel,false);
                compiler.addLabel(endLabel);
            }  
    }
}
