package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;


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
        type.decompile(s);
        s.print(" ");
        paramName.decompile(s);
    }

    @Override
    protected Type verifyParamMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify declParamMembers: start");
        // Étape 1 : Vérification du type
        Type paramType = this.type.verifyType(compiler);
        this.type.setType(paramType);

        // Étape 2 : Vérifier que le type n'est pas void
        if (paramType.isVoid()) {
            throw new ContextualError("Parameter " + paramName.getName() + "cannot have type void" + " // class : " + currentClass.getType() , this.getLocation());
        }

        LOG.debug("verify declParamMembers: end");
        return paramType;
    }

    @Override
    protected void verifyParamBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify declParamBody: start");
        // Étape 3 : Mise à jour de l'environnement
        Symbol name = this.paramName.getName();
        ParamDefinition paramDef = new ParamDefinition(type.getType(), paramName.getLocation());
        paramName.setDefinition(paramDef);
        paramName.setType(type.getType());
        try {
            localEnv.declare(name, paramDef);
        } catch (DoubleDefException e) {
            throw new ContextualError("Parameter " + name + " is already used " + " // class : " + currentClass.getType(), this.getLocation());
        }
        LOG.debug("verify declParamBody: end");
    }
   

    @Override
    protected void codeGenParam(DecacCompiler compiler) {
        int paramOffset = -(3 + compiler.getMangerM().getCurrentOffsetPram());
    
        RegisterOffset paramLoc = new RegisterOffset(paramOffset, Register.LB);
        
        compiler.getMangerM().InCurrentOffsetPram();
        paramName.getExpDefinition().setOperand(paramLoc);
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // throw new UnsupportedOperationException("Not yet supported");
        type.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        paramName.iter(f);
    }

}