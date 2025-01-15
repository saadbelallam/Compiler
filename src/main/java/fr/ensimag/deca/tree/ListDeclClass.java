package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.VirtualTables;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    private final VirtualTables vTables = new VirtualTables();
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
    
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClass(compiler);
        }
    
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGenListClass(DecacCompiler compiler) {
        vTables.codeGenVMTforObject(compiler);
        for (AbstractDeclClass c : this.getList()) {
            //System.err.println(""+ c.getLocation());
            vTables.codeGenVMT(compiler, c);
        }
}



}
