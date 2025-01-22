package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Concrete implementation of method body
 */
public class MethodBody extends AbstractMethodBody {
    private final ListDeclVar declVariables;
    private final ListInst instructions;
    private boolean hasReturn = false;
    private Label returnLabel;

    public MethodBody(ListDeclVar declVariables, ListInst instructions) {
        this.declVariables = declVariables;
        this.instructions = instructions;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        this.declVariables.verifyListDeclVariable(compiler, localEnv, currentClass);
        this.instructions.verifyListInst(compiler, localEnv, currentClass, returnType);

        // check s'il y a un return
        for (AbstractInst inst : instructions.getList()) {
            if (inst instanceof Return) {
                this.setHasReturn(true);
            }
            if (inst instanceof IfThenElse) {
                for (AbstractInst inst1 : ((IfThenElse) inst).getThenBranch().getList()) {
                    if (inst1 instanceof Return) {
                        this.setHasReturn(true);
                    }
                }
                if (((IfThenElse) inst).getElseBranch() != null) {
                    for (AbstractInst inst2 : ((IfThenElse) inst).getElseBranch().getList()) {
                        if (inst2 instanceof Return) {
                            this.setHasReturn(true);
                        }
                    }
                }
            }
            if (inst instanceof While) {
                for (AbstractInst inst1 : ((While) inst).getBody().getList()) {
                    if (inst1 instanceof Return) {
                        this.setHasReturn(true);
                    }
                }
            }
        }
    }

    @Override
    protected void codeGenBody(DecacCompiler compiler) {
      if(declVariables != null){
         for (AbstractDeclVar var : declVariables.getList()) {
            if (var instanceof DeclVar) {
                DeclVar declVar = (DeclVar) var;
             declVar.codeGenLocalVar(compiler);
             }
        }
        if (instructions != null) {
            for (AbstractInst inst : instructions.getList()) {
                if (inst instanceof Return) {
                    ((Return)inst).setReturnLabel(returnLabel);
                    this.setHasReturn(true);
                }
                inst.codeGenInst(compiler);
            }
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
    
    @Override
    public boolean hasReturn() {
        return hasReturn;
    }

    /**
     * Called by Return statements during code generation
     */
   
    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }
    
    @Override
    public void setLabelLabel(Label returnLabel){
        this.returnLabel = returnLabel;
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        instructions.prettyPrint(s, prefix, false);
    
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        instructions.iter(f);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        instructions.decompile(s);
        s.unindent();
        s.println("}");
    }

   @Override
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
