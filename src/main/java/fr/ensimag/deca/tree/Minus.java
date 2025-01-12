package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SUB;


/**
 * @author gl24
 * @date 01/01/2025
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

   /* @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        //<codeExp( op[ e1 e2] , n) > avec <d v a l ( e2) >=⊥ e t n=MAX
        if (compiler.getMangerR().getnbOfUsedRegisiters() == compiler.getMaxRegisters()-1){
            // méthode de l'algorithe voir la classe 
            // ManagerOfRegisters
            compiler.getMangerR().MaxRegCase(compiler,getLeftOperand(), getRightOperand(), destReg);
            compiler.addInstruction(new SUB(Register.getR(0), destReg));
            //OV error handling for float
            if(getType().isFloat()){
                addBOVinstruction(compiler, compiler.getMangerL().getOverflowLabel());
            }
            
        }
        // Cad n < Max
        else {
            // Generation du code pour left
            getLeftOperand().codeGenVal(compiler, destReg);
            // Generate code for right operand in next register
            GPRegister rightReg = compiler.getMangerR().allocateRegister();
            getRightOperand().codeGenVal(compiler, rightReg);
            // Perform subtraction
            compiler.addInstruction(new SUB(rightReg, destReg));
            //OV error handling for float
            if(getType().isFloat()){
                addBOVinstruction(compiler, compiler.getMangerL().getOverflowLabel());
            }
            // Free the right register
            compiler.getMangerR().freeRegister(rightReg);
        }
    } */

    @Override
    public void addArithInsruction(DecacCompiler compiler,GPRegister srctReg , GPRegister dstReg){
            compiler.addInstruction(new SUB(srctReg, dstReg));
    }
    
}
