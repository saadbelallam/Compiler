package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.deca.DecacCompiler;

public class StackManager {
    private int currentStackSize = 0;
    private final int maxStackSize;

    public StackManager(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    public void pushToStack(DecacCompiler compiler, Register register) {
        if (currentStackSize >= maxStackSize) {
            throw new IllegalStateException("Stack overflow");
        }
        compiler.addInstruction(new PUSH(register));
        currentStackSize++;
    }

    public void popFromStack(DecacCompiler compiler, Register register) {
        if (currentStackSize <= 0) {
            throw new IllegalStateException("Stack underflow");
        }
        compiler.addInstruction(new POP((GPRegister) register));
        currentStackSize--;
    }

    public int getCurrentStackSize() {
        return currentStackSize;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }
}
