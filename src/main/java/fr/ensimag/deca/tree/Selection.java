package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class Selection extends AbstractLValue {
    
    private AbstractExpr classM;
    final private AbstractIdentifier field;

    public Selection(AbstractExpr classM, AbstractIdentifier field){
        Validate.notNull(classM);
        Validate.notNull(field);
        this.classM = classM;
        this.field = field;
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type type = classM.verifyExpr(compiler, localEnv, currentClass);
        if (!type.isClass()) {
            throw new ContextualError("Selection must be called on a class", getLocation());
        }
        ClassDefinition classDef = (ClassDefinition) compiler.environmentType.get(classM.getType().getName());
        if (classDef == null) {
            throw new ContextualError("Class " + classM.getType().getName() + " not found", getLocation());
        }
        field.verifyExpr(compiler, classDef.getMembers(), currentClass);
        if (field.getDefinition() == null) {
            throw new ContextualError("Field " + field.getName() + " not found in class " + classM.getType().getName(), getLocation());
        }
        if (!field.getDefinition().isField()) {
            throw new ContextualError("Field " + field.getName() + " is not a field", getLocation());
        }
 
        if (field.getFieldDefinition().getVisibility() == Visibility.PROTECTED) {
            // Vérifie si la classe courante est un sous-type de la classe où le champ protégé est déclaré.
            if (currentClass == null) {
                throw new ContextualError("The field is protected and cannot be accessed", getLocation());
            }
            if (!compiler.environmentType.subType(currentClass.getType(), field.getFieldDefinition().getContainingClass().getType())) {
                throw new ContextualError("The field is protected and cannot be accessed",  getLocation());
            }

            // Vérifie si le type de l'expression est un sous-type de la classe courante.
            if (!compiler.environmentType.subType(classDef.getType(), currentClass.getType())) {
                throw new ContextualError("The field is protected and cannot be accessed", getLocation());
            }
        }
        Type fieldType = field.getType();
        setType(fieldType);
        return fieldType;
    }

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {

        GPRegister objReg = compiler.getMangerR().allocateRegister();
        try {
           
            classM.codeGenVal(compiler, objReg);
            
  
            compiler.addInstruction(new CMP(new NullOperand(), objReg));
            compiler.addInstruction(new BEQ(new Label("null_deref")));
            
         
            int fieldOffset = field.getFieldDefinition().getIndex();
            compiler.addInstruction(new LOAD(new RegisterOffset(fieldOffset, objReg), destReg));
            
        } finally {
            compiler.getMangerR().freeRegister(objReg);
        }
    }

    @Override
    public void codeGenStore(DecacCompiler compiler, GPRegister srcReg) {

        GPRegister objReg = compiler.getMangerR().allocateRegister();
        try {

            classM.codeGenVal(compiler, objReg);
            

            compiler.addInstruction(new CMP(new NullOperand(), objReg));
            compiler.addInstruction(new BEQ(new Label("null_deref")));
            

            int fieldOffset = field.getFieldDefinition().getIndex();
            compiler.addInstruction(new STORE(srcReg, new RegisterOffset(fieldOffset, objReg)));
            
        } finally {
            compiler.getMangerR().freeRegister(objReg);
        }
    }


   @Override
    public void codeGenAddr(DecacCompiler compiler, GPRegister dest) {

        classM.codeGenVal(compiler, dest);
        

        compiler.addInstruction(new CMP(new NullOperand(), dest));
        compiler.addInstruction(new BEQ(new Label("null_deref")));
    }


    public AbstractIdentifier getField() {
        return field;
    }



    @Override
    public void decompile(IndentPrintStream s) {
        classM.decompile(s);
        s.print(".");
        field.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classM.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classM.iter(f);
        field.iter(f);
    }

    @Override
    public RegisterOffset getMemoryLocation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMemoryLocation'");
    }

    @Override
    public ExpDefinition getExpDefinition() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExpDefinition'");
    }
    public AbstractIdentifier getFeildSelc(){

        return field;

    }

    public AbstractExpr getObject(){

        return classM;

    }
}
