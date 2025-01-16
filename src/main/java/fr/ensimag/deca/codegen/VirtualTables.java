package fr.ensimag.deca.codegen;

import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

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
    
     private static final String EQUALS_METHOD = "equals";
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

        classDef.inheritMethodsFrom(superDef);

        Map<Integer, Map.Entry<String, Label>> sortedMethods = new TreeMap<>();
        
        for (Map.Entry<String, Integer> entry : superDef.getMethodOffsets().entrySet()) {
            String methodName = entry.getKey();
            int offset = entry.getValue();
            Label label = superDef.getMethodLabel(methodName);
            sortedMethods.put(offset, new AbstractMap.SimpleEntry<>(methodName, label));
        }
        
        // Add/override methods from this class
        for (AbstractDeclMethod method : declClass.getMethods().getList()) {
            DeclMethod declMethod = (DeclMethod) method;
            String methodName = declMethod.getName();
            Label methodLabel = new Label("code." + className + "." + methodName);
            
            // get existing offset if method is overridden, otherwise use new offset
            int offset = classDef.getMethodOffset(methodName) != null ? 
                        classDef.getMethodOffset(methodName) : 
                        sortedMethods.size() + 1;
            
            classDef.addMethod(methodName, methodLabel, offset);
            
            // Update 
            sortedMethods.put(offset, new AbstractMap.SimpleEntry<>(methodName, methodLabel));
        }
        
        for (Map.Entry<Integer, Map.Entry<String, Label>> entry : sortedMethods.entrySet()) {
            Label methodLabel = entry.getValue().getValue();
        
            compiler.addInstruction(new LOAD(new LabelOperand(methodLabel), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, compiler.getMangerM().allocateVariable()));
        }
    }
    public void codeGenVMTforObject(DecacCompiler compiler) {
         compiler.addComment("--------------------------------------------------");
         compiler.addComment("|       Construction des tables des methodes     |");
         compiler.addComment("--------------------------------------------------");
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
        objectClassDef.addMethod(EQUALS_METHOD, equalsLabel, 1);
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