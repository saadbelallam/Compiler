/**
 * Utilities used for IMA code generation.
 *
 * The code generation itself is implement as methods of the {@link fr.ensimag.deca.tree.Tree}
 * class.
 *
 * @author gl24
 * @date 01/01/2025
 */
package fr.ensimag.deca.codegen;

import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.DAddr;
import static fr.ensimag.ima.pseudocode.Register.getR;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;



public class ManagerOfMemory {
    private int currentOffset;
    private static final int WORD_SIZE = 1; // 32-bit words
    private static final int STACK_RESERVE = 16; // Reserve space for register spilling

    public ManagerOfMemory() {
        this.currentOffset = 1;
    }

    public RegisterOffset allocateVariable() {
        RegisterOffset location = new RegisterOffset(currentOffset, Register.GB);
        currentOffset += WORD_SIZE;
        return location;
    }

    public RegisterOffset allocateStack() {
        currentOffset -= WORD_SIZE;
        return new RegisterOffset(currentOffset, Register.SP);
    }
    
    public void initializeStack() {
        // Reserve space for register spilling
        currentOffset -= STACK_RESERVE * WORD_SIZE;
    }

    public int getCurrentOffset() {
        return currentOffset;
    }

    public int getTotalSpace() {
        return Math.abs(currentOffset - 1);
    }
}
