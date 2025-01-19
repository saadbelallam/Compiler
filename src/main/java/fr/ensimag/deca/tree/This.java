package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class This extends AbstractExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type type = currentClass.getType();
        this.setType(type);
        return type;
    }

    
    
    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister dest) {
        // For value generation, we do the same as address generation
        // since 'this' is a reference
        codeGenAddr(compiler, dest);
    }

    @Override
    public void codeGenAddr(DecacCompiler compiler, GPRegister dest) {
        // Load address of 'this' from stack (-2(LB) contains the object address)
        compiler.addInstruction(new LOAD(
            new RegisterOffset(-2, Register.LB),
            dest
        ));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("this");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
       // Nothing to do
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterChildren'");
    }
    
}
