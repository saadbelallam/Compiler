package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;





public class ListDeclParam extends TreeList<AbstractDeclParam>{
    private static final Logger LOG = Logger.getLogger(AbstractDeclParam.class);


    public EnvironmentExp verifyListParamBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listParamBody: start");
        EnvironmentExp envExp = new EnvironmentExp(null);
        for (AbstractDeclParam declParam : getList()) {
            declParam.verifyParamBody(compiler, envExp);
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