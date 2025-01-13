package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;





public class ListDeclParam extends TreeList<AbstractDeclParam>{
    
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