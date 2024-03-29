package loderunner.test;

import static org.junit.Assert.assertEquals;


import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import loderunner.contrat.EditableScreenContrat;
import loderunner.contrat.EngineContrat;
import loderunner.contrat.EnvironmentContrat;
import loderunner.impl.EditableScreenImpl;
import loderunner.impl.EngineImpl;
import loderunner.impl.EnvironmentImpl;
import loderunner.map.DrawMap;
import loderunner.services.Cell;
import loderunner.services.Command;
import loderunner.services.EngineService;
import loderunner.services.Pair;
import loderunner.services.Status;
import loderunner.services.Triplet;

public class TestEngine extends AbstractJeuTest{



	private EnvironmentContrat enviContrat;
	private EngineService engine ;
	private int playerXInit ;
	private int playerYInit ;


	@Override
	public void beforeTests() {

		setEngine(new EngineContrat(new EngineImpl()));

		EditableScreenImpl screen = new EditableScreenImpl();
		EditableScreenContrat  screenContrat = new EditableScreenContrat(screen);

		DrawMap.drawmap(screenContrat,"mapTestEngine.txt");

		//créer un environment
		EnvironmentImpl	envi = new EnvironmentImpl();
		enviContrat = new EnvironmentContrat(envi);
		enviContrat.init(screenContrat.getHeight(),screenContrat.getWidth(),screenContrat);

	}
	@Override
	public void afterTests() {
		enviContrat = null;
		engine  = null;

	}


	public void initialisation() {

		//créer un player qui est en pos (4,2)
		playerXInit = 4;
		playerYInit = 2;		
		Pair<Integer, Integer> player = new Pair<Integer, Integer>(playerXInit,playerYInit);



		//créer un guard qui est en pos (0,2)
		List<Triplet<Integer,Integer,Boolean>> listGuards = new ArrayList<Triplet<Integer,Integer,Boolean>> ();
		listGuards.add(new Triplet<Integer,Integer,Boolean>(0,2,false));

		//créer un tresor en pos(6,2)
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair<Integer, Integer>(6,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,player, listGuards, listTresors);
		engine.setEnTestMode();


	}


	/*Tests preconditions*/
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
		
		assertEquals(engine.getPlayer().getWdt(), 4);
		assertEquals(engine.getPlayer().getHgt(), 2);
		assertEquals(engine.getGuards().get(0).getWdt(), 0);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);
		assertEquals(engine.getPlayer().getScore(), 0);
		

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

	/*Tests transitions*/
	@Test
	public void testExtensionContrat() {
		initialisation();//player est en pos (4,2), un guard en pos (0,2)
		engine.setCmd(Command.DigL);
		engine.step();
		for(int i = 0; i<3;i++) {
			engine.setCmd(Command.Neutral);
			engine.step();
		}

		//guard tombe dans le trou 
		assertEquals(engine.getGuards().get(0).getWdt(), 3);
		assertEquals(engine.getGuards().get(0).getHgt(), 1);

		engine.setCmd(Command.Left);
		engine.step();

		for(int i = 0; i<4;i++) {
			engine.setCmd(Command.Neutral);
			engine.step();
		}
		assertEquals(engine.getGuards().get(0).getTimeInHole(), 5);
		assertEquals(engine.getEnvironment().getCellNature(3, 1), Cell.HOL);

		assertEquals(engine.getPlayer().getVie(), 2);
		// l'eran est reinitialise
		assertEquals(engine.getPlayer().getWdt(), playerXInit);
		assertEquals(engine.getPlayer().getHgt(), playerYInit);
		assertEquals(engine.getGuards().get(0).getWdt(), 0);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);
		assertEquals(engine.getPlayer().getScore(), 0);

	}

	@Test
	public void testExtensionGuardSpecial() {

		//créer un player qui est en pos (4,2)
		Pair<Integer, Integer> player = new Pair<Integer, Integer>(4,2);

		//créer un guard special qui peut passer au dessus des trous  en pos (1,2)
		List<Triplet<Integer,Integer,Boolean>> listGuards = new ArrayList<Triplet<Integer,Integer,Boolean>> ();
		listGuards.add(new Triplet<Integer,Integer,Boolean>(1,2,true));

		//créer des tresors en pos (6,2) 
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair<Integer, Integer>(6,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,player, listGuards, listTresors);
		engine.setEnTestMode();

		engine.setCmd(Command.DigL);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		assertEquals(engine.getGuards().get(0).getWdt(), 3);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);
		assertEquals(engine.getPlayer().getWdt(), 4);
		assertEquals(engine.getPlayer().getHgt(), 2);
		engine.setCmd(Command.Neutral);
		engine.step();
		//le guard et player sont revenu a la position initiale
		assertEquals(engine.getGuards().get(0).getWdt(), 1);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);
		assertEquals(engine.getEnvironment().getCellNature(3, 1), Cell.HOL);
		assertEquals(engine.getPlayer().getVie(), 2);
	}

	@Test
	public void testExtensionScoreVie() {

		//créer un player qui est en pos (4,2)
		Pair<Integer, Integer> player = new Pair<Integer, Integer>(4,2);

		//créer un guard qui est en pos (0,2)
		List<Triplet<Integer,Integer,Boolean>> listGuards = new ArrayList<Triplet<Integer,Integer,Boolean>> ();
		listGuards.add(new Triplet<Integer,Integer,Boolean>(0,2,false));

		//créer des tresors en pos (3,2) (6,2) (7,2) 
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair<Integer, Integer>(3,2));
		listTresors.add(new Pair<Integer, Integer>(6,2));
		listTresors.add(new Pair<Integer, Integer>(7,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,player, listGuards, listTresors);
		engine.setEnTestMode();

		engine.setCmd(Command.Left);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		assertEquals(engine.getPlayer().getScore(), 1);
		engine.setCmd(Command.Neutral);
		engine.step();

		//	player est attrappe par un guard, il doit revenir au position initiale
		assertEquals(engine.getPlayer().getScore(), 0);
		assertEquals(engine.getPlayer().getWdt(), 4);
		assertEquals(engine.getPlayer().getHgt(), 2);

		assertEquals(engine.getGuards().get(0).getWdt(), 0);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);
		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.Neutral);
		engine.step();
		assertEquals(engine.getPlayer().getVie(), 2);

		assertEquals(engine.getPlayer().getScore(), 2);
		assertEquals(engine.getStatus(), Status.Win);





	}

	@Test
	public void testRamasserTresors() {
		//créer un player qui est en pos (4,2)
		Pair<Integer, Integer> player = new Pair<Integer, Integer>(4,2);

		//créer une liste de guard vide 
		List<Triplet<Integer,Integer,Boolean>> listGuards = new ArrayList<Triplet<Integer,Integer,Boolean>> ();

		//créer des tresors en pos (3,2) (6,2) (7,2) 
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair<Integer, Integer>(3,2));
		listTresors.add(new Pair<Integer, Integer>(6,2));
		listTresors.add(new Pair<Integer, Integer>(7,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,player, listGuards, listTresors);
		engine.setEnTestMode();

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
		assertEquals(engine.getEnvironment().getCellContent(3, 2).size(), 0);
		assertEquals(engine.getEnvironment().getCellContent(6, 2).size(), 0);
		assertEquals(engine.getEnvironment().getCellContent(7, 2).size(), 1);

		assertEquals(engine.getPlayer().getScore(), 3);
		assertEquals(engine.getStatus(), Status.Win);

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

	/********************************Tests Etats remarquables **********************************/
	/**
	 *  Etat remarquable : le jeu est gagne
	 * En initialisation, le player est en position (4,2), un seul tresor est en position(6, 2)
	 * le player va aller a droite et recuperer le tresor
	 */
	@Test
	public void testWin() {



		initialisation();
		assertEquals(engine.getStatus(), Status.Playing);

		engine.setCmd(Command.Right);
		engine.step();
		engine.setCmd(Command.Right);
		engine.step();

		assertEquals(engine.getEnvironment().getCellContent(6, 2).size(),2);

		engine.setCmd(Command.Neutral);
		engine.step();
		assertEquals(engine.getPlayer().getScore(),1);
		assertEquals(engine.getStatus(), Status.Win);


		assertEquals(engine.getEnvironment().getCellContent(6, 2).size(),1);


	}

	/**
	 * Etat remarquable : le jeu est perdu car le joueur est attaqué par un guard
	 * En initialisation, le player est en position (2,2), un seul guard est en position(0, 2)
	 * le player ne bouge pas
	 * le guard va rattraper le joueur et le joueur perd une vie
	 * et ca se passe 3 fois, le jeu est perdu
	 */
	@Test
	public void testLossByGuard() {

		//créer un player qui est en pos (2,2)
		Pair<Integer, Integer> player = new Pair<Integer, Integer>(2,2);

		//créer une liste de guard vide 
		List<Triplet<Integer,Integer,Boolean>> listGuards = new ArrayList<Triplet<Integer,Integer,Boolean>> ();
		listGuards.add(new Triplet<Integer,Integer,Boolean>(0,2,false));
		//créer des tresors en pos (6,2)  
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair<Integer, Integer>(6,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,player, listGuards, listTresors);
		engine.setEnTestMode();
		
		assertEquals(engine.getStatus(), Status.Playing);

		for(int i = 0;i<2;i++) {
			engine.setCmd(Command.Neutral);
			engine.step();
		}


		/* le guard doit revenir au pos initial et le player perd une vie */
		assertEquals(engine.getGuards().get(0).getWdt(), 0);
		assertEquals(engine.getGuards().get(0).getHgt(), 2);
		assertEquals(engine.getPlayer().getWdt(), 2);
		assertEquals(engine.getPlayer().getHgt(), 2);
		assertEquals(engine.getPlayer().getVie(), 2);

		for(int i = 0;i<2;i++) {
			engine.setCmd(Command.Neutral);
			engine.step();
		}

		assertEquals(engine.getPlayer().getVie(), 1);

		for(int i = 0;i<2;i++) {
			engine.setCmd(Command.Neutral);
			engine.step();
		}
		assertEquals(engine.getPlayer().getVie(), 0);
		assertEquals(engine.getStatus(), Status.Loss);
	}

	/*
	 * Etat remarquable : le joueur perd une vie car il est dans un trou qui rebouche
	 * le player est tombe dans un trou pendant 15 step, le trou est rebouche
	 * le joueur perd une vie et revient a sa position initiale
	 */
	@Test
	public void testPlayerPerdVieByTrouRebouche() {

		//créer un player qui est en position (3,2)
		Pair<Integer, Integer> player = new Pair<Integer, Integer>(3,2);

		//créer une liste vide de guard
		List<Triplet<Integer,Integer,Boolean>> listGuards = new ArrayList<Triplet<Integer,Integer,Boolean>> ();

		//créer un tresor qui est en pos (4,2)
		List<Pair<Integer, Integer>> listTresors = new ArrayList<Pair<Integer, Integer>> ();
		listTresors.add(new Pair<Integer, Integer>(4,2));

		//Initialiser engine
		engine = getEngine();
		engine.init(enviContrat,player, listGuards, listTresors);
		engine.setEnTestMode();

		engine.setCmd(Command.DigL);
		engine.step();
		assertEquals(engine.getEnvironment().getCellNature(2, 1), Cell.HOL);

		engine.setCmd(Command.Left);
		engine.step();
		for(int i =0;i<14;i++) {
			engine.setCmd(Command.Neutral);
			engine.step();
		}
		assertEquals(engine.getPlayer().getVie(), 2);
		assertEquals(engine.getPlayer().getScore(), 0);

		assertEquals(engine.getEnvironment().getCellNature(2, 1), Cell.PLT);


	}





}
