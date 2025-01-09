package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!type.isBoolean()) {
            throw new ContextualError("Operator '!' only on booleans", getLocation());
        }
        setType(type);
        return type;
    }

    
    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        getOperand().codeGenVal(compiler, destReg);
        
        // Compute NOT by subtracting from 1:
        // Nhsbo x b x = 1 - x (since x is either 0 or 1)
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), destReg));
        compiler.addInstruction(new SUB(destReg, destReg));
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Label label, boolean jump) {
        // reverse the contion by the jump
        getOperand().codeGenCondition(compiler, label, !jump);
    }
}
