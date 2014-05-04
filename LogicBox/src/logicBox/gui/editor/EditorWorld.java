


package logicBox.gui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import logicBox.gui.GUI;
import logicBox.gui.Gfx;
import logicBox.gui.editor.tools.Selection;
import logicBox.sim.Simulation;
import logicBox.sim.component.SourceOscillator;
import logicBox.util.Bbox2;
import logicBox.util.BinaryFunctor;
import logicBox.util.Geo;
import logicBox.util.Line2;
import logicBox.util.SpatialGrid;
import logicBox.util.Vec2;



/**
 * Stores and queries the "world" of the editor.
 * It houses the simulation and is the element which gets saved.
 * @author Lee Coakley
 */
public class EditorWorld implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private SpatialGrid<EditorComponent> grid;
	private List       <EditorComponent> ecoms;
	private Simulation                   sim;
	private boolean                      isPowerOn;
	
	private transient Set<SourceOscillator> oscillators;
	
	
	
	public EditorWorld() {
		grid      = new SpatialGrid<>( 2048, 2048, 128 );
		ecoms     = new ArrayList<>(); 
		sim       = new Simulation();
		isPowerOn = false;
	}
	
	
	
	/**
	 * Send the clock signal which oscillators sync from.
	 * @return Whether the sim changed state.
	 */
	public synchronized boolean sendClockSignal() {
		if (isPowerOn) {
			boolean simChanged = false;
			
			for (SourceOscillator osc: getOscillators())
				simChanged |= osc.sendClockSignal();
			
			if (simChanged)
				simUpdate();
			
			return simChanged;
		}
		
		return false;
	}
	
	
	
	private Set<SourceOscillator> getOscillators() {
		if (oscillators == null)
			oscillators = sim.getOscillators();
		
		return oscillators;
	}
	
	
	
	private void purgeOscillatorCache() {
		oscillators = null;
	}
	
	
	
	/**
	 * Check whether the sim is powered on.
	 */
	public synchronized boolean isPowerOn() {
		return isPowerOn;
	}
	
	
	
	/**
	 * Run the sim for one timestep.
	 * If unpowered, nothing happens.
	 */
	public synchronized void simUpdate() {
		try {
			if (isPowerOn) {
				sim.simulate();
				signalWorldChange();
			}
		}
		catch (Simulation.NonLevelisableCircuitException ex) {
			simPowerOff();
			GUI.showError(
				GUI.getMainFrame(),
				"Circuit Contains Loop",
				"The circuit contains a feedback loop.  This version of LogicBox does not support loops in circuits."
			);
		}
	}
	
	
	
	/**
	 * Turn the power on.
	 * If already on, nothing happens.
	 */
	public synchronized void simPowerOn() {
		if ( ! isPowerOn) {
			isPowerOn = true;
			sim.reset();
			simUpdate();
		}
	}
	
	
	
	/**
	 * Reset the sim to its initial state.
	 * This also turns the power back on.
	 */
	public synchronized void simPowerReset() {
		isPowerOn = true;
		sim.reset();
		simUpdate();
	}
	
	
	
	/**
	 * Turn the power off.
	 * If it's already off, nothing happens.
	 * Does not cause a sim update.
	 */
	public synchronized void simPowerOff() {
		if (isPowerOn) {
			isPowerOn = false;
			sim.reset();
			signalWorldChange();
		}
	}
	
	
	
	private void signalWorldChange() {
		for (EditorComponent ecom: ecoms)
			ecom.onWorldChange();
	}
	
	
	
	/**
	 * Clear everything out of the simulation.
	 */
	public synchronized void clear() {
		grid .clear();
		ecoms.clear();
		sim  .clear();
		purgeOscillatorCache();
	}
	
	
	
	/**
	 * Check whether there are any components in the simulation.
	 */
	public synchronized boolean isEmpty() {
		return ecoms.isEmpty();
	}
	
	
	
	/**
	 * Paste from the clipboard.
	 */
	public synchronized void paste( Selection sel ) {
		for (EditorComponent ecom: sel)
			addInternal( ecom );
		
		simUpdate();
	}
	
	
	
	/**
	 * Delete a selection.
	 */
	public synchronized void delete( Selection sel ) {
		for (EditorComponent ecom: sel) {
			ecom.getComponent().disconnect();
			removeInternal( ecom );
		}
		
		signalWorldChange();
		simUpdate();
	}
	
	
	
	/**
	 * Add a component to the world.
	 */
	public synchronized void add( EditorComponent...ecoms ) {
		for (EditorComponent ecom: ecoms)
			addInternal( ecom );
		
		simUpdate();
	}
	
	
	
	private void addInternal( EditorComponent ecom ) {
		addToGrid( ecom );		
		ecoms.add( ecom );
		ecom.linkToWorld( this );
		sim.add( ecom.getComponent() );
		purgeOscillatorCache();
		
		if (ecom instanceof EditorComponentOscillator)
			resyncOscillators();
	}
	
	
	
	/**
	 * Reset all oscillators to keep them in sync.
	 * This should be called whenever an oscillator's frequency is changed, or a new one is added.
	 */
	protected synchronized void resyncOscillators() {
		for (SourceOscillator osc: sim.getOscillators())
			osc.reset();
	}
	
	
	
	/**
	 * Remove a component from the world.
	 */
	public synchronized void remove( EditorComponent...ecoms ) {
		for (EditorComponent ecom: ecoms)
			removeInternal( ecom );
		
		simUpdate();
	}
	
	
	
	private void removeInternal( EditorComponent ecom ) {
		ecoms.remove( ecom );
		grid .remove( ecom );
		ecom.unlinkFromWorld();
		sim.remove( ecom.getComponent() );
		purgeOscillatorCache();
	}
	
	
	
	private void move( EditorComponent ecom ) {
		ecoms.remove( ecom );
		grid .remove( ecom );
		
		addToGrid( ecom );		
		ecoms.add( ecom );
	}
	
	
	
	private void addToGrid( EditorComponent ecom ) {
		if ( ! (ecom instanceof EditorComponentTrace))
			 grid.add( ecom.getGraphic().getBbox(), ecom );
		else addToGrid( (EditorComponentTrace) ecom );
	}
	
	
	
	private void addToGrid( EditorComponentTrace trace ) {
		for (Line2 line: trace.getGraphic().getLines())
			grid.add( line, trace );
	}
	
	
	
	/**
	 * Update the underlying world structures in response to
	 * a component orientation or position change.
	 * EditorComponents should call this method automatically.
	 */
	public synchronized void onComponentTransform( EditorComponent ecom ) {
		move( ecom );
	}
	
	
	
	/**
	 * Make all graphics un-highlighted and un-selected.
	 */
	public synchronized void clearGraphicSelectionAndHighlightStates() {
		for (EditorComponent ecom: ecoms) {
			ecom.getGraphic().setHighlighted( false );
			ecom.getGraphic().setSelected   ( false );
		}
	}
	
	
	
	/**
	 * Find one component at the given position.
	 * It is always the most recently modified or moved component.
	 * This is the component highlighted or affected by any editor tool.
	 * Returns null if no component is found.
	 */
	public synchronized EditorComponent findTopmostAt( Vec2 pos ) {
		List<EditorComponent> list = find( pos );
		
		if ( ! list.isEmpty())
			 return list.get( list.size() - 1 ); 
		else return null;
	}
	
	
	
	/**
	 * Find all components which contain the given position.
	 */
	public synchronized List<EditorComponent> find( Vec2 pos ) {
		List<EditorComponent> list = new ArrayList<>();
		
		for (EditorComponent ecom: grid.findPotentials( pos ))
			if (ecom.getGraphic().contains( pos ))
				list.add( ecom );
		
		return list;
	}
	
	
	
	/**
	 * Find all components which overlap the given bounding box.
	 */
	public synchronized List<EditorComponent> find( Bbox2 bbox ) {
		List<EditorComponent> list = new ArrayList<>(); 
		
		for (EditorComponent ecom: grid.findPotentials( bbox ))
			if (ecom.getGraphic().overlaps( bbox ))
				list.add( ecom );
		
		return list;
	}
	
	
	
	public class FindClosestPinResult {
		public boolean           foundPin;
		public EditorComponent   ecom;
		public GraphicPinMapping gpm;
	}
	
	
	
	/**
	 * Find the closest pin/ecom within the given radius.
	 */
	public synchronized FindClosestPinResult findClosestPin( Vec2 pos, double radius ) {
		FindClosestPinResult result = new FindClosestPinResult();
		Bbox2  bbox     = new Bbox2(pos,pos).expand( radius * 2 );
		double bestDist = Double.MAX_VALUE;
		
		for (EditorComponent ecom: find( bbox )) {
			GraphicPinMapping gpm = ecom.findPinNear( pos, radius );
			
			if (gpm != null) {
				double dist = gpm.line.distanceToPoint( pos );
				
				if (dist <= bestDist) {
					bestDist = dist;
					result.foundPin = true;
					result.gpm      = gpm;
					result.ecom     = ecom;
				}
			}
		}
		
		return result;
	}
	
	
	
	public class FindClosestTraceResult {
		public boolean              foundTrace;
		public EditorComponentTrace ecom;
		public Vec2                 closestPos;
		public int                  lineIndex;
	}
	
	
	
	/**
	 * Find the closest trace within the given radius.
	 */
	public synchronized FindClosestTraceResult findClosestTrace( Vec2 pos, double radius ) {
		FindClosestTraceResult result = new FindClosestTraceResult();
		Bbox2  bbox     = new Bbox2(pos,pos).expand( radius * 2 );
		double bestDist = Double.MAX_VALUE;
		
		for (EditorComponent ecom: find( bbox )) {
			if ( ! (ecom instanceof EditorComponentTrace))
				continue;
			
			EditorComponentTrace trace = (EditorComponentTrace) ecom;
			
			List<Line2> lines = trace.getGraphic().getLines();
			for (int i=0; i<lines.size(); i++) {
				Line2  line = lines.get( i );
				double dist = line.distanceToPoint( pos );
				
				if (dist <= bestDist) {
					bestDist = dist;
					result.foundTrace = true;
					result.ecom       = trace;
					result.closestPos = line.closestPoint( pos );
					result.lineIndex  = i;
				}
			}
		}
		
		return result;
	}
	
	
	
	/**
	 * Get a list of all components.
	 */
	public synchronized List<EditorComponent> getComponents() {
		return new ArrayList<>( ecoms );
	}
	
	
	
	/**
	 * Find the extent of the world occupied by components.
	 * This is the union of all component bounding boxes.
	 * If there are no components a default area is used.
	 */
	public synchronized Bbox2 getWorldExtent() {
		Bbox2 extent = getExtent( ecoms );
		
		if (extent == null)
		     return new Bbox2( 0,0, 1024,768 );
		else return extent;
	}
	
	
	
	/**
	 * Get union of component bounding boxes.
	 */
	public static Bbox2 getExtent( Collection<EditorComponent> ecoms ) {
		List<Bbox2> bboxes = new ArrayList<>();
		
		for (EditorComponent ecom: ecoms)
			bboxes.add( ecom.getGraphic().getBbox() );
		
		return Geo.reduce( bboxes, new BinaryFunctor<Bbox2>() {
			public Bbox2 call( Bbox2 a, Bbox2 b ) {
				return Bbox2.union( a, b );
			}
		});
	}
	
	
	
	/**
	 * Returns only the components whose bounding boxes lie within (or close to) the view boundary. 
	 */
	public synchronized List<EditorComponent> getViewableComponents( Camera cam, double tolerance ) {
		Bbox2 bbox = cam.getWorldViewArea().expand( new Vec2(tolerance) );
		
		List<EditorComponent> list = new ArrayList<>();
		
		for (EditorComponent ecom: grid.findPotentials( cam.getWorldViewArea() ))
			if (ecom.getGraphic().getBbox().overlaps( bbox ))
				list.add( ecom );
		
		return list;
	}
	
	
	
	public synchronized List<Graphic> getViewableComponentGraphics( Camera cam, double tolerance ) {
		List<Graphic> list = new ArrayList<>();
		
		for (EditorComponent ecom: getViewableComponents( cam, tolerance ))
			list.add( ecom.getGraphic() );
		
		putTracesUnderneathActives( list );
		
		return list;
	}
	
	
	
	/**
	 * Sort so traces are underneath actives.
	 * @param list
	 */
	private void putTracesUnderneathActives( List<Graphic> list ) {
		Collections.sort( list, new Comparator<Graphic>() { 
			public int compare( Graphic a, Graphic b ) {
				boolean aTrace = a instanceof GraphicTrace;
				boolean bTrace = b instanceof GraphicTrace;
				
				if (aTrace != bTrace)
					 return boolToInt(bTrace) - boolToInt(aTrace);
				else return 0; 
			}
			
			
			public int boolToInt( boolean b ) {
				return (b) ? 1 : 0;
			}
		});
	}
	
	
	
	public synchronized String toString() {
		String str = "EditorWorld with " + ecoms.size() + " components:\n";
		
		for (EditorComponent ecom: ecoms)
			str += "\t" + ecom + " \t[" + ecom.getComponent() + "]\n"; 
		
		return str;
	}
	
	
	
	public synchronized RepaintListener getSpatialGridDebugRepainter() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				int[][] array = grid.debugGridLevels();
				double  size  = grid.getCellSize();
				
				for (int y=0; y<array   .length; y++)			
				for (int x=0; x<array[0].length; x++) {
					double count = array[y][x];
					
					if (count <= 0)
						continue;
					
					double colF  = Geo.boxStep( count, 0, 6 );
					Color  col   = Geo.lerp( Color.green, Color.red, colF );
					Vec2   tl    = new Vec2(x,y).multiply( size );
					Bbox2  bbox  = new Bbox2( tl, tl.add(size) ); 
					
					Gfx.pushAntialiasingStateAndSet( g, false );
						Gfx.pushColorAndSet( g, col );
							Gfx.drawBbox( g, bbox, false );
							Gfx.drawOrientedRect( g, bbox.getCentre(), new Vec2(size*0.1), 0, false );
						Gfx.popColor( g );
					Gfx.popAntialiasingState( g );
				}
				
				Gfx.pushAntialiasingStateAndSet( g, false );
					Gfx.pushColorAndSet( g, Color.BLUE );
						Gfx.drawBbox( g, new Bbox2(0,0,grid.getCellsPerRow()*size,grid.getCellsPerColumn()*size), false );
					Gfx.popColor( g );
				Gfx.popAntialiasingState( g );
			}
		};
	}
}































