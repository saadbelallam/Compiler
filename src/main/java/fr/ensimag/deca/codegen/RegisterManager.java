package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.GPRegister;
import java.util.Stack;

public class RegisterManager {
    private final int maxRegisters;
    private final Stack<Integer> freeRegisters;
    private int globalOffset = 1;

    public RegisterManager(int maxRegisters) {
        this.maxRegisters = maxRegisters;
        this.freeRegisters = new Stack<>();
        for (int i = maxRegisters; i >1; i--) {
            freeRegisters.push(i);
        }
    }

    public Register getRegister() {
        if (freeRegisters.isEmpty()) {
            throw new IllegalStateException("No registers available");
        }
        System.out.println("getRegister");
        return Register.getR(freeRegisters.pop());
    }

    public void releaseRegister(Register register) {
        int regNumber = ((GPRegister) register).getNumber();
        if (regNumber > 0 && regNumber <= maxRegisters) {
            freeRegisters.push(regNumber);
        } else {
            throw new IllegalArgumentException("Invalid register: " + register);
        }
    }

    public int getNextGlobalOffset() {
        return globalOffset++;
    }

    public RegisterOffset getGlobalOffset() {
        return new RegisterOffset(globalOffset-1, Register.GB);
    }

}