package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class InstanceOf extends AbstractExpr {
    private AbstractExpr expr;
    private AbstractIdentifier type;

    public InstanceOf(AbstractExpr expr, AbstractIdentifier type) {
        this.expr = expr;
        this.type = type;
    
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type type1 = expr.verifyExpr(compiler, localEnv, currentClass);
        Type type2 = type.verifyType(compiler);
        if (!type1.isClassOrNull()) {
            throw new ContextualError("Expression must be a class or null", expr.getLocation());
        }
        if (!type2.isClass()) {
            throw new ContextualError("Type must be a class", type.getLocation());
        }
        setType(compiler.environmentType.BOOLEAN);
        return compiler.environmentType.BOOLEAN;
    }

       //// Charger l'adresse de l'objet dans un registre temporaire
       //GPRegister objectReg = compiler.getMangerR().allocateRegister();
       //GPRegister tableReg = compiler.getMangerR().allocateRegister();
//
       //// Générer le code pour évaluer l'expression à gauche de `instanceof`
       //expr.codeGenVal(compiler, objectReg);
//
       //// Vérification : si l'objet est null, gérer une erreur de déréférencement
       //Label derefError = new Label("dereferencement_null");
       //compiler.addInstruction(new CMP(new NullOperand(), objectReg));
       //compiler.addInstruction(new BEQ(derefError));
//
       //// Charger la table des méthodes de l'objet
       //compiler.addInstruction(new LOAD(new RegisterOffset(0, objectReg), tableReg));
//
       //// Étiquette pour la boucle de vérification
       //Label instanceofLoop = new Label("instanceof_loop");
       //Label instanceofTrue = new Label("instanceof_true");
       //Label instanceofFalse = new Label("instanceof_false");
//
       //// Étiquette de la classe cible
       //AbstractIdentifier targetClass = ((ClassType) type.getType()).getClassDefinition().getName();
       //DAddr targetTableAddr = targetClass.getClassDefinition().getOperand();
//
       //// Boucle : comparer les adresses des tables des méthodes
       //compiler.addLabel(instanceofLoop);
       //compiler.addInstruction(new CMP(targetTableAddr, tableReg));
       //compiler.addInstruction(new BEQ(instanceofTrue)); // Si égal, test réussi
//
       //// Vérifier si on est arrivé à Object
       //compiler.addInstruction(new CMP(compiler.getEnvironmentType().getObjectType().getClassDefinition().getOperand(), tableReg));
       //compiler.addInstruction(new BEQ(instanceofFalse)); // Si Object, test échoué
//
       //// Charger la table parente
       //compiler.addInstruction(new LOAD(new RegisterOffset(0, tableReg), tableReg));
       //compiler.addInstruction(new BRA(instanceofLoop)); // Retour à la boucle
//
       //// Si le test est vrai
       //compiler.addLabel(instanceofTrue);
       //compiler.addInstruction(new LOAD(new ImmediateInteger(1), destReg)); // Résultat = 1
       //compiler.addInstruction(new BRA(new Label("instanceof_end")));
//
       //// Si le test est faux
       //compiler.addLabel(instanceofFalse);
       //compiler.addInstruction(new LOAD(new ImmediateInteger(0), destReg)); // Résultat = 0
//
       //// Fin de l'instruction
       //Label end = new Label("instanceof_end");
       //compiler.addLabel(end);
//
       //// Libérer les registres
       //compiler.getMangerR().freeRegister(objectReg);
       //compiler.getMangerR().freeRegister(tableReg);

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister register) {
        // On charge l'adresse de l'objet dans un registre temporaire
        //GPRegister objectReg = compiler.getMangerR().allocateRegister();

        // On charge l'adresse de l'objet dans objectReg
        //expr.codeGenVal(compiler, objectReg);

        ClassType typeObjet = (ClassType) expr.getType();
        ClassDefinition classe =    type.getClassDefinition();
        ClassType typeCible = classe.getType();     
        if (typeObjet.isSubClassOf(typeCible)) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(1), register));
        } else {
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));
        }

        // On vérifie si l'objet est null
        //Label derefError = new Label("null_deref");
        //compiler.addInstruction(new CMP(new NullOperand(), objectReg));
        //compiler.addInstruction(new BEQ(derefError));

        // On charge la table des méthodes de l'objet
        //GPRegister tableReg = compiler.getMangerR().allocateRegister();
        //compiler.addInstruction(new LOAD(new RegisterOffset(0, objectReg),objectReg));
//
        //// On crée les étiquettes pour la boucle de vérification
        //Label instanceofLoop = new Label("instanceof_loop");
        //Label instanceofTrue = new Label("instanceof_true");
        //Label instanceofFalse = new Label("instanceof_false");
//
        //// On récupère l'adresse de la table de la classe cible
        //ClassDefinition classe = type.getClassDefinition();
        //DAddr targetTableAddr = classe.getVMTablesAddr();
//
        //// On crée la boucle de vérification
        //compiler.addLabel(instanceofLoop);
        //compiler.addInstruction(new CMP(targetTableAddr, objectReg));
        //compiler.addInstruction(new BEQ(instanceofTrue));
//
        //// On vérifie si on est arrivé à Object
        //compiler.addInstruction(new CMP(new RegisterOffset(1, Register.GB), objectReg));   
        //compiler.addInstruction(new BEQ(instanceofFalse));
//
        //// On charge la table parente
        //compiler.addInstruction(new LOAD(new RegisterOffset(0, objectReg), objectReg));
        //compiler.addInstruction(new BRA(instanceofLoop));
//
        //// Si le test est vrai
        //compiler.addLabel(instanceofTrue);
        //compiler.addInstruction(new LOAD(new ImmediateInteger(1), register));
//
        //// Si le test est faux
        //compiler.addLabel(instanceofFalse);
        //compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));
//
        //// Fin de l'instruction
        //Label end = new Label("instanceof_end");
        //compiler.addLabel(end);
//
        //// Libérer les registres
        //compiler.getMangerR().freeRegister(objectReg);
        //
//
         
    }
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        expr.decompile(s);
        s.print(" instanceof ");
        type.decompile(s);
        s.print(")");
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);  
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
        type.iter(f);
    }
}
