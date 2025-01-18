package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.RTS;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl24
 * @date 01/01/2025
 */
public class DeclClass extends AbstractDeclClass {
    private final AbstractIdentifier name;
    private final AbstractIdentifier superClass;
    private final ListDeclField fields;
    private final ListDeclMethod methods;

    public DeclClass(AbstractIdentifier name, AbstractIdentifier superClass,
    ListDeclMethod methods, ListDeclField fields) {
        this.name = name;
        this.superClass = superClass;
        this.fields = fields;
        this.methods = methods;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        //System.out.println("superclass" + superClass.getName());
        ClassDefinition superClassDef = null;
    
        if (superClass != null) {
            // Verify superclass exists in environment for the super class
            Definition superDef = compiler.environmentType.get(superClass.getName());
            if (superDef == null) {
                throw new ContextualError("Superclass " + superClass.getName() + " is not defined", 
                    superClass.getLocation());
            }
            if (!(superDef instanceof ClassDefinition)) {
                throw new ContextualError("Superclass " + superClass.getName() + " is not a class", 
                    superClass.getLocation());
            }
            superClassDef = (ClassDefinition) superDef;
            superClass.setDefinition(superClassDef);
        } else {
            superClassDef = (ClassDefinition) compiler.getEnvironmentType().get(
                compiler.createSymbol("Object"));
        }

        if (name.getName().equals(superClass.getName())) {
            throw new ContextualError("Class " + name.getName() + " cannot extend itself", 
                name.getLocation());
        }
        
        if (name.getName().getName().equals(superClass.getName().getName())) {
            throw new ContextualError("Class " + name.getName() + " cannot extend itself", name.getLocation());
        }

        ClassType type = new ClassType(name.getName(), this.getLocation(), superClass.getClassDefinition());
        ClassDefinition currentClass = new ClassDefinition(type, this.getLocation(), superClass.getClassDefinition());
        if (compiler.environmentType.get(name.getName()) != null) {
            throw new ContextualError("Class " + name.getName() + " already exists", name.getLocation());
        }
        
        
        name.setDefinition(currentClass);
        name.setType(type);
        compiler.environmentType.declareType(name.getName(), currentClass);

    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        ClassDefinition currentClass = name.getClassDefinition();
        ClassDefinition superClassDef = superClass.getClassDefinition();
        currentClass.setNumberOfFields(superClassDef.getNumberOfFields());
        currentClass.setNumberOfMethods(superClassDef.getNumberOfMethods());

        fields.verifyListDeclFieldMembers(compiler, name.getClassDefinition());
        methods.verifyListDeclMethodMembers(compiler, name.getClassDefinition());
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        fields.verifyListDeclFieldBody(compiler, this.getClassDefinition().getMembers(), name.getClassDefinition());
        methods.verifyListDeclMethodBody(compiler, this.getClassDefinition().getMembers(), name.getClassDefinition());
    }


    @Override
    public void codeGenClass(DecacCompiler compiler) {
        compiler.setCurrentClass(this);
        String className = name.getName().getName();

        System.err.println("pointer on the VMT is : " + this.getClassDefinition().getVMTablesAddr());
        
        // 1. Generate initialization code for fields
        generateInitialization(compiler, className);
        
        // 2. Generate code for methods
        //generateMethods(compiler, className);
    }

    private void generateInitialization(DecacCompiler compiler, String className) {
        Label initLabel = new Label("init." + className);
        compiler.addLabel(initLabel);
        compiler.addComment("Initialisation des champs de "+className);
        String superClass = this.getSuperClassName();
        if (superClass == "Object") {
            fields.codeGenListField(compiler);   
        }else{
            fields.codeGenListFieldExtends(compiler);
        }
        compiler.addInstruction(new RTS());


        /* compiler.addFirst(new BOV(new Label("pile_pleine")));
        
        compiler.getMangerR().SaveRegIntoStack(compiler);
        
        if (superClass != null) {
            compiler.addInstruction(new BSR(new Label("init." + superClass.getName().getName())));
        }
    
        for (AbstractDeclField field : fields.getList()) {
            field.codeGenField(compiler);
        } */

        for (AbstractDeclMethod method : methods.getList()) {
            method.codeGenMethod(compiler);
        }
         
       /*  compiler.getMangerR().RestoreSavedRegisters(compiler);
        
        compiler.addInstruction(new RTS()); */
    }



    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // throw new UnsupportedOperationException("Not yet supported");
        name.prettyPrint(s, prefix, false);
        superClass.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // throw new UnsupportedOperationException("Not yet supported");
        name.iter(f);
        superClass.iter(f);
        fields.iter(f);
        methods.iter(f);
    }

    public ClassDefinition getClassDefinition() {
           return name.getClassDefinition();}
    
    public ClassDefinition getSuperClassDefinition(){
        return superClass.getClassDefinition();
    }


    public String getSuperClassName(){
        return superClass.getName().getName();
    }


    public AbstractIdentifier getSuperClass() {
        return superClass;
    }

    public ListDeclMethod  getMethods(){
        return methods;
    }

    //getter for name
    public String getClassName(){
        return name.getName().getName();
    }
          
    
}
