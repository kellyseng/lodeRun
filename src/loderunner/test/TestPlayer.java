package loderunner.test;
import loderunner.contrat.EditableScreenContrat;
import loderunner.contrat.EngineContrat;
import loderunner.contrat.EnvironmentContrat;
import loderunner.contrat.GuardContrat;
import loderunner.contrat.PlayerContrat;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

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
import loderunner.services.Triplet;

public class TestPlayer extends AbstractJeuTest{



	private EnvironmentContrat enviContrat;


	private EngineService engine ;
	@Override
	public void beforeTests() {

		setEngine(new EngineContrat(new EngineImpl()));

		EditableScreenImpl screen = new EditableScreenImpl();
		EditableScreenContrat  screenContrat = new EditableScreenContrat(screen);

		DrawMap.drawmap(screenContrat,"mapTestPlayer.txt");

		//cr�er un environment
		EnvironmentImpl	envi = new EnvironmentImpl();
		enviContrat = new EnvironmentContrat(envi);
		enviContrat.init(screenContrat.getHeight(),screenContrat.getWidth(),screenContrat);

	}
	@Override
	public void afterTests() {
		enviContrat= null;
		engine = null;
	}

	public void initialisation() {


		//cr�er un player qui est en pos (4,2)
		Pair<Integer, Integer> player = new Pair<Integer, Integer>(4,2);



		//cr�er un guard qui est en pos (0,2)
		List<Triplet<Integer,Integer,Boolean>> listGuards = new ArrayList<Triplet<Integer,Integer,Boolean>> ();
		listGuards.add(new Triplet<Integer,Integer,Boolean>(0,2,false));

		//cr�er un tresor en pos(6,2)
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair<Integer, Integer>(6,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,player, listGuards, listTresors);

		engine.setEnTestMode();

	}
	@Test
	public void testInitPrePositif() {


		//cr�er un player qui est en pos (4,2)
		Pair<Integer, Integer> player = new Pair<Integer, Integer>(4,2);



		//cr�er un guard qui est en pos (0,2)
		List<Triplet<Integer,Integer,Boolean>> listGuards = new ArrayList<Triplet<Integer,Integer,Boolean>> ();
		listGuards.add(new Triplet<Integer,Integer,Boolean>(0,2,false));

		//cr�er un tresor en pos(6,2)
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair<Integer, Integer>(6,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,player, listGuards, listTresors);

	}

	/**
	 *  pre init(S,x,y) requires Environment : :CellNature(S,x,y) = EMP
	 *  test quand CellNature(S,x,y) = PLT
	 */
	@Test
	public void testInitPreNegatif() {

		//cr�er un player qui est en pos (4,1)
		Pair<Integer, Integer> player = new Pair<Integer, Integer>(4,1);

		//cr�er un guard qui est en pos (0,2)
		List<Triplet<Integer,Integer,Boolean>> listGuards = new ArrayList<Triplet<Integer,Integer,Boolean>> ();
		listGuards.add(new Triplet<Integer,Integer,Boolean>(0,2,false));

		//cr�er un tresor en pos(6,2)
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair<Integer, Integer>(6,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,player, listGuards, listTresors);
	}

	
	@Test
	public void testPlayerGoRightPLT() {


		initialisation(); // player en position (4,2), un seul guard est en position(0, 2)
		engine.setCmd(Command.Right);
		engine.step();
		assertEquals(engine.getPlayer().getWdt(),5);
		assertEquals(engine.getPlayer().getHgt(),2);
		assertEquals(engine.getGuards().get(0).getWdt(), 1);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);


	}

	/**
	 * On test le player goRight quand il est sur un handrail
	 */
	@Test
	public void testPlayerGoRightHDR() {


		initialisation(); // player en position (4,2), un seul guard est en position(0, 2)
		engine.setCmd(Command.Left);
		engine.step();
		engine.setCmd(Command.Up);
		engine.step();
		engine.setCmd(Command.Up);
		engine.step();
		engine.setCmd(Command.Up);
		engine.step();
		engine.setCmd(Command.Up);
		engine.step();
		engine.setCmd(Command.Right);
		engine.step();

		engine.setCmd(Command.Right);
		engine.step();

		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.Right);
		engine.step();

		assertEquals(engine.getPlayer().getWdt(),7);
		assertEquals(engine.getPlayer().getHgt(),6);


	}


	@Test
	public void testPlayerGoLeft() {


		initialisation();// player en position (4,2), un seul guard est en position(0, 2)
		engine.setCmd(Command.Left);
		engine.step();
		assertEquals(engine.getPlayer().getWdt(),3);
		assertEquals(engine.getPlayer().getHgt(),2);
		assertEquals(engine.getGuards().get(0).getWdt(), 1);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);


	}


	@Test
	public void testGoUp() {

		// En initiale,le player en position (4,2), un seul guard est en position(0, 2)
		initialisation();


		engine.setCmd(Command.Left);
		engine.step();
		engine.setCmd(Command.Up);
		engine.step();
		assertEquals(engine.getPlayer().getHgt(),3);
		assertEquals(engine.getPlayer().getWdt(),3);
		assertEquals(engine.getGuards().get(0).getWdt(), 2);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);


	}
	@Test
	public void testPlayerFallDeHaut() {

		// En initiale,le player en position (4,2), un seul guard est en position(0, 2)
		initialisation();

		engine.setCmd(Command.Left);
		engine.step();
		engine.setCmd(Command.Up);
		engine.step();
		engine.setCmd(Command.Up);
		engine.step();
		engine.setCmd(Command.Up);
		engine.step();
		engine.setCmd(Command.Up);
		engine.step();
		engine.setCmd(Command.Left);
		engine.step();
		engine.setCmd(Command.Left);
		engine.step();

		engine.setCmd(Command.Neutral);
		engine.step();
		assertEquals(engine.getPlayer().getHgt(),5);
		assertEquals(engine.getPlayer().getWdt(),1);

	}
	
	@Test
	public void testPlayerFallDansHole(){
		// En initiale,le player en position (4,2), un seul guard est en position(0, 2)
		initialisation();
		engine.setCmd(Command.DigR);
		engine.step();
		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		assertEquals(engine.getPlayer().getWdt(),5);
		assertEquals(engine.getPlayer().getHgt(),1);
	
	}

	@Test
	public void testPlayerGoDown() {

		// En initiale,le player en position (4,2), un seul guard est en position(0, 2)
		initialisation();
		engine.setCmd(Command.Left);
		engine.step();
		engine.setCmd(Command.Up);
		engine.step();
		assertEquals(engine.getPlayer().getHgt(),3);
		assertEquals(engine.getPlayer().getWdt(),3);

		engine.setCmd(Command.Down);
		engine.step();
		//player est attaqué par un guard

		assertEquals(engine.getPlayer().getHgt(),2);
		assertEquals(engine.getPlayer().getWdt(),4);


	}


	/**
	 * En initialisation, le player est  en position (4,2),un seul guard est en position(0, 2)
	 * le player va aller a gauche et fait DigL, donc le guard tombe dans le trou
	 */
	@Test
	public void testPlayerDigL() {

		initialisation();
		engine.setCmd(Command.Left);
		engine.step();
		engine.setCmd(Command.DigL);
		engine.step();
		assertEquals(engine.getEnvironment().getCellNature(2, 1), Cell.HOL);


		engine.setCmd(Command.Neutral);
		engine.step();

		assertEquals(engine.getGuards().get(0).getWdt(), 2);
		assertEquals(engine.getGuards().get(0).getHgt(), 1);

	}
	@Test
	public void testPlayerDigR() {

		initialisation();// player en position (4,2),un seul guard est en position(0, 2)
		engine.setCmd(Command.DigR);
		engine.step();
		assertEquals(engine.getEnvironment().getCellNature(5, 1), Cell.HOL);
		assertEquals(engine.getGuards().get(0).getWdt(), 1);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);

	}



}