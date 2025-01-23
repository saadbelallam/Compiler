package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl24
 * @date 01/01/2025
 */
public class ConvFloat extends AbstractUnaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractUnaryExpr.class);

    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify conv float: start");
        // Type operandType = getOperand().verifyExpr(compiler, localEnv, currentClass);
        
        // Verify operand is an int
        // if (!getOperand().getType().isInt()) {
        //     throw new ContextualError("Cannot convert non-int type to float", getLocation());
        // }
        
        Type floatType = compiler.environmentType.FLOAT;
        setType(floatType);
        LOG.debug("verify conv float: end");
        return floatType;
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        getOperand().codeGenVal(compiler, destReg);
        compiler.addInstruction(new FLOAT(destReg, destReg));
    }


}
