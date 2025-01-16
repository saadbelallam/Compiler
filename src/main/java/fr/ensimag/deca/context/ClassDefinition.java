package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Definition of a class.
 *
 * @author gl24
 * @date 01/01/2025
 */
public class ClassDefinition extends TypeDefinition {
    private DAddr VMTableAddr;
    // i declared this method to prevent losing overridden methods
    private final Map<String, Integer> methodOffsets = new HashMap<>();
    // methods are suspected to be overridden
    private final Map<String, Label> methodLabels = new HashMap<>();
 
   
    public void addMethod(String methodName, Label label, int offset) {
        methodLabels.put(methodName, label);
        methodOffsets.put(methodName, offset);
    }

    public Label getMethodLabel(String methodName) {
        return methodLabels.get(methodName);
    }

    public Integer getMethodOffset(String methodName) {
        return methodOffsets.get(methodName);
    }

    public Map<String, Label> getMethodLabels() {
        return new HashMap<>(methodLabels);
    }

    public Map<String, Integer> getMethodOffsets() {
        return new HashMap<>(methodOffsets);
    }

    // Method to inherit methods from superclass
    public void inheritMethodsFrom(ClassDefinition superclass) {
        if (superclass != null) {
            methodLabels.putAll(superclass.getMethodLabels());
            methodOffsets.putAll(superclass.getMethodOffsets());
        }
    }
     
    public DAddr getVMTablesAddr(){
        return VMTableAddr;

    }

    public void setVMTableAddr(DAddr adress){
        this.VMTableAddr = adress;
    }


    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }
    
    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    
    @Override
    public boolean isClass() {
        return true;
    }
    
    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass; 

    public EnvironmentExp getMembers() {
        return members;
    }

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
    }
    
}
