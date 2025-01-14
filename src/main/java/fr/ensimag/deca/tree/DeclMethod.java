package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclMethod extends AbstractDeclMethod{

    private static final Logger LOG = Logger.getLogger(AbstractDeclMethod.class);


    
    final private AbstractIdentifier type;
    final private AbstractIdentifier methodName;
    final private ListDeclParam listParams;
    final private ListDeclVar listDeclVar;
    final private ListInst listInst;



    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam listParams,ListDeclVar listDeclVar,ListInst listInst) {
        Validate.notNull(type);
        Validate.notNull(methodName);
        Validate.notNull(listParams);
        Validate.notNull(listDeclVar);
        Validate.notNull(listInst);

        this.type = type;
        this.methodName = methodName;
        this.listParams =listParams;
        this.listDeclVar = listDeclVar;
        this.listInst = listInst;
    }

    @Override
    protected void codeGenMethod(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'codeGenMethod'");
    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyDeclMethod'");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decompile'");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'prettyPrintChildren'");
        type.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        listParams.prettyPrint(s, prefix, false);
        listDeclVar.prettyPrint(s, prefix, false);
        listInst.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterChildren'");
    }
    
}
