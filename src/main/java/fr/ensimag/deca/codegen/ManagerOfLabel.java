package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;



public class ManagerOfLabel {
     
    private int currentLabelNumber = 0;
    private final Label overflowLabel = new Label("overflow_error");
    private final Label io_Label = new Label("io_error");
    
    
    /**
     * Creates a new  label for BOV
     * @return OverFlow Label
     */
    public Label getOverflowLabel() {
        return overflowLabel;
    }

    public Label getIOLabel(){
        return io_Label;
    }
 
    /**
     * Creates a new unique label number
     * @return The next available label number
     */
    public int newLabel() {
        return currentLabelNumber++;
    }
    
    /**
     * Resets the label counter
     */
    public void reset() {
        currentLabelNumber = 0;
    }
}