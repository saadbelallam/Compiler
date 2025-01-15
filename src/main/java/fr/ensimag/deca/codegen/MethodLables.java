package fr.ensimag.deca.codegen;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.ima.pseudocode.Label;







public class MethodLables{

    private final Map<String, Label> methodLabels = new HashMap<>();   


    public Label getMethodLabel(String methodName) {
        return methodLabels.get(methodName);
    }

    public Map<String,Label> getMethodLabels() {
        return methodLabels;
    }
    
    public void setMethodLabel(String methodName, Label label) {
        methodLabels.put(methodName, label);
    }


}