package fr.ensimag.deca.codegen;



public class ManagerOfLabel {
     
    private int currentLabelNumber = 0;
    
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