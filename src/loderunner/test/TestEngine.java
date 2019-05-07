package loderunner.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import loderunner.contrat.EditableScreenContrat;
import loderunner.contrat.EngineContrat;
import loderunner.contrat.EnvironmentContrat;
import loderunner.contrat.GuardContrat;
import loderunner.contrat.PlayerContrat;
import loderunner.impl.EditableScreenImpl;
import loderunner.impl.EngineImpl;
import loderunner.impl.EnvironmentImpl;
import loderunner.impl.GuardImpl;
import loderunner.impl.PlayerImpl;
import loderunner.services.Cell;
import loderunner.services.Command;
import loderunner.services.EngineService;
import loderunner.services.GuardService;
import loderunner.services.Pair;
import loderunner.services.Status;

public class TestEngine extends AbstractJeuTest{



	private EnvironmentContrat enviContrat;

	
	private EngineService engine ;
	@Override
	public void beforeTests() {

		setEngine(new EngineContrat(new EngineImpl()));
	}
	

	public void drawMap() {
		EditableScreenImpl screen = new EditableScreenImpl();
		EditableScreenContrat  screenContrat = new EditableScreenContrat(screen);
		screenContrat.init(5, 5);
		for(int i = 0; i<5;i++)
			screenContrat.setNature(i, 0, Cell.MTL);
		
			

		screenContrat.setNature(0, 1, Cell.PLT);
		screenContrat.setNature(1, 1, Cell.PLT);
		screenContrat.setNature(2, 1, Cell.PLT);
		screenContrat.setNature(3, 1, Cell.PLT);
		screenContrat.setNature(4, 1, Cell.PLT);

		//créer un environment
		EnvironmentImpl	envi = new EnvironmentImpl();
		 enviContrat = new EnvironmentContrat(envi);
		enviContrat.init(screenContrat.getHeight(),screenContrat.getWidth(),screenContrat);
				
	}
	
	public void initialisation() {
		
		drawMap();
		//créer un player qui est en position (3,2)
		PlayerImpl player = new PlayerImpl();
		PlayerContrat playerContrat = new PlayerContrat(player);	
		playerContrat.init(enviContrat, 3, 2);
		

		//créer un guard qui est en position (0,2)
		GuardImpl guard = new GuardImpl();
		GuardContrat guardContrat = new GuardContrat(guard);
		guardContrat.init(100, 0, 2, enviContrat, playerContrat);
		ArrayList<GuardService> guardsContrat = new ArrayList<GuardService>();
		guardsContrat.add(guardContrat);	
		
		//créer un tresor qui est en pos (4,2)
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair(4,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,playerContrat, guardsContrat, listTresors);

	}
	
	/**
	 * Test le cas ou le jeu est gagne
	 * En initialisation, le player est en position (3,2), un seul tresor est en position(4, 2)
	 * le player va aller a droite et recuperer le tresor
	 */
	@Test
	public void testWin() {



		initialisation();
		assertEquals(engine.getStatus(), Status.Playing);

		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		assertEquals(engine.getStatus(), Status.Win);
	


	}
	
	/**
	 * Test le cas ou le jeu est perdu
	 * En initialisation, le player est en position (3,2), un seul guard est en position(0, 2)
	 * le player ne bouge pas
	 * le guard va rattraper le joueur et le jeu est perdu
	 */
	@Test
	public void testLossByGuard() {

		initialisation();
		assertEquals(engine.getStatus(), Status.Playing);
		engine.setCmd(Command.Neutral);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		assertEquals(engine.getStatus(), Status.Loss);
	}
	
	/*
	 * Test le cas ou le jeu est perdu
	 * le player est tomber dans un trou pendant 15 step, le trou est rebouche, le jeu est perdu
	 */
	@Test
	public void testLossByTrouRebouche() {

		drawMap();
		//créer un player qui est en position (3,2)
		PlayerImpl player = new PlayerImpl();
		PlayerContrat playerContrat = new PlayerContrat(player);	
		playerContrat.init(enviContrat, 3, 2);
		

		//créer une liste vide de guard
		ArrayList<GuardService> guardsContrat = new ArrayList<GuardService>();
		
		//créer un tresor qui est en pos (4,2)
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair(4,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,playerContrat, guardsContrat, listTresors);
		
		engine.setCmd(Command.DigL);
		engine.step();
		assertEquals(engine.getEnvironment().getCellNature(2, 1), Cell.HOL);

		engine.setCmd(Command.Left);
		engine.step();
		for(int i =0;i<14;i++) {
			engine.setCmd(Command.Neutral);
			engine.step();
		}
	
		assertEquals(engine.getStatus(), Status.Loss);
		assertEquals(engine.getEnvironment().getCellNature(2, 1), Cell.PLT);


	}
	
	
	

}