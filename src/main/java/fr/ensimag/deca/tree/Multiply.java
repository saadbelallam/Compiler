package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.MUL;


/**
 * @author gl24
 * @date 01/01/2025
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }


    /* @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        //<codeExp( op[ e1 e2] , n) > avec <d v a l ( e2) >=⊥ e t n=MAX
        //System.out.println("MaxRegisters()" + compiler.getMaxRegisters());
        //System.out.println("nbOfUsedRegisiters" + compiler.getMangerR().getnbOfUsedRegisiters());
        if (compiler.getMangerR().getnbOfUsedRegisiters() == compiler.getMaxRegisters() - 1){
            //System.out.print("entré mult:" + destReg);
            // méthode de l'algorithe voir la classe 
            // ManagerOfRegisters
            compiler.getMangerR().MaxRegCase(compiler,getLeftOperand(), getRightOperand(), destReg);
            compiler.addInstruction(new MUL(Register.getR(0), destReg));
            //OV error handling for float
            if(getType().isFloat()){
                addBOVinstruction(compiler, compiler.getMangerL().getOverflowLabel());
            }
        }
        // Cad n < Max
        else {
            //System.out.print("entré else mult:" + destReg);
            // Generation du code pour left
            getLeftOperand().codeGenVal(compiler, destReg);
            // Generation du code pour right dans le registre n+1
            GPRegister rightReg = compiler.getMangerR().allocateRegister();
            getRightOperand().codeGenVal(compiler, rightReg);
            // ADD
            compiler.addInstruction(new MUL(rightReg, destReg));
            //OV error handling for float
            if(getType().isFloat()){
                addBOVinstruction(compiler, compiler.getMangerL().getOverflowLabel());
            }
            // libération du rightReg
            compiler.getMangerR().freeRegister(rightReg);
        }
    } */

   @Override
    public void addArithInsruction(DecacCompiler compiler,GPRegister srctReg , GPRegister dstReg){
            compiler.addInstruction(new MUL(srctReg, dstReg));
    }

}
