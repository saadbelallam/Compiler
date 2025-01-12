package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

import org.apache.log4j.Logger;

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
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) {
        // throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify conv float: start");
        Type type = compiler.environmentType.FLOAT;
        this.setType(type);
        LOG.debug("verify conv float: end");
        return type;
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
