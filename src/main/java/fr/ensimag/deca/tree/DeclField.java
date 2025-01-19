package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class DeclField extends AbstractDeclField {

            private static final Logger LOG = Logger.getLogger(AbstractDeclField.class);

    final private Visibility visibility;
    final private AbstractIdentifier type;
    final private AbstractIdentifier fieldName;
    final private AbstractInitialization initialization;


    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization initialization) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(fieldName);
        Validate.notNull(initialization);
        this.visibility = visibility;
        this.type = type;
        this.fieldName = fieldName;
        this.initialization = initialization;
    }


    @Override
    protected void codeGenField(DecacCompiler compiler, GPRegister FRegister ) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'codeGenField'");
        GPRegister reg = compiler.getMangerR().allocateRegister();
        RegisterOffset address = compiler.getMangerT().allocateField(FRegister);
        fieldName.getExpDefinition().setOperand(address);   
       
        if (initialization instanceof Initialization) {
            AbstractExpr initExpr = ((Initialization) initialization).getExpression();
            try {
                // Use the potentially converted expression
                initExpr.codeGenVal(compiler, reg);
                compiler.addInstruction(new STORE(reg, address), fieldName.getExpDefinition().getOperand()+"est l'adresse de "+fieldName.getName());
            } finally{}            
        }else{
            // Determine the value to initialize the field with
            switch (type.getName().getName()) {
                case "int":
                    // Initialize integers to 0
                    compiler.addInstruction(new LOAD(new ImmediateInteger(0), reg));
                    break;
                case "float":
                    // Initialize floats to 0.0
                    compiler.addInstruction(new LOAD(new ImmediateFloat(0.0f), reg));
                    break;
                case "boolean":
                    // Initialize booleans to false (0)
                    compiler.addInstruction(new LOAD(new ImmediateInteger(0),reg));
                    break;
                default:
                    // Initialize objects (references) to null
                    compiler.addInstruction(new LOAD(new NullOperand(), reg));
                    break;
            }
            compiler.addInstruction(new STORE(reg, address),fieldName.getExpDefinition().getOperand()+"est l'adresse de "+fieldName.getName());
        }
    } 

    
   
    @Override
    protected void codeGenFieldExends(DecacCompiler compiler, GPRegister regF) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'codeGenFieldExends'");
        RegisterOffset address = compiler.getMangerT().allocateField(regF);
        fieldName.getExpDefinition().setOperand(address);  
        GPRegister reg = Register.R0;
        // Determine the value to initialize the field with
            switch (type.getName().getName()) {
                case "int":
                    // Initialize integers to 0
                    compiler.addInstruction(new LOAD(new ImmediateInteger(0), reg));
                    break;
                case "float":
                    // Initialize floats to 0.0
                    compiler.addInstruction(new LOAD(new ImmediateFloat(0.0f), reg));
                    break;
                case "boolean":
                    // Initialize booleans to false (0)
                    compiler.addInstruction(new LOAD(new ImmediateInteger(0),reg));
                    break;
                default:
                    // Initialize objects (references) to null
                    compiler.addInstruction(new LOAD(new ImmediateInteger(0), reg));
                    break;
            }
            compiler.addInstruction(new STORE(reg, address),fieldName.getExpDefinition().getOperand()+"est l'adresse de "+fieldName.getName());
        }
            


    @Override
    protected void codeGenFieldExendsExplicit(DecacCompiler compiler) {
        GPRegister reg = compiler.getMangerR().allocateRegister();
        DAddr address = fieldName.getExpDefinition().getOperand();
        String className = compiler.getCurrentCass().getClassName();
        // initialisation explicite des champs de la classe courante
        if (initialization instanceof Initialization) {
            AbstractExpr initExpr = ((Initialization) initialization).getExpression();
            try {
                compiler.addComment("Initialisation explicite de "+className+"."+fieldName.getName());
                initExpr.codeGenVal(compiler, reg);
                compiler.addInstruction(new STORE(reg, address),fieldName.getExpDefinition().getOperand()+"est l'adresse de "+fieldName.getName());
            } finally{}     
        }
    }
    

    @Override
    protected Visibility verifyDeclFieldMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify declFieldMembers: start");

        // Étape 1 : Vérification du type
        Type fieldType = this.type.verifyType(compiler);
        
        // Étape 2 : Vérifier que le type n'est pas void
        if (fieldType.isVoid()) {
            throw new ContextualError("Field cannot have type void", this.getLocation());
        }

        // Étape 3 : Vérification si l’identificateur name est déjà défini dans l’environnement des expressions de la super-classe 
                    // alors ce doit être un identificateur de champ
        Definition superDef = currentClass.getSuperClass().getMembers().get(fieldName.getName());
        if (superDef != null) {
            if (!superDef.isField()) {
                throw new ContextualError("Field name cannot be used", this.getLocation());
            }
        }

        // Étape 4 : Ajout de la définition du champ dans l'environnement de la classe
        currentClass.incNumberOfFields();
        FieldDefinition fieldDef = new FieldDefinition(fieldType, getLocation(), visibility, currentClass, currentClass.getNumberOfFields());
        fieldName.setDefinition(fieldDef);
        fieldName.setType(fieldType);
        try {
            currentClass.getMembers().declare(fieldName.getName(), fieldDef);
        } catch (DoubleDefException e) {
            throw new ContextualError("Field name already used", this.getLocation());
        }
        LOG.debug("verify declFieldMembers: end");
        return visibility;
    }

    @Override
    protected void verifyDeclFieldBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify declFieldBody: start");
        initialization.verifyInitialization(compiler, type.getType(), localEnv, currentClass);
        LOG.debug("verify declFieldBody: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decompile'");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'prettyPrintChildren'");
        type.prettyPrint(s, prefix, false);
        fieldName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterChildren'");
    }


   

   
}
