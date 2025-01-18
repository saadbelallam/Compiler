package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
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
        // Get a temporary register for the object reference
        GPRegister objReg = compiler.getMangerR().allocateRegister();
        
        try {
            // Evaluate the object expression into objReg
            classM.codeGenVal(compiler, objReg);
            
            // Add null check
            Label errorLabel = new Label("null_deref");
            compiler.addInstruction(new CMP(new NullOperand(), objReg));
            compiler.addInstruction(new BEQ(errorLabel));
            
            // Get the field offset from the identifier definition
            FieldDefinition fieldDef = field.getFieldDefinition();
            int offset = fieldDef.getIndex();
            
            // Load field value from object's memory location into destReg
            compiler.addInstruction(new LOAD(new RegisterOffset(offset, objReg), destReg));
            
        } finally {
            // Free the temporary register
            compiler.getMangerR().freeRegister(objReg);
        }
    }
    
    @Override
    protected void codeGenStore(DecacCompiler compiler, GPRegister srcReg) {
        // Get a temporary register for the object reference
        GPRegister objReg = compiler.getMangerR().allocateRegister();
        
        try {
            // Evaluate the object expression into objReg
            classM.codeGenVal(compiler, objReg);
            
            // Add null check
            Label errorLabel = new Label("null_deref");
            compiler.addInstruction(new CMP(new NullOperand(), objReg));
            compiler.addInstruction(new BEQ(errorLabel));
            
            // Get field offset
            int fieldOffset = field.getFieldDefinition().getIndex();
            
            // Store srcReg value into object's field
            compiler.addInstruction(new STORE(srcReg, new RegisterOffset(fieldOffset, objReg)));
            
        } finally {
            // Free the temporary register
            compiler.getMangerR().freeRegister(objReg);
        }
    }



    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decompile'");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO Auto-generated method stub
        classM.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterChildren'");
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
    
}
