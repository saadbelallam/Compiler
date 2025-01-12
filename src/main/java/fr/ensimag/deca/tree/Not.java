package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Not extends AbstractUnaryExpr {
        private static final Logger LOG = Logger.getLogger(AbstractUnaryExpr.class);


    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify not: start");
        Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!type.isBoolean()) {
            throw new ContextualError("Operator '!' only on booleans", getLocation());
        }
        setType(type);
        LOG.debug("verify not: end");
        return type;
    }

    
    @Override
    protected String getOperatorName() {
        return "!";
    }

   /*  @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        getOperand().codeGenVal(compiler, destReg);
        
        // Compute NOT by subtracting from 1:
        // Nhsbo x b x = 1 - x (since x is either 0 or 1)
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), destReg));
        compiler.addInstruction(new SUB(destReg, destReg));
    } */

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        // Create labels for control flow
        Label falseLabel = new Label("not_false_" + compiler.getMangerL().newLabel());
        Label endLabel = new Label("not_end_" + compiler.getMangerL().newLabel());
        
        // Load true (1) by default
        compiler.addInstruction(new LOAD(1, destReg));
        
        // Use codeGenCondition - if condition is true, jump to falseLabel
        // Note: we pass !jump to invert the condition
        codeGenCondition(compiler, falseLabel, false);
        
        // Skip the false case if condition was false (meaning NOT is true)
        compiler.addInstruction(new BRA(endLabel));
        
        // False case - load 0
        compiler.addLabel(falseLabel);
        compiler.addInstruction(new LOAD(0, destReg));
        
        compiler.addLabel(endLabel);
    }


    @Override
    public void codeGenCondition(DecacCompiler compiler, Label label, boolean jump) {
        // reverse the contion by the jump
        getOperand().codeGenCondition(compiler, label, !jump);
    }
}
