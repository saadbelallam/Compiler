package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Class representing the `null` literal in Deca.
 */
public class Null extends AbstractExpr {
            private static final Logger LOG = Logger.getLogger(AbstractExpr.class);


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify null: start");
        Type nullType = compiler.environmentType.NULL;        
        this.setType(nullType);
        LOG.debug("verify null: end");
        return nullType;
    }

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister register) {
        // Generate code to load #null into the provided register
        compiler.addInstruction(new LOAD(new NullOperand(), register));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
}
