package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

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
        // throw new UnsupportedOperationException("not yet implemented");
        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    
        if (!type1.isInt() && !type1.isFloat()) {
            throw new ContextualError("Left operand must be of type int or float", this.getLeftOperand().getLocation());
        }
        if (!type2.isInt() && !type2.isFloat()) {
            throw new ContextualError("Right operand must be of type int or float", this.getRightOperand().getLocation());
        }
        if (type1.isFloat() || type2.isFloat()) {
            // si gauche int et droite float: convertir gauche en float
            if (type1.isInt()) {
                this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
                this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            }
            // si droite int et gauche float: convertir droite en float
            if (type2.isInt()) {
                this.setRightOperand(new ConvFloat(this.getRightOperand()));
                this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            }

        }
        Type type = compiler.environmentType.BOOLEAN    ;
        this.setType(type);
        return type;
    }
    

}
