package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Concrete implementation of method body
 */
public class MethodBody extends AbstractMethodBody {
    private final ListDeclVar declVariables;
    private final ListInst instructions;
    private boolean hasReturn = false;

    public MethodBody(ListDeclVar declVariables, ListInst instructions) {
        this.declVariables = declVariables;
        this.instructions = instructions;
    }

    @Override
    protected void codeGenBody(DecacCompiler compiler) {
       
        
        // Generate code for method instructions
        if (instructions != null) {
            instructions.codeGenListInst(compiler);
        }
    }

    /* @Override
    public int getStackSpace() {
        int space = 0;
        // Space for local variables
        if (declVariables != null) {
            space += declVariables.size();
        }
        // Space for maximum method call parameters in instructions
        if (instructions != null) {
            space += instructions.getMaxMethodCallParams();
        }
        // Add space for temporary calculations (typically 2-3 words)
        space += 3;
        return space;
    } */

    /* @Override
    public int getLocalVarCount() {
        return declVariables != null ? declVariables.size() : 0;
    }

    @Override
    public boolean hasReturn() {
        return hasReturn;
    } */

    /**
     * Called by Return statements during code generation
     */
    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        instructions.prettyPrint(s, prefix, false);
    
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterChildren'");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decompile'");
    }
}
