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
 *
 * @author gl24
 * @date 01/01/2025
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    /* @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // Create labels for loop
        Label condLabel = new Label("while_cond_" + compiler.getMangerL().newLabel());
        Label startLabel = new Label("while_start_" + compiler.getMangerL().newLabel());
        
        // Jump to condition check first
        compiler.addInstruction(new BRA(condLabel));
        
        // Loop body
        compiler.addLabel(startLabel);
        body.codeGenListInst(compiler);
        
        // Condition check
        compiler.addLabel(condLabel);
        condition.codeGenCondition(compiler, startLabel, true); // Branch to start if condition is true
        
    } */

    /* @Override
    protected void codeGenInst(DecacCompiler compiler) {

        Label whileTest = new Label("while_test." + compiler.getMangerL().newLabel());
        Label whileEnd = new Label("while_end." + compiler.getMangerL().newLabel());
        
        // Add label for condition testing
        compiler.addLabel(whileTest);
        
        // Generate code for condition and branch if false
        condition.codeGenCondition(compiler, whileEnd, false);
        
        // Generate code for loop body
        body.codeGenListInst(compiler);
        
        // Jump back to test condition
        compiler.addInstruction(new BRA(whileTest));
        
        // Add end label
        compiler.addLabel(whileEnd);
    }
 */ 
   @Override
    protected void codeGenInst(DecacCompiler compiler) {
       
        Label whileTest = new Label("while_test_" + compiler.getMangerL().newLabel());
        Label whileBody = new Label("while_body_" + compiler.getMangerL().newLabel());
        Label whileEnd = new Label("while_end_" + compiler.getMangerL().newLabel());
        
        // Jump to test condition first
        compiler.addInstruction(new BRA(whileTest));
        
        // Generate code for while body
        compiler.addLabel(whileBody);
        body.codeGenListInst(compiler);
        
        // Test condition
        compiler.addLabel(whileTest);
        condition.codeGenCondition(compiler, whileEnd, false);
        compiler.addInstruction(new BRA(whileBody));
        
        // End of while
        compiler.addLabel(whileEnd);
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
