package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl24
 * @date 01/01/2025
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // Étape 1 : Vérification du type
        Type varType = this.type.verifyType(compiler);
        
        // Étape 2 : Vérifier que le type n'est pas void
        if (varType.isVoid()) {
            throw new ContextualError("Variable cannot have type void", this.getLocation());
        }

        // Étape 3 : Vérification de l'initialisation
        if (this.initialization != null) {
            this.initialization.verifyInitialization(compiler, varType, localEnv, currentClass);
        }

        // Étape 4 : Mise à jour de l'environnement
        Symbol name = this.varName.getName();
        VariableDefinition varDef = new VariableDefinition(varType, this.getLocation());
        try {
            localEnv.declare(name, varDef);
        } catch (DoubleDefException e) {
            throw new ContextualError("Variable " + name + " is already defined ", this.getLocation());
        }
    }


    
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
