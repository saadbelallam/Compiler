package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

/**
 * Full if/else if/else statement.
 *
 * @author gl24
 * @date 01/01/2025
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    //Getters pour le parser
    
    // Getter pour la condition
    public AbstractExpr getCondition() {
        return condition;
    }

    // Getter pour la branche "then"
    public ListInst getThenBranch() {
        return thenBranch;
    }

    // Getter pour la branche "else"
    public ListInst getElseBranch() {
        return elseBranch;
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label falseLabel = new Label("else_" + compiler.getMangerL().newLabel());
        Label endLabel = new Label("endif_" + compiler.getMangerL().newLabel());
        
        // Generate condition code
        condition.codeGenCondition(compiler, falseLabel,false);
        
        // Generate then branch
        thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(endLabel));
        
        // Generate else branch
        compiler.addLabel(falseLabel);
        elseBranch.codeGenListInst(compiler);
        
        compiler.addLabel(endLabel);
}

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
