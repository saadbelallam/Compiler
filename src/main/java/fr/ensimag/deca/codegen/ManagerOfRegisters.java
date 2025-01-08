package fr.ensimag.deca.codegen;

import java.util.Stack;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

public class ManagerOfRegisters {
    private static final int FIRST_REG = 2;
    private static final int LAST_REG = 15;
    private Stack<GPRegister> registerStack;
    private boolean[] usedRegisters;
    private int nbOfUsedRegisiters = 0;

    public ManagerOfRegisters() {
        this.registerStack = new Stack<>();
        this.usedRegisters = new boolean[LAST_REG + 1];
        // Initialize register stack with available registers (R15 to R2)
        for (int i = LAST_REG; i >= FIRST_REG; i--) {
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
        if (regNum >= FIRST_REG && regNum <= LAST_REG && usedRegisters[regNum]) {
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

}
