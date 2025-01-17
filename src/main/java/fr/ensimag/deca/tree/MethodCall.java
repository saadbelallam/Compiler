package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodCall extends AbstractExpr{

    private static final Logger LOG = Logger.getLogger(AbstractExpr.class);


    private AbstractExpr classM;
    final private AbstractIdentifier methodName;
    final private ListExpr listExpr;
     

    public MethodCall(AbstractExpr classM,AbstractIdentifier methodName,ListExpr listExpr ) {
        Validate.notNull(methodName);
        Validate.notNull(listExpr);
        Validate.notNull(classM);
        this.methodName = methodName;
        this.listExpr = listExpr;
        this.classM =classM;
    }

    public MethodCall(AbstractIdentifier methodName,ListExpr listExpr) {
        Validate.notNull(methodName);
        Validate.notNull(listExpr);
        this.methodName = methodName;
        this.listExpr = listExpr;
    }

    @Override 
    public int getMaxExpressionDepth() {
        int maxArgDepth = 0;
        for (AbstractExpr arg : listExpr.getList()) {
            maxArgDepth = Math.max(maxArgDepth, arg.getMaxExpressionDepth());
        }
        return maxArgDepth + listExpr.size() + 3;
    }

    /* @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        methodCallCount++; // Increment counter when generating code for method call
        
        // Push arguments onto stack in reverse order
        for (int i = arguments.size() - 1; i >= 0; i--) {
            GPRegister argReg = compiler.getRegisterManager().allocateRegister();
            arguments.getList().get(i).codeGenVal(compiler, argReg);
            compiler.addInstruction(new PUSH(argReg));
            compiler.getRegisterManager().freeRegister(argReg);
        }

        // Call the method
        compiler.addInstruction(new BSR(method.getMethodDefinition().getLabel()));

        // Pop result into destination register
        compiler.addInstruction(new POP(destReg));

        // Clean up arguments from stack
        if (arguments.size() > 0) {
            compiler.addInstruction(new SUBSP(arguments.size()));
        }
    } */

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decompile'");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO Auto-generated method stub
        methodName.prettyPrint(s, prefix, false);
        listExpr.prettyPrint(s, prefix, false);
        if (classM != null){
            classM.prettyPrint(s, prefix, false);
        }


    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterChildren'");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyExpr'");
    }
    
}
