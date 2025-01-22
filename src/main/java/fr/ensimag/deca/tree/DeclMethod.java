package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

public class DeclMethod extends AbstractDeclMethod {
    private static final Logger LOG = Logger.getLogger(AbstractDeclMethod.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier methodName;
    final private ListDeclParam listParams;
    final private AbstractMethodBody body;
    

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam listParams,AbstractMethodBody body) {
        Validate.notNull(type);
        Validate.notNull(methodName);
        Validate.notNull(listParams);
        Validate.notNull(body);


        this.type = type;
        this.methodName = methodName;
        this.listParams =listParams;
        this.body = body;

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
        
        int d2 = body.getLocalVarCount() + paramCount;
    
        // d1 = d2 + register saves + method call overhead + expression depth
        int d1 = d2 + 
                compiler.getMangerR().getnbOfUsedRegisiters() +
                + body.getMaxExpressionDepth() ;
            
        compiler.addInstruction(new TSTO(d1));
        compiler.addInstruction(new BOV(new Label("stack_overflow_error")));
        compiler.addInstruction(new ADDSP(d2));
       
        compiler.getMangerR().SaveRegIntoStack(compiler);

        listParams.codeGenListParam(compiler);

        Label endLabel = new Label("fin." + ClassName+ "." + methodName);

        if (body != null) {
            body.setLabelLabel(endLabel);
            body.codeGenBody(compiler);
        }
        
        compiler.addLabel(endLabel);
        
        compiler.getMangerR().RestoreSavedRegisters(compiler);
        compiler.addInstruction(new RTS());
    }


    @Override
    protected void verifyDeclMethodMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        // Récupérer l'environnement des types
        EnvironmentType envTypes = compiler.environmentType;
       
        // Récupérer l'environnement des expressions de la super-classe (si elle existe)
        ClassDefinition superClass = currentClass.getSuperClass();
        EnvironmentExp envExpSuper = (superClass != null) ? superClass.getMembers() : null;

        // Vérification du type et de la signature de la méthode
        Type methodType = this.type.verifyType(compiler);
        Signature methodSignature = this.listParams.verifyListParamMembers(compiler, currentClass);

        // Vérification si la méthode est déjà définie dans la super-classe
        Definition superMethodDef = (envExpSuper != null) ? envExpSuper.get(methodName.getName()) : null;

        if (superMethodDef == null) {
            // La méthode n'est pas définie dans la super-classe : création d'une nouvelle méthode
            currentClass.incNumberOfMethods();
            int methodIndex = currentClass.getNumberOfMethods();
            MethodDefinition methodDef = new MethodDefinition(methodType, this.getLocation(), methodSignature, methodIndex);
            try {
                currentClass.getMembers().declare(methodName.getName(), methodDef);
            } catch (EnvironmentExp.DoubleDefException e) {
                throw new ContextualError("The method " + methodName + " is already defined in class : " + currentClass.getType() + " as a " + currentClass.getMembers().get(methodName.getName()), this.getLocation());
            }
            this.methodName.setDefinition(methodDef);
            this.methodName.setType(methodType);
            return;
        }

       

        // Si une méthode existe dans la super-classe, vérifier les règles de redéfinition
        if (!(superMethodDef instanceof MethodDefinition)) {
            throw new ContextualError("The method" + methodName + " cannot be redefined in class : " + currentClass.getType(), this.getLocation());
        }

        // Vérifier la signature
        Signature superSignature = ((MethodDefinition) superMethodDef).getSignature();
        if (!methodSignature.equals(superSignature)) {
            throw new ContextualError("Redefinition of " + methodName + " with different signature in class : " + currentClass.getType(), this.getLocation());
        }

        // Vérifier le type de retour
        Type superMethodType = superMethodDef.getType();
        if (!envTypes.subType(methodType, superMethodType)) {
            throw new ContextualError("Return type of " + methodName + " in class" + currentClass.getType() + " is not compatible with super class", this.getLocation());
        }

        // La méthode redéfinie hérite de l'index de la méthode parent
        int superMethodIndex = ((MethodDefinition) superMethodDef).getIndex();
        MethodDefinition methodDef = new MethodDefinition(methodType, this.getLocation(), methodSignature, superMethodIndex);
        try {
            currentClass.getMembers().declare(methodName.getName(), methodDef);
            
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("The method " + methodName + " is already defined in class : " + currentClass.getType(), this.getLocation());
        }
        this.methodName.setDefinition(methodDef);
        this.methodName.setType(methodType);
    }


    @Override
    protected void verifyDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify declMethodBody: start");
        EnvironmentExp envExpParams = listParams.verifyListParamBody(compiler, localEnv, currentClass);
        body.verifyMethodBody(compiler, envExpParams, currentClass, type.getType());  
        
        if (!(body instanceof MethodAsmBody)) {
            if (!(type.getName() == compiler.createSymbol("void")) && !this.body.hasReturn()) {
                throw new ContextualError("The method " + methodName + " must have a return statement in class : " + currentClass.getType(), this.getLocation());
            }
        }
        LOG.debug("verify declMethodBody: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        listParams.decompile(s);
        s.print(") ");
        body.decompile(s);
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
        type.iter(f);
        methodName.iter(f);
        listParams.iter(f);
        body.iter(f);
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


/*  if (!type.getName().toString().equals("void")) {
            compiler.addInstruction(new WSTR("Error: missing return statement in non-void method"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        } */