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
    
    public void setElseBranch(ListInst elseBranch) {
        Validate.notNull(elseBranch);
        this.elseBranch = elseBranch;
    }

    public void setElseBranch(AbstractInst elseBranch) {
        Validate.notNull(elseBranch);
        this.elseBranch.add(elseBranch);
    }

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
        // Create labels for else and end blocks
        Label elseLabel = new Label("E_Sinon." + compiler.getMangerL().newLabel());
        Label endLabel = new Label("E_Fin." + compiler.getMangerL().newLabel());
        
        // Generate code for condition with false branch to else label
        // Using faux (false) as specified in the rules
        condition.codeGenCondition(compiler, elseLabel, false);
        
        // Generate code for then branch
        thenBranch.codeGenListInst(compiler);
        
        // Skip else branch when then branch completes
        compiler.addInstruction(new BRA(endLabel));
        
        // Generate else label and branch
        compiler.addLabel(elseLabel);
        if (elseBranch != null) {
            elseBranch.codeGenListInst(compiler);
        }
        
        // End label
        compiler.addLabel(endLabel);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        // throw new UnsupportedOperationException("not yet implemented");
        s.print("if (");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.println("} else {");
        s.indent();
        elseBranch.decompile(s);
        s.unindent();
        s.print("}");
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
