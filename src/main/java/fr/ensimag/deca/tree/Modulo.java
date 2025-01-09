package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        Type type1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!type1.isInt() || !type2.isInt()) {
            throw new ContextualError("Operands of modulo should be of type int", getLocation());
        }
        setType(type1);
        return type1;
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }


     @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        
        GPRegister rightReg = compiler.getMangerR().allocateRegister();
        
        try {
            
            getLeftOperand().codeGenVal(compiler, destReg);
            
            // Generate code for right operand into rightReg
            getRightOperand().codeGenVal(compiler, rightReg);
            
            // Compute remainder directly using REM instruction
            compiler.addInstruction(new REM(rightReg, destReg));
            
        } finally {
            compiler.getMangerR().freeRegister(rightReg);
        }
    }
    }

