package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }


    /*méthode incomplète, elle donne pas la sortie attendue (à compléter) on a toujours
     * une erreur de division par zero
     */
    @Override
    public void codeGenInst(DecacCompiler compiler) {
        // Gestion des registres
        RegisterManager registerManager = compiler.getRegisterManager();
        // Registres pour les opérandes
        GPRegister leftRegister = (GPRegister) registerManager.getRegister();
        GPRegister rightRegister = (GPRegister) registerManager.getRegister();
        try {
    
        // Charge le leftOperand dans leftRegister
        if (getLeftOperand() instanceof AbstractIdentifier) {
            AbstractIdentifier leftIdentifier = (AbstractIdentifier) getLeftOperand();
            VariableDefinition leftVarDef = (VariableDefinition) leftIdentifier.getDefinition();
            DAddr leftOperandAddr = leftVarDef.getOperand();
            compiler.addInstruction(new LOAD(leftOperandAddr, leftRegister));
        } else {
            getLeftOperand().codeGenExpr(compiler, leftRegister); // Si ce n'est pas un identifiant, évaluer l'expression
        }

        // Charge le rightOperand dans rightRegister
        if (getRightOperand() instanceof AbstractIdentifier) {
            AbstractIdentifier rightIdentifier = (AbstractIdentifier) getRightOperand();
            VariableDefinition rightVarDef = (VariableDefinition) rightIdentifier.getDefinition();
            DAddr rightOperandAddr = rightVarDef.getOperand();
            compiler.addInstruction(new LOAD(rightOperandAddr, rightRegister));
        } else {
            getRightOperand().codeGenExpr(compiler, rightRegister); // Si ce n'est pas un identifiant, évaluer l'expression
        }
        
        //La division peut etre entière ou division pour les flottants
        // Vérifier les types des opérandes
        Type leftType = getLeftOperand().getType();
        Type rightType = getRightOperand().getType();

        // Vérification de la division par zéro
        compiler.addInstruction(new CMP(0, rightRegister));
        Label divZeroLabel = new Label("div_zero");
        compiler.addInstruction(new BEQ(divZeroLabel));
        System.out.println("Vérification de la division par zéro");
        // Définir l'étiquette div_zero dans le programme
        compiler.addLabel(divZeroLabel);
        compiler.addInstruction(new WSTR("Error: Division by zero"));
        compiler.addInstruction(new ERROR());

        if (leftType.isInt() && rightType.isInt()) {
            // Division entière
            compiler.addInstruction(new QUO(leftRegister, rightRegister)); // QUO dval, Rm : Rm <- V[Rm] / V[dval]  
            System.out.println("QUO");
        } else if (leftType.isFloat() || rightType.isFloat()) {
            // Division flottante
            //DIV dval, Rm : Rm <- V[Rm] / V[dval]
            compiler.addInstruction(new DIV(leftRegister, rightRegister)); // Résultat dans leftRegister
            System.out.println("DIV");

        } else {
            // Gestion des erreurs de type (non nécessaire si le typage est vérifié avant)
            throw new UnsupportedOperationException("Erreur de type: pour la division on accepte que int ou float");
        }
    } finally {
        // Libéreation des registres
        registerManager.releaseRegister(leftRegister);
        registerManager.releaseRegister(rightRegister);
    }
    }


}
