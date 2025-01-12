package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl24
 * @date 01/01/2025
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if ((type1.isInt() || type1.isFloat()) && (type2.isInt() || type2.isFloat())) {

            if (type1.isFloat() || type2.isFloat()) {
                // Convert operands to float if one of them is float
                if (type1.isInt()) {
                    this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
                    this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                }
                if (type2.isInt()) {
                    this.setRightOperand(new ConvFloat(this.getRightOperand()));
                    this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                }
            }
        } else if (type1.isBoolean() && type2.isBoolean()) {
              // nothing
        } else {
            throw new ContextualError(
                "Operands must be of the same type and either numeric (int/float) or Boolean",
                this.getLocation()
            );
        }

        Type type = compiler.environmentType.BOOLEAN;
        this.setType(type);
        return type;
    }

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        // Create labels for control flow
        Label falseLabel = new Label("cmp_false_" + compiler.getMangerL().newLabel());
        Label endLabel = new Label("cmp_end_" + compiler.getMangerL().newLabel());
        
        // Load true (1) by default
        compiler.addInstruction(new LOAD(1, destReg));
        
        // Use codeGenCondition - if comparison is false, jump to falseLabel
        codeGenCondition(compiler, falseLabel, false);
        
        // Skip the false case if comparison was true
        compiler.addInstruction(new BRA(endLabel));
 
        // False case - load 0
        compiler.addLabel(falseLabel);
        compiler.addInstruction(new LOAD(0, destReg));
        
        compiler.addLabel(endLabel);
    }

    public abstract void addBranchInstruction(DecacCompiler compiler, Label destLabel,boolean  jump);

}
