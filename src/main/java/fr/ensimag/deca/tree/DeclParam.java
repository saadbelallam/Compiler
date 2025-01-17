package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;


// this class was made to deal at the first place with params of the methode !
// this is not the final implementation !!


public class DeclParam extends AbstractDeclParam {
    private static final Logger LOG = Logger.getLogger(AbstractDeclParam.class);



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
    protected void verifyParamBody(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
        LOG.debug("verify declParamBody: start");


        /* 

        enventually passe 2 :

        // Étape 1 : Vérification du type
        Type paramType = this.type.verifyType(compiler);

        // Étape 2 : Vérifier que le type n'est pas void
        if (paramType.isVoid()) {
            throw new ContextualError("Parameter cannot have type void", this.getLocation());
        }


        */

        
        // Étape 1 : Vérification du type
        Type paramType = this.type.verifyType(compiler); // passe 2 (?)


        // Étape 3 : Mise à jour de l'environnement
        Symbol name = this.paramName.getName();
        ParamDefinition paramDef = new ParamDefinition(type.getType(), paramName.getLocation());
        paramName.setDefinition(paramDef);
        paramName.setType(paramType);
        try {
            localEnv.declare(name, paramDef);
        } catch (DoubleDefException e) {
            throw new ContextualError("Parameter " + name + " is already used ", this.getLocation());
        }
        LOG.debug("verify declParamBody: end");
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