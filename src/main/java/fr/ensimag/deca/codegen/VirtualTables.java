package fr.ensimag.deca.codegen;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.tree.AbstractDeclClass;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.deca.tree.DeclClass;
import fr.ensimag.deca.tree.DeclMethod;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Handles generation of virtual method tables
 */
public class VirtualTables {
    
    
    /**
     * Generates virtual method table for a class
     */
    public void codeGenVMT(DecacCompiler compiler, AbstractDeclClass clss) {
        DeclClass declClass = (DeclClass) clss;
        ClassDefinition classDef = declClass.getClassDefinition();
        
        String className = classDef.getType().getName().getName();
        compiler.addComment("Code for the method table of " + className);

        ClassDefinition superDef = classDef.getSuperClass();
        compiler.addInstruction(new LEA(superDef.getVMTablesAddr(), Register.R0));
        
        RegisterOffset tableAddr = compiler.getMangerM().allocateVariable();
        compiler.addInstruction(new STORE(Register.R0, tableAddr));

        classDef.setVMTableAddr(tableAddr);

        Map<String, Label> methodTable = new HashMap<>();

        if (declClass.getSuperClass() != null) {
            System.err.println("hey");
            for (Map.Entry<String, Label> entry : superDef.getMethodLabels().entrySet()) {
                System.err.println("key = " + entry.getKey());
                System.err.println("Value = " + entry.getValue());
            }   

            methodTable.putAll(superDef.getMethodLabels());
        }
        else {
            System.err.println("i'm null " + declClass.getSuperClassName());
        }

        for (AbstractDeclMethod method : declClass.getMethods().getList()) {
            DeclMethod declMethod = (DeclMethod) method;

            String methodName = declMethod.getName();
            Label methodLabel = new Label("code." + className + "." + methodName);
            methodTable.put(methodName, methodLabel);
            classDef.setMethodLabel(methodName, methodLabel);}


        // Step 4: Generate VMT entries in correct order
        int offset = 1; // Start after parent pointe

        for (Map.Entry<String, Label> entry : methodTable.entrySet()) {
            // Load method address
            compiler.addInstruction(new LOAD(new LabelOperand(entry.getValue()), Register.R0));
            
            // Store in VMT at correct offset
            RegisterOffset methodOffset = compiler.getMangerM().allocateVariable();
            compiler.addInstruction(new STORE(Register.R0, methodOffset));
            
            // Store method offset in class definition for method calls
            classDef.setMethodOffset(entry.getKey(), offset++);
        }

}

     public void codeGenVMTforObject(DecacCompiler compiler) {
         compiler.addComment("Code for Object method table");
    
        Definition objectDef = compiler.getEnvironmentType().get(
            compiler.createSymbol("Object")
        );
        ClassDefinition objectClassDef = (ClassDefinition) objectDef;
        
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        
        RegisterOffset objectVMT = compiler.getMangerM().allocateVariable();
        compiler.addInstruction(new STORE(Register.R0, objectVMT));
        
        Label equalsLabel = new Label("code.Object.equals");
        compiler.addInstruction(new LOAD(new LabelOperand(equalsLabel), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, 
        compiler.getMangerM().allocateVariable()));
        
        objectClassDef.setVMTableAddr(objectVMT);
        objectClassDef.setMethodLabel("equals", equalsLabel);
    }


    
// Step 3: Generate method table entries
        /* Map<String, Label> methodLabels = new HashMap<>();
        
        // First, copy superclass methods
        for (AbstractDeclMethod superMethod : declClass.getSuperClass() {
            String methodName = superMethod.getName().getName();
            Label methodLabel = superMethod.getMethodLabel();
            methodLabels.put(methodName, methodLabel);
        }
        
        // Then, override with class's own methods
        for (AbstractDeclMethod method : declClass.getMethods().getList()) {
            String methodName = method.getName().getName();
            Label methodLabel = new Label("code." + declClass.getName() + "." + methodName);
            methodLabels.put(methodName, methodLabel);
            
            // Generate method code
            compiler.addInstruction(new LOAD(new LabelOperand(methodLabel), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, 
                compiler.getMemoryManager().allocateVariable()));
        } */


    
}