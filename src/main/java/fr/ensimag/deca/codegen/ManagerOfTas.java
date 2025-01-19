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
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.RegisterOffset;




public class ManagerOfTas {
    private int currentOffset;
    private static final int WORD_SIZE = 1; // 32-bit words
    

    public ManagerOfTas() {
        this.currentOffset = 1;
    }

    public RegisterOffset allocateField(Register reg) {
        RegisterOffset location = new RegisterOffset(currentOffset, reg);
        currentOffset += WORD_SIZE;
        return location;
    }

    public int getCurrentOffset() {
        return currentOffset;
    }

    public int getTotalSpace() {
        return Math.abs(currentOffset - 1);
    }
    public void restOffset(){
        this.currentOffset = 1;

    }
}
