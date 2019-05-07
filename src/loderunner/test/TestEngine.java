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
import loderunner.map.DrawMap;
import loderunner.services.Cell;
import loderunner.services.Command;
import loderunner.services.EngineService;
import loderunner.services.GuardService;
import loderunner.services.Pair;

public class TestEngine extends AbstractJeuTest{



	private EnvironmentContrat enviContrat;


	private EngineService engine ;
	@Override
	public void beforeTests() {

		setEngine(new EngineContrat(new EngineImpl()));

		EditableScreenImpl screen = new EditableScreenImpl();
		EditableScreenContrat  screenContrat = new EditableScreenContrat(screen);

		DrawMap.drawmap(screenContrat,"mapTestGuard.txt");

		//créer un environment
		EnvironmentImpl	envi = new EnvironmentImpl();
		enviContrat = new EnvironmentContrat(envi);
		enviContrat.init(screenContrat.getHeight(),screenContrat.getWidth(),screenContrat);

	}

	public void initialisation() {

		//créer un player qui est en pos (4,2)
		PlayerImpl player = new PlayerImpl();
		PlayerContrat playerContrat = new PlayerContrat(player);	
		playerContrat.init(enviContrat, 4, 2);


		//créer un guard qui est en pos (0,2)
		GuardImpl guard = new GuardImpl();
		GuardContrat guardContrat = new GuardContrat(guard);
		guardContrat.init( 0, 2, enviContrat, playerContrat);
		ArrayList<GuardService> guardsContrat = new ArrayList<GuardService>();
		guardsContrat.add(guardContrat);	

		//créer un tresor en pos(6,2)
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair(6,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,playerContrat, guardsContrat, listTresors);

	}


	/*Tests transitions*/
	@Test
	public void testRamasserTresors() {
		//créer un player qui est en pos (4,2)
		PlayerImpl player = new PlayerImpl();
		PlayerContrat playerContrat = new PlayerContrat(player);	
		playerContrat.init(enviContrat, 4, 2);


		//créer une liste de guard vide 
		ArrayList<GuardService> guardsContrat = new ArrayList<GuardService>();

		//créer des tresors en pos (3,2) (6,2) (7,2) 
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair<Integer, Integer>(3,2));
		listTresors.add(new Pair<Integer, Integer>(6,2));
		listTresors.add(new Pair<Integer, Integer>(7,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,playerContrat, guardsContrat, listTresors);
		
		engine.setCmd(Command.Left);
		engine.step();
		engine.setCmd(Command.Right);
		engine.step();
		assertEquals(engine.getPlayer().getScore(), 1);

		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.Right);
		engine.step();
		assertEquals(engine.getPlayer().getScore(), 2);
		engine.setCmd(Command.Neutral);
		engine.step();
		assertEquals(engine.getPlayer().getScore(), 3);

			


	}
	/**
	 * En initialisation, le player est en position (4,2), un seul guard est en position(0, 2)
	 * le player ne bouge pas,  le guard va aller a droite
	 */
	@Test
	public void testGuardGoRightTrans() {



		initialisation();
		engine.setCmd(Command.Neutral);
		engine.step();
		assertEquals(engine.getGuards().get(0).getWdt(), 1);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);


	}

	/**
	 * En initialisation, le player est  en position (4,2),un seul guard est en position(0, 2)
	 * player fait DigL, et apres il fait rien pendant 3 step, le guard tombe dans le trou
	 */
	@Test
	public void testTrapperGuard() {

		initialisation();
		engine.setCmd(Command.DigL);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		assertEquals(engine.getEnvironment().getCellNature(3, 1), Cell.HOL);
		assertEquals(engine.getGuards().get(0).getWdt(), 3);
		assertEquals(engine.getGuards().get(0).getHgt(), 1);

	}

	/**
	 * En initialisation, le player est  en position (4,2),un seul guard est en position(0, 2)
	 * player fait DigL, puis il va droite , apres il fait rien pendant 2 step, le guard tombe dans le trou
	 * et puis apres 5 step, le guard fait un ClimbRight
	 */
	@Test
	public void testGuardClimbRight() {

		initialisation();
		engine.setCmd(Command.DigL);
		engine.step();
		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();

		/*le guard est tomber dans trou*/
		assertEquals(engine.getEnvironment().getCellNature(3, 1), Cell.HOL);
		assertEquals(engine.getPlayer().getWdt(), 5);
		assertEquals(engine.getPlayer().getHgt(), 2);
		assertEquals(engine.getGuards().get(0).getWdt(), 3);
		assertEquals(engine.getGuards().get(0).getHgt(), 1);
		engine.setCmd(Command.Neutral);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();

		System.out.println("TimeInHole = "+ engine.getGuards().get(0).getTimeInHole());

		assertEquals(engine.getGuards().get(0).getWdt(), 4);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);
		assertEquals(engine.getEnvironment().getCellNature(3, 1), Cell.HOL);


	}

	/* Test senario : le guard revient à la position initiale
	 * au début, le player est  en position (4,2),un seul guard est en position(0, 2)
	 * player va a gauche et fait DigL, puis il va droite et fait un DigL, puis il va droite et fait un DigL(Il a fait 3 trous au total)
	 * apres il attend pendant 15 step, 
	 * le guard tombe dans le premier trou et puis apres 5 step, le guard fait un ClimbRight, et puis il tombe dans le deuxieme trou 
	 * apres 5 step, le guard fait un ClimbRight et tombe dans la 3ème trou
	 * au bout d'un moment, le 3ème trou est rebouché, et le guard revient à la position initiale
	 * */
	@Test
	public void testGuardRevientPosInit() {
		initialisation();
		engine.setCmd(Command.Left);
		engine.step();
		engine.setCmd(Command.DigL);
		engine.step();
		assertEquals(engine.getEnvironment().getCellNature(2, 1), Cell.HOL);

		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.DigL);
		engine.step();
		assertEquals(engine.getEnvironment().getCellNature(3, 1), Cell.HOL);

		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.DigL);
		engine.step();
		assertEquals(engine.getEnvironment().getCellNature(4, 1), Cell.HOL);


		for(int i=0; i<15;i++) { 
			engine.setCmd(Command.Neutral);
			engine.step();
		}
		assertEquals(engine.getEnvironment().getCellNature(2, 1), Cell.PLT);
		assertEquals(engine.getEnvironment().getCellNature(3, 1), Cell.PLT);
		assertEquals(engine.getEnvironment().getCellNature(4, 1), Cell.PLT);

		assertEquals(engine.getGuards().get(0).getWdt(), 0);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);




	}



}