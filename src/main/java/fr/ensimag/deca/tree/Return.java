package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

public class Return extends AbstractInst{

    private AbstractExpr expr;
    private  Label returnLabel;

    public Return(AbstractExpr expr){
        this.expr = expr;
    }
    
    @Override
    public void setReturnLabel(Label rLabel){
        this.returnLabel = rLabel;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyInst'");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        
        /* if (expr != null) {
            expr.codeGenVal(compiler, Register.R0);
        } */
        compiler.addInstruction(new BRA(returnLabel));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decompile'");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
       expr.iter(f);
    }
    
}
