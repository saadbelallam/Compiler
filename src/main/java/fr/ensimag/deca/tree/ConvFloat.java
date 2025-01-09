package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
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
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) {
        // throw new UnsupportedOperationException("not yet implemented");
        Type type = compiler.environmentType.FLOAT;
        this.setType(type);
        //this.setType(new FloatType(compiler.createSymbol("float")));
        return type;
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        // First generate code for the operand (int value)
        getOperand().codeGenVal(compiler, destReg);
        
        // Convert the integer in destReg to float using FLOAT instruction
        compiler.addInstruction(new FLOAT(destReg, destReg));
    }

}
