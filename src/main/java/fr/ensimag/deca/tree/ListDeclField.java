package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl24
 * @date 01/01/2025
 */
public class ListDeclField extends TreeList<AbstractDeclField> {
    private static final Logger LOG = Logger.getLogger(ListDeclField.class);

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decompile'");
    }


    void verifyListDeclFieldMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listDeclField: start");
        for (AbstractDeclField declField : getList()) {
            declField.verifyDeclFieldMembers(compiler, currentClass);
        }
        LOG.debug("verify listDeclField: end");
    }


    void verifyListDeclFieldBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listDeclField: start");
        for (AbstractDeclField declField : getList()) {
            declField.verifyDeclFieldBody(compiler, localEnv, currentClass);
        }
        LOG.debug("verify listDeclField: end");
    }   
    
    

    //méthode pour calculer le nombre de variable global (utile pour la taille de la pile)
    public int getNombreFields() {
        return this.getList().size();
    }

    
    public void codeGenListVar(DecacCompiler compiler){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decompile'");
    }
}
