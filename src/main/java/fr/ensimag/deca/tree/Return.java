package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
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
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType) throws ContextualError {
        
        if (returnType == null) {
            throw new ContextualError("Return statement not allowed here", getLocation());
        }

        if (expr == null) {
            if (!returnType.isVoid()) {
                throw new ContextualError("Missing return value for non-void method", getLocation());
            }
            return;
        }
        
        Type actualType = expr.verifyExpr(compiler, localEnv, currentClass);
        
        if (actualType == null) {
            throw new ContextualError("Cannot determine type of return expression", getLocation());
        }
        if (!compiler.getEnvironmentType().assignCompatible(actualType, returnType)) {
            throw new ContextualError(
                "Incompatible return type: expected " + returnType.getName() + 
                ", got " + actualType.getName(),
                getLocation()); 
        }

        expr.setType(actualType);
    }

    public AbstractExpr getexpr() {
        return expr;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        
        if (expr != null) {
            expr.codeGenVal(compiler, Register.R0);
        }
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
