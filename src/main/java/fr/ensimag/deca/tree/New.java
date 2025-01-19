package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class New extends AbstractExpr {

    final private AbstractIdentifier ident;

    public New(AbstractIdentifier ident){
        Validate.notNull(ident);
        this.ident = ident;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // Règle syntaxe contextuelle : (3.42)
        Type type = ident.verifyType(compiler);
        if (!type.isClass()) {
            throw new ContextualError("Identifier " + type + " is not a class", getLocation());
        }
        setType(type);
        return type;
    }

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        // Calcule de la taille de la structure à allouer
        ClassDefinition classDef = (ClassDefinition) this.ident.getDefinition();
        int size = classDef.getNumberOfFields() + 1;

        //Allocation du tas
        compiler.addInstruction(new NEW(size, destReg));
        compiler.addInstruction(new BOV(new Label("tas_plein"))); // Gestion de débordement du tas

        // On charger la table des méthodes de la classe 
        compiler.addInstruction(new LEA(classDef.getVMTablesAddr(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, destReg)));

        //Appeler l'initialisation
        compiler.addInstruction(new PUSH(destReg)); // Empiler l'adresse de l'objet
        compiler.addInstruction(new BSR(classDef.getInitLabel())); // Appeler init.Classe
        compiler.addInstruction(new POP(destReg)); // Récupérer l'adresse dans destReg
    }
    @Override
    public void codeGenAddr(DecacCompiler compiler, GPRegister destReg) {
             codeGenVal(compiler, destReg);
    }
   

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        ident.decompile(s);
        s.print("()");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        ident.iter(f);
    }
    
}
