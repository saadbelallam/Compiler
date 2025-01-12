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
public class ListDeclVar extends TreeList<AbstractDeclVar> {
    private static final Logger LOG = Logger.getLogger(ListDeclVar.class);


    @Override
    public void decompile(IndentPrintStream s) {
        // throw new UnsupportedOperationException("Not yet implemented");
        for (AbstractDeclVar declVar : getList()) {
            declVar.decompile(s);
            s.println();
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify list_decl_var: start");
        for (AbstractDeclVar declVar : getList()) {
            declVar.verifyDeclVar(compiler, localEnv, currentClass);
        }
        LOG.debug("verify list_decl_var: end");
    }     
    //méthode pour calculer le nombre de variable global (utile pour la taille de la pile)
    public int getNombreVariables() {
        return this.getList().size();
    }

    
    public void codeGenListVar(DecacCompiler compiler){
        for(AbstractDeclVar variable : this.getList()){
            variable.codeGenVar(compiler);
        }
}}
