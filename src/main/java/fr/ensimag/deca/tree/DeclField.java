package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;

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
    protected void codeGenField(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'codeGenField'");
    }

    @Override
    protected void verifyDeclFieldMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
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
