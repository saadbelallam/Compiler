package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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



    public void codeGenListField(DecacCompiler compiler) {
      
        /*  compiler.addInstruction(new PUSH(Register.getR(2)));
        compiler.addInstruction(new PUSH(Register.getR(3)));
        compiler.addInstruction(new PUSH(Register.getR(4)));
        compiler.addInstruction(new PUSH(Register.getR(5)));
        compiler.addInstruction(new PUSH(Register.getR(6)));
        compiler.addInstruction(new PUSH(Register.getR(7)));
        compiler.addInstruction(new PUSH(Register.getR(8)));
        compiler.addInstruction(new PUSH(Register.getR(9)));
        compiler.addInstruction(new PUSH(Register.getR(10)));
        compiler.addInstruction(new PUSH(Register.getR(11)));
        compiler.addInstruction(new PUSH(Register.getR(12)));
        compiler.addInstruction(new PUSH(Register.getR(13)));
        compiler.addInstruction(new PUSH(Register.getR(14)));
        compiler.addInstruction(new PUSH(Register.getR(15))); */
        compiler.getMangerR().SaveRegIntoStack(compiler);
        // Allocate registers for object address and value
        GPRegister objReg = compiler.getMangerR().allocateRegister();
        GPRegister valueReg = compiler.getMangerR().allocateRegister();
        
        try {
            // Get object address from parameter (-2(LB))
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), objReg));
            
            // First initialize all fields to default values
            for (AbstractDeclField field : getList()) {
                DeclField f = (DeclField)field;
                int fieldOffset = f.getFieldOffset();
                
                // Load appropriate default value based on type
                switch (f.getFieldName().getType().getName().getName()) {
                    case "int":
                        compiler.addInstruction(new LOAD(new ImmediateInteger(0), valueReg));
                        break;
                    case "float":
                        compiler.addInstruction(new LOAD(new ImmediateFloat(0.0f), valueReg));
                        break;
                    case "boolean":
                        compiler.addInstruction(new LOAD(new ImmediateInteger(0), valueReg));
                        break;
                    default:
                        compiler.addInstruction(new LOAD(new NullOperand(), valueReg));
                        break;
                }
                
                // Store default value
                compiler.addInstruction(new STORE(valueReg, 
                    new RegisterOffset(fieldOffset, objReg)));
            }
            
            // Then handle explicit initializations
            for (AbstractDeclField field : getList()) {
                field.codeGenField(compiler);
            }
        } finally {
            compiler.getMangerR().freeRegister(valueReg);
            compiler.getMangerR().freeRegister(objReg);
        }

        /*  compiler.addInstruction(new POP(Register.getR(15)));
         compiler.addInstruction(new POP(Register.getR(14)));
         compiler.addInstruction(new POP(Register.getR(13)));
         compiler.addInstruction(new POP(Register.getR(12)));
         compiler.addInstruction(new POP(Register.getR(11)));
         compiler.addInstruction(new POP(Register.getR(10)));
         compiler.addInstruction(new POP(Register.getR(9)));
         compiler.addInstruction(new POP(Register.getR(8)));
         compiler.addInstruction(new POP(Register.getR(7)));
         compiler.addInstruction(new POP(Register.getR(6)));
         compiler.addInstruction(new POP(Register.getR(5)));
         compiler.addInstruction(new POP(Register.getR(4)));
         compiler.addInstruction(new POP(Register.getR(3)));
         compiler.addInstruction(new POP(Register.getR(2))); */
        compiler.getMangerR().RestoreSavedRegisters(compiler);

    }



   /*  public void codeGenListField(DecacCompiler compiler) {
        // First set all fields to default values (0/false/null)
        for (AbstractDeclField field : getList()) {
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
            compiler.addInstruction(new LOAD(0, Register.R0));
            compiler.addInstruction(new STORE(Register.R0, 
                new RegisterOffset(((DeclField)field).getFieldOffset()
                , Register.R1)));
        }
        
        // Then handle explicit initializations
        for (AbstractDeclField field : getList()) {
            field.codeGenField(compiler);
        }
    }
     */
   /*  public void codeGenListField(DecacCompiler compiler){
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
        compiler.getMangerR().freeRegister(reg);


       
    } */
}
