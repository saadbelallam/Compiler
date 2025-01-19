package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

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
        for (AbstractDeclField declField : getList()) {
            declField.decompile(s);
            s.println();
        }
        s.println();
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

    
    public void codeGenListField(DecacCompiler compiler){
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'decompile'");
        //Charger l'adresse de l'objet (-2(LB)) dans un registre
        compiler.getMangerR().SaveRegIntoStack(compiler);
        compiler.getMangerT().restOffset();
        GPRegister reg =compiler.getMangerR().allocateRegister();
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB),reg),
         "R contient l'adresse de l'objet");
        for(AbstractDeclField field : this.getList()){
            field.codeGenField(compiler, reg);
        }
        //compiler.getMangerT().restOffset();
        compiler.getMangerR().RestoreSavedRegisters(compiler);

    }


    public void codeGenListFieldExtends(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'codeGenListFieldExtends'");
       GPRegister reg =compiler.getMangerR().allocateRegister();
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), reg), "R1 contient l'adresse de l'objet");
        for(AbstractDeclField field : this.getList()){
            field.codeGenFieldExends(compiler,reg);
        }
        
        // Appel de l'initialisation des champs hérités
            String superClass = compiler.getCurrentCass().getSuperClassName();
            compiler.addComment("Appel de l'initialisation des champs hérités de " + superClass);
            compiler.addInstruction(new PUSH(Register.R1));
            Label initLabel = new Label("init." + superClass);
            compiler.getMangerR().SaveRegIntoStack(compiler);
            compiler.addInstruction(new BSR(initLabel));
            compiler.addInstruction(new SUBSP(1));
            
            for(AbstractDeclField field : this.getList()){
                field.codeGenFieldExendsExplicit(compiler);
            }
            compiler.getMangerR().RestoreSavedRegisters(compiler);

       
    }
}
