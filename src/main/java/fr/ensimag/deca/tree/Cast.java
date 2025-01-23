package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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
        
        // Check if cast is possible
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
        else if (sourceType.isFloat() && targetType.isInt()) {
            compiler.addInstruction(new INT(destReg, destReg));
        }
        else if (sourceType.isClassOrNull() && targetType.isClass()) {
            Label successLabel = new Label("cast_success_" + compiler.getMangerL().newLabel());
            Label errorLabel = new Label("cast_error");
            
            
            compiler.addInstruction(new CMP(new NullOperand(), destReg));
            compiler.addInstruction(new BEQ(successLabel)); // null can be cast to any class type
            
            GPRegister vtableReg = compiler.getMangerR().allocateRegister();
            compiler.addInstruction(new LOAD(new RegisterOffset(0, destReg), vtableReg));
            ClassDefinition currentClass = ((ClassType)targetType).getDefinition();
            
            
            /* // Compare with target class vtable
            DAddr targetVTable = targetClass.getVMTablesAddr();
            System.err.println("currentClass.getVMTablesAddr() = " + targetVTable);
            System.err.println("currentClass = " + currentClass.getVMTablesAddr());
            System.err.println("currentClass = " + currentClass);
           
            /* compiler.addInstruction(new LEA(currentClass.getVMTablesAddr(), Register.R0));
            compiler.addInstruction(new CMP(Register.R0, vtableReg));
            compiler.addInstruction(new BEQ(successLabel));
              */
            
            while (currentClass.getSuperClass() != null) {
                currentClass = currentClass.getSuperClass();
                compiler.addInstruction(new LEA(currentClass.getVMTablesAddr(), Register.R0));
                compiler.addInstruction(new CMP(Register.R0, vtableReg));
                compiler.addInstruction(new BEQ(successLabel));
            }
            
            compiler.addInstruction(new BRA(errorLabel));
            
            compiler.addLabel(successLabel);
            compiler.getMangerR().freeRegister(vtableReg);
            
           
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
