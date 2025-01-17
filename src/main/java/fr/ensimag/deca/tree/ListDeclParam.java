package fr.ensimag.deca.tree;


import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;





public class ListDeclParam extends TreeList<AbstractDeclParam>{
    private static final Logger LOG = Logger.getLogger(AbstractDeclParam.class);


    public Signature verifyListParamMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listParamMembers: start");
        Signature signature = new Signature();
        for (AbstractDeclParam declParam : getList()) {
            signature.add(declParam.verifyParamMembers(compiler, currentClass));
        }
        LOG.debug("verify listParamMembers: end");
        return signature;
    }


    public EnvironmentExp verifyListParamBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listParamBody: start");
        EnvironmentExp envExp = new EnvironmentExp(localEnv);
        for (AbstractDeclParam declParam : getList()) {
            declParam.verifyParamBody(compiler, envExp, currentClass);
        }
        LOG.debug("verify listParamBody: end");
        return envExp;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclParam declParam : getList()) {
            declParam.decompile(s);
            s.println();
        }
    }

    public void codeGenListParm(DecacCompiler compiler){
        for(AbstractDeclParam declParm : getList()){
            declParm.codeGenParam(compiler);
        }
    }
}