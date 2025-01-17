package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;

public class Cast extends AbstractExpr {

    final private AbstractIdentifier type;
    final private AbstractExpr expr;

    public Cast(AbstractIdentifier type,AbstractExpr expr){
        Validate.notNull(type);
        Validate.notNull(expr);

        this.type = type;
        this.expr = expr;
    }

     @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, 
            ClassDefinition currentClass) throws ContextualError {
        Type castType = type.verifyType(compiler);
        Type exprType = expr.verifyExpr(compiler, localEnv, currentClass);
        
        
        if (!compiler.getEnvironmentType().castCompatible(exprType, castType)) {
            throw new ContextualError("Invalid cast from " + exprType + " to " + castType, getLocation());
        }
        
        setType(castType);
        return castType;
    }

     @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        expr.codeGenVal(compiler, destReg);
        
        Type sourceType = expr.getType();
        Type targetType = type.getType();

        if (sourceType.isInt() && targetType.isFloat()) {
            compiler.addInstruction(new FLOAT(destReg, destReg));
        } 
        else if(targetType.isInt() && sourceType.isFloat()){
            compiler.addInstruction(new INT(destReg, destReg));
        }

    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(") (");
        expr.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expr.iter(f);
    }
    
}
