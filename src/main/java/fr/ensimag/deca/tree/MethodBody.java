package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
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
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        this.declVariables.verifyListDeclVariable(compiler, localEnv, currentClass);
        this.instructions.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenBody(DecacCompiler compiler) {
      if(declVariables != null){
         for (AbstractDeclVar var : declVariables.getList()) {
            if (var instanceof DeclVar) {
                DeclVar declVar = (DeclVar) var;
             declVar.codeGenLocalVar(compiler);}
       }
       
    
    }}




   /*  @Override
    public int getStackSpace() {
        int space = 0;
        if (declVariables != null) {
            space += declVariables.size();
        }
        return space;
    } */

    @Override
    public int getLocalVarCount() {
        return declVariables != null ? declVariables.size() : 0;
    }
    /*
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

   public int getMaxExpressionDepth() {
        int maxDepth = 0;
        
        if (instructions != null) {
            for (AbstractInst inst : instructions.getList()) {
                if (inst instanceof AbstractExpr) {
                    maxDepth = Math.max(maxDepth, ((AbstractExpr) inst).getMaxExpressionDepth());
                }
            }
        }

        return maxDepth;
    }
}
