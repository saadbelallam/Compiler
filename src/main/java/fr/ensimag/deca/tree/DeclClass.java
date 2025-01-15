package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.io.PrintStream;

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
        System.out.println("superclass" + superClass.getName());
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
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
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

}
