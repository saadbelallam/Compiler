package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.QUO;


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



   @Override 
    public void codeGenVal(DecacCompiler compiler, GPRegister register) {
        // Vérification si on est dans le cas n = MAX
    if (compiler.getMangerR().getnbOfUsedRegisiters() == compiler.getMaxRegisters() - 1) {
        // Cas où les registres sont saturés
        compiler.getMangerR().MaxRegCase(compiler, getLeftOperand(), getRightOperand(), register);

        // Vérification de la division par zéro (float ou int)
        if (getType().isFloat()) {
            float EPSILON = 1e-6f;
            compiler.addInstruction(new LOAD(new ImmediateFloat(EPSILON), Register.getR(1)));
            compiler.addInstruction(new CMP(Register.getR(1), Register.getR(0)));
            //manque label
        } else {
            compiler.addInstruction(new CMP(0, Register.getR(0)));
            // Manque label
        }

        //la division
        if (getType().isFloat()) {
            compiler.addInstruction(new DIV(Register.getR(0), register));
            
        } else {
            compiler.addInstruction(new QUO(Register.getR(0), register));
        }

        //OV error handling for float
        addBOVinstruction(compiler, compiler.getMangerL().getOverflowLabel());
            
    } else {
        // Cas normal
        GPRegister tempReg = compiler.getMangerR().allocateRegister();
        compiler.addComment("allocate R" + tempReg.getNumber());


        try {
            // Evaluation de right et left
            getLeftOperand().codeGenVal(compiler, register);
            getRightOperand().codeGenVal(compiler, tempReg);

            // Vérification de la division par zéro (float ou int)
            if (getType().isFloat()) {
                float EPSILON = 1e-6f;
                compiler.addInstruction(new LOAD(new ImmediateFloat(EPSILON), compiler.getMangerR().getScratchRegister(0)));
                compiler.addInstruction(new CMP(compiler.getMangerR().getScratchRegister(0), tempReg));
                // Manque label
            } else {
                compiler.addInstruction(new CMP(0, tempReg));
                // Manque label
            }

            //la division
            if (getType().isFloat()) {
                compiler.addInstruction(new DIV(tempReg, register));
            } else {
                compiler.addInstruction(new QUO(tempReg, register));
            }
            //OV error handling for float
            addBOVinstruction(compiler, compiler.getMangerL().getOverflowLabel());
        } finally {
            // Libération du registre temporaire
            compiler.addComment("release R" + tempReg.getNumber());
            compiler.getMangerR().freeRegister(tempReg);
        }
    }
    }
}
