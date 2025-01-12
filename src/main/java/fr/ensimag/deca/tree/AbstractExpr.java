package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl24
 * @date 01/01/2025
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,  Type expectedType) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        Type type = this.verifyExpr(compiler, localEnv, currentClass);
        
        if (!type.sameType(expectedType)) {
            if (type.isInt() && expectedType.isFloat()) {
                ConvFloat convFloat = new ConvFloat(this);
                convFloat.verifyExpr(compiler, localEnv, currentClass);
                return convFloat;
            }
            throw new ContextualError("Type mismatch: expected " + expectedType + " but found " + type, this.getLocation());
        }
        
        return this;
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        this.verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        Type conditionType = this.verifyExpr(compiler, localEnv, currentClass);

        if (!conditionType.isBoolean()) {
            throw new ContextualError("Condition must be of type boolean, found: " + conditionType, this.getLocation());
        }
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {
         GPRegister tmpReg = compiler.getMangerR().getScratchRegister(1); 
        this.codeGenVal(compiler, tmpReg);
        // 3. Print based on type
        switch (this.getType().toString()) {
            case "int":
                compiler.addInstruction(new WINT());
                break;
            case "float":
                compiler.addInstruction(new WFLOAT());
                break;
            case "boolean":
                // For boolean, we typically print "true" or "false"
                Label endLabel = new Label("print_end_" + compiler.getMangerL().newLabel());
                Label falseLabel = new Label("print_false_" + compiler.getMangerL().newLabel());
                
                // Compare with 0 (false)
                compiler.addInstruction(new CMP(0, tmpReg));
                compiler.addInstruction(new BEQ(falseLabel));
                
                compiler.addInstruction(new WSTR(new ImmediateString("true")));
                compiler.addInstruction(new BRA(endLabel));

                compiler.addLabel(falseLabel);
                compiler.addInstruction(new WSTR(new ImmediateString("false")));
                
                compiler.addLabel(endLabel);
                break;
            default:
                throw new UnsupportedOperationException("Print not implemented for type: " + this.getType());
        }
}   
    /**
     * Generate code to printlnx the expression
     *
     * @param compiler
     */
    protected void codeGenPrintLnx(DecacCompiler compiler){
        GPRegister tmpReg = compiler.getMangerR().getScratchRegister(1); 
        this.codeGenVal(compiler,tmpReg);
        switch (this.getType().toString()) {
            case "float" :
                compiler.addInstruction(new WFLOATX());
                break;
            case "int"   :
                compiler.addInstruction(new WINT());
                break;
            case "boolean"  :
                 Label endLabel = new Label("print_end_" + compiler.getMangerL().newLabel());
                Label falseLabel = new Label("print_false_" + compiler.getMangerL().newLabel());
                
                // Compare with 0 (false)
                compiler.addInstruction(new CMP(0, tmpReg));
                compiler.addInstruction(new BEQ(falseLabel));
                
                compiler.addInstruction(new WSTR(new ImmediateString("true")));
                compiler.addInstruction(new BRA(endLabel));

                compiler.addLabel(falseLabel);
                compiler.addInstruction(new WSTR(new ImmediateString("false")));
                
                compiler.addLabel(endLabel);
                
                break;
            default:
                throw new UnsupportedOperationException("Print not implemented for type: " + this.getType());
        }
    }


    /* @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    } */
    
    

    // In AbstractExpr class
    public  void codeGenVal(DecacCompiler compiler, GPRegister reg){
        throw new UnsupportedOperationException("not yet implemented");
    }
    
 

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

    public DVal getVal(DecacCompiler compiler){
        return null;
    }
     

    public void codeGenCondition(DecacCompiler compiler, Label label, boolean jump) {
        
        GPRegister reg = compiler.getMangerR().allocateRegister();
        
        // Generate code to evaluate expression into reg (to compute the expression's bool results)
        this.codeGenVal(compiler, reg); 

       // rebuild the way it hadnles codeGenCond its also CodeGenVal ?
    
        // Compare result with 0
        compiler.addInstruction(new CMP(new ImmediateInteger(0), reg));
        
        // Branch based on jump parameter
        if (jump) {
            // Branch if not equal to 0 (condition is true)
            compiler.addInstruction(new BNE(label));
        } else {
            // Branch if equal to 0 (condition is false)
            compiler.addInstruction(new BEQ(label));
        }
        compiler.getMangerR().freeRegister(reg);   
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler){
        // Get a temporary register
        GPRegister reg = compiler.getMangerR().allocateRegister();
        
        // Generate code for the expression
        this.codeGenVal(compiler, reg);
        
        // Note: The result is computed but discarded since it's not used
        // This matches the semantics of an expression statement
    }


}
