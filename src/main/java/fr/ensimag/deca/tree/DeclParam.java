package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;


// this class was made to deal at the first place with params of the methode !
// this is not the final implementation !!


public class  DeclParam extends AbstractDeclParam {

    final private AbstractIdentifier type;
    final private AbstractIdentifier paramName;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier paramName) {
    Validate.notNull(type);
    Validate.notNull(paramName);

    this.type = type;
    this.paramName = paramName;

}

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("{ ... A FAIRE ... }");
    }

   @Override
   protected  void verifyParam(DecacCompiler compiler){
        throw new UnsupportedOperationException("not yet implemented");
   }
   

   @Override
   protected  void verifyBody(DecacCompiler compiler,
                          EnvironmentExp methodEnv) 
        throws ContextualError { 
        throw new UnsupportedOperationException("not yet implemented");
   }

    @Override
    protected  void codeGenParam(DecacCompiler compiler){
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // throw new UnsupportedOperationException("Not yet supported");
        type.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}