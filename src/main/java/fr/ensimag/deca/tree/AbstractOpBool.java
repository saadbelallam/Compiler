package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import org.apache.log4j.Logger;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractBinaryExpr.class);

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify op bool: start");
        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!type1.isBoolean()) {
            throw new ContextualError("Left operand must be of type boolean", this.getLeftOperand().getLocation());
        }
        if (!type2.isBoolean()) {
            throw new ContextualError("Right operand must be of type boolean", this.getRightOperand().getLocation());
        }
        this.setType(type1);
        LOG.debug("verify op bool: end");
        return type1;
    }

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        // Create labels for control flow
        Label falseLabel = new Label("bool_false_" + compiler.getMangerL().newLabel());
        Label endLabel = new Label("bool_end_" + compiler.getMangerL().newLabel());
        
        // Load true (1) by default
        compiler.addInstruction(new LOAD(1, destReg));
        
        // Use codeGenCondition - if condition is false, jump to falseLabel
        codeGenCondition(compiler, falseLabel, false);
        
        // Skip the false case if condition was true
        compiler.addInstruction(new BRA(endLabel));
        
        // False case - load 0
        compiler.addLabel(falseLabel);
        compiler.addInstruction(new LOAD(0, destReg));
        
        compiler.addLabel(endLabel);
    }
    
}