package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.REM;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Modulo extends AbstractOpArith {
    private static final Logger LOG = Logger.getLogger(AbstractOpArith.class);


    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify modulo: start");
        Type type1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!type1.isInt() || !type2.isInt()) {
            throw new ContextualError("Operands of modulo should be of type int", getLocation());
        }
        setType(type1);
        LOG.debug("verify modulo: end");
        return type1;
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }


     @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        //<codeExp( op[ e1 e2] , n) > avec <d v a l ( e2) >=⊥ e t n=MAX
        if (compiler.getMangerR().getnbOfUsedRegisiters() == compiler.getMaxRegisters()){
            // méthode de l'algorithe voir la classe 
            // ManagerOfRegisters
            compiler.getMangerR().MaxRegCase(compiler,getLeftOperand(), getRightOperand(), destReg);
            compiler.addInstruction(new REM(Register.getR(0), destReg));
        }
        // Cad n < Max
        else {
            // Generation du code pour left
            getLeftOperand().codeGenVal(compiler, destReg);
            // Generation du code pour right dans le registre n+1
            GPRegister rightReg = compiler.getMangerR().allocateRegister();
            getRightOperand().codeGenVal(compiler, rightReg);
            // Opération 
            compiler.addInstruction(new REM(rightReg, destReg));
            // libération du rightReg
            compiler.getMangerR().freeRegister(rightReg);
        }
        // Gérer la division par zéro directement via OV
        Label divByZeroError = compiler.getMangerL().getDivByZeroLabel();
        addBOVinstruction(compiler, divByZeroError);
    }
    }

