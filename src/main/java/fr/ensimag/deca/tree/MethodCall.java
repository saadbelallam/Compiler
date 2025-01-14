package fr.ensimag.deca.tree;

import java.io.PrintStream;

import javax.swing.AbstractListModel;

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
