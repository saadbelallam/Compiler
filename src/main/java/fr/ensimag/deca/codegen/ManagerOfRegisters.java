package fr.ensimag.deca.codegen;

import java.util.Stack;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;

public class ManagerOfRegisters {
    private static final int FIRST_REG = 2;
    private int maxReg;
    private Stack<GPRegister> registerStack;
    private boolean[] usedRegisters;
    private int nbOfUsedRegisiters = 0;
    private int nbTemporaires = 0;
    private int maxTemporaires = 0;
    

    public ManagerOfRegisters(int maxReg) {
        this.maxReg = maxReg;
        //System.out.println("Manager: "+this.maxReg);
        this.registerStack = new Stack<>();
        this.usedRegisters = new boolean[maxReg + 1];
        // Initialize register stack with available registers (R15 to R2)
        for (int i = maxReg; i >= FIRST_REG; i--) {
            registerStack.push(Register.getR(i));
        }
    }

    public GPRegister allocateRegister() {
        if (registerStack.isEmpty()) {
            System.err.println("Register stack overflow");
        }
        GPRegister reg = registerStack.pop();
        usedRegisters[reg.getNumber()] = true;
        nbOfUsedRegisiters++;
        return reg;
    }

    public void freeRegister(GPRegister register) {
        int regNum = register.getNumber();
        if (regNum >= FIRST_REG && regNum <= maxReg && usedRegisters[regNum]) {
            usedRegisters[regNum] = false;
            nbOfUsedRegisiters--;
            registerStack.push(register);
        }
    }

    public GPRegister getScratchRegister(int index) {
        if (index != 0 && index != 1) {
            throw new IllegalArgumentException("Scratch register index must be 0 or 1");
        }
        return Register.getR(index);
    }

    public boolean hasAvailableRegisters() {
        return !registerStack.isEmpty();
    }

    public int getnbOfUsedRegisiters(){
        return nbOfUsedRegisiters;
    }

    public void MaxRegCase(DecacCompiler compiler,AbstractExpr leftOperand, AbstractExpr rightOperand, GPRegister destReg) {
        //<codeExp( e1 , n) >
        leftOperand.codeGenVal(compiler, destReg);
        // PUSH Rn ; sauvegarde
        compiler.addInstruction(new PUSH(destReg));
        // réservation d'une case dans la pile pour push
        nbTemporaires++;

        //On cherche le nombre max des push consécutifs avant un pop
        if (nbTemporaires > maxTemporaires) {
            maxTemporaires = nbTemporaires;
        }

        //<codeExp( e2 , n) >
        rightOperand.codeGenVal(compiler,destReg);
        //LOAD Rn, R0
        compiler.addInstruction(new LOAD(destReg,Register.getR(0)));
        // POP Rn ; restauration
        compiler.addInstruction(new POP(destReg));
        // libération d'une case dans la pile si pop est directement après push
        nbTemporaires--;
    }

    public int getMaxTemporaires(){
            return maxTemporaires;
    }

}
