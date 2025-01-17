package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

public class Return extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(AbstractInst.class);


    private AbstractExpr expr;
    private  Label returnLabel;

    public Return(AbstractExpr expr){
        this.expr = expr;
    }
    
    @Override
    public void setReturnLabel(Label rLabel){
        this.returnLabel = rLabel;
    }

    public void setExpr(AbstractExpr expr){
        this.expr = expr;
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        LOG.debug("verify Return: start");

        if (returnType.isVoid()) {
            throw new ContextualError("Return type cannot be void", this.getLocation());
        }

        if (!compiler.environmentType.assignCompatible(returnType, expr.getType())) {
            throw new ContextualError("Type " + expr.getType().getName() + " is not compatible with return type " + returnType, this.getLocation());
        }

        this.setExpr(expr.verifyRValue(compiler, localEnv, currentClass, returnType));

        LOG.debug("verify Return: end");
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
