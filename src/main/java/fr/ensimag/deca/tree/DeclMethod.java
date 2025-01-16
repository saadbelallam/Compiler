package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class DeclMethod extends AbstractDeclMethod{

    private static final Logger LOG = Logger.getLogger(AbstractDeclMethod.class);


    
    final private AbstractIdentifier type;
    final private AbstractIdentifier methodName;
    final private ListDeclParam listParams;
    //final private ListDeclVar listDeclVar;
    //final private ListInst listInst;
    final private AbstractMethodBody body;
    



    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam listParams,AbstractMethodBody body) {
        Validate.notNull(type);
        Validate.notNull(methodName);
        Validate.notNull(listParams);
        Validate.notNull(body);
        //Validate.notNull(listInst);

        this.type = type;
        this.methodName = methodName;
        this.listParams =listParams;
        this.body = body;
        //this.listDeclVar = listDeclVar;
        //this.listInst = listInst;
    }
    
    @Override
    protected void codeGenMethod(DecacCompiler compiler) {
        compiler.getMangerM().resetOffsetParam();
        DeclClass currentClass = (DeclClass) compiler.getCurrentCass();
        String methodN = methodName.getName().getName();
        String ClassName = currentClass.getClassDefinition().getType().getName().getName();
        Label methodLabel = new Label("code." + ClassName + "." + methodN);
        compiler.addLabel(methodLabel);

        int paramCount = listParams.size();
        
        
        compiler.addFirst(new TSTO(paramCount + 16));
        compiler.addFirst(new BOV(new Label("stack_overflow")));
        compiler.addInstruction(new ADDSP(paramCount + 2));

        listParams.codeGenListParm(compiler);
        if (body != null) {
            body.codeGenBody(compiler);
        }

        if (!type.getName().toString().equals("void")) {
            compiler.addInstruction(new WSTR("Error: missing return statement in non-void method"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
        Label endLabel = new Label("fin." + ClassName+ "." + methodName);
        compiler.addLabel(endLabel);

        compiler.addInstruction(new SUBSP(paramCount + 2));
        compiler.addInstruction(new RTS());
    }


    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyDeclMethod'");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decompile'");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'prettyPrintChildren'");
        type.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        listParams.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterChildren'");
    }

    public String getName() {
        return methodName.getName().getName();
        }

    /* @Override
    protected void codeGenMethod(DecacCompiler compiler) {
        // Generate unique label for this method
        Label methodLabel = getMethodLabel();
        compiler.addLabel(methodLabel);

        // Save registers that will be used
        compiler.addInstruction(new PUSH(compiler.getRegisterManager().getScratchRegister(1)));

        // Generate code for method parameters
        params.codeGenParams(compiler);

        // Generate method body
        body.codeGenBody(compiler);

        // Restore registers
        compiler.addInstruction(new POP(compiler.getRegisterManager().getScratchRegister(1)));

        // Return from method
        compiler.addInstruction(new RTS());
    } */

   /* @Override
    public ClassDefinition getClassDefinition() {
        return classDefinition;
    }

    public void setClassDefinition(ClassDefinition classDef) {
        this.classDefinition = classDef;
    } */

    
}
