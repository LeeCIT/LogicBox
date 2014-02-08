package frameShareProto;


import frameShareProto.ToolSelectionEnum.ToolSelection;

public class GetToolBarSelectionAsString {
	
	/**
	 * 
	 * @param toolSelection The enum of the toolbar selection
	 * @return The string equivalent
	 */
	public static String getToolbarselectionAsString(ToolSelection toolSelection) {
		
		switch (toolSelection) {
		
			case AND_GATE:  
				return "And Gate";
		
			case OR_GATE: 
				return "Or Gate";
				
			case XORGATE:
				return "XOr Gate";
				
			case BULB:
				return "Bulb";
				
			case OSCILATOR:
				return "Oscilator";
		
		}
		
		return "Nothing selected";		
	}
}
