package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
        // Étape 0 : Déclaration de type string (?)
        if (this.type.toString().equals("string")) {
            compiler.environmentType.declareType(compiler.createSymbol("string"), 
                                                new TypeDefinition(compiler.environmentType.STRING, this.getLocation()));
        }

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
        varName.setType(varType);
        varName.setDefinition(varDef);
        try {
            localEnv.declare(name, varDef);
        } catch (DoubleDefException e) {
            throw new ContextualError("Variable " + name + " is already defined ", this.getLocation());
        }
    }
    
    @Override
    protected void codeGenDeclVar(DecacCompiler compiler) {
        System.out.println("Entrée codeGenDeclVar");
        RegisterManager registerManager = compiler.getRegisterManager(); // Récupération du gestionnaire de registres
        System.out.println("Récupération du gestionnaire de registrese");
        // Obtenir un registre libre
        Register tempRegister = registerManager.getRegister();
        System.out.println("Obtenir un registre libre");
        
        
        
        try {
            // On charge la valeur de la variable (constante ou valeur par défaut 0 si NoInitialization) 
            if (initialization instanceof Initialization) {
                System.out.println("before initialisation");
                AbstractExpr initExpr = ((Initialization) initialization).getExpression();
                System.out.println("After initialisation");
                // à implémenter dans les sous-classes de AbstractExpr
                initExpr.codeGenExpr(compiler, (GPRegister) tempRegister);  

            } else {
                compiler.addInstruction(new LOAD(new ImmediateInteger(0), (GPRegister) tempRegister)); // Valeur par défaut 0
                System.out.println("LOAD");
            
            }
    
            // On détermine l'adresse en mémoire de la variable
            int offset = registerManager.getNextGlobalOffset();
            System.out.println("Calcul d'offset: " + offset);
            RegisterOffset adress = registerManager.getGlobalOffset();
            System.out.println("Clacul d'adresse: "+ adress);


            // On stocke cette adresse dans la définition de la variable
            //à compléter

            // On stocke la valeur dans l'emplacement mémoire correspondant
            compiler.addInstruction(new STORE(tempRegister, adress)); // Sauvegarde de la valeur dans la pile
            System.out.println("Store");
            
        } finally {
            // Libération du registre utilisé
            //registerManager.releaseRegister(tempRegister);
            System.out.println("finaly");
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
