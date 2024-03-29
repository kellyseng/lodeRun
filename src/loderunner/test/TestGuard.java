package loderunner.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loderunner.contrat.GuardContrat;
import loderunner.contrat.PreconditionError;
import loderunner.impl.CharacterImpl;
import loderunner.impl.EditableScreenImpl;
import loderunner.impl.EnvironmentImpl;
import loderunner.impl.GuardImpl;
import loderunner.services.Cell;
import loderunner.services.CharacterService;
import loderunner.services.EditableScreenService;
import loderunner.services.EnvironmentService;
import loderunner.services.GuardService;

public class TestGuard {
	
	GuardService g1;
	GuardService g2;
	EnvironmentService env;
	CharacterService target;
	EditableScreenService es;
	private int h = 5;
	private int w = 5;
	
	
	@Before
	public void beforeTests() {
		g1 = new GuardContrat(new GuardImpl());
		g2 = new GuardContrat(new GuardImpl());
		env = new EnvironmentImpl();
		es = new EditableScreenImpl();
		target = new CharacterImpl();
		es.init(h, w);
		env.init(es.getWidth(), es.getHeight(),es);
		target.init(env, w-1, 1);
	}
	
	@After
	public void afterTests() {
		g1 = null;
		g2 = null;
		env = null;
		es = null;
		target = null;
	}
	

	

	@Test
	public void test1InitPrePositif() {
		try {
			g1.init(0, 1, env, target,false);
			assertTrue(true);
		}catch(PreconditionError e) {
			fail("error init");
			assertTrue(false);
		}
	}
	
	

	@Test
	public void test1InitPreNegatif() {
		try {
			es.setNature(0, 1, Cell.PLT);
			env.init(es.getWidth(), es.getHeight(),es);
			target.init(env, w-1, 1);
			g1.init(0, 1, env, target,false);
			assertTrue(false);
		}catch(PreconditionError e) {
			fail("error init");
			assertTrue(true);
		}
	}
	
	@Test
	public void test2InitPreNegatif() {
		try {
			es.setNature(0, 1, Cell.PLT);
			env.init(es.getWidth(), es.getHeight(),es);
			target.init(env, w-1, 1);
			g1.init(0, 1, env, null,false);
			assertTrue(false);
		}catch(PreconditionError e) {
			fail("error init");
			assertTrue(true);
		}
	}
	
	@Test
	public void test1InitPositif() {
		try {
			g1.init(0, 1, env, target,false);
			g2.init(2, 1, env, target,false);
			assertEquals(g1.getId(), 0);
			assertEquals(g1.getWdt(), 0);
			assertEquals(g1.getHgt(), 1);
			assertEquals(g2.getId(), 1);
			assertEquals(g2.getWdt(), 2);
			assertEquals(g2.getHgt(), 1);
		}catch(PreconditionError e) {
			fail("error init");
			assertTrue(false);
		}
	}
	
	@Test
	public void test1ClimbLeftPrePositif() {
		try {
			for (int i = 0; i < h; i++) {
				es.setNature(i, 1, Cell.PLT);
			}
			
			env.init(h, w, es);
			env.dig(1, 1);
		
			g1.init(2, 2, env, target,false);
			g1.goLeft();
			g1.goDown();
			g1.climbLeft();
		}
		catch(PreconditionError e) {
			fail("error climbLeft");
			assertTrue(false);
		}
		
	}
	


	@Test
	public void test1ClimbLeftPreNegatif() {
		try {
			g1.init(1, 1, env, target,false);
			g1.climbLeft();
			assertTrue(false);
		} catch(PreconditionError e) {
			fail("error climbLeft");
			assertTrue(true);
		}
	}
	
	
	@Test
	public void test1ClimbLeftPositif() {
		
		for (int i = 0; i < h; i++) {
			es.setNature(i, 1, Cell.PLT);
		}
		
		env.init(h, w, es);
		env.dig(1, 1);
	
		g1.init(2, 2, env, target,false);
		g1.goLeft();
		g1.goDown();
		g1.climbLeft();
		
		assertEquals(g1.getWdt(), 0);
		assertEquals(g1.getHgt(), 2);
	}
	
	@Test
	public void test2ClimbLeftPositif() {
		
			for (int i = 0; i < h; i++) {
				es.setNature(i, 1, Cell.PLT);
			}
			es.setNature(0, 2, Cell.PLT);
			env.init(h, w, es);
			env.dig(1, 1);
			g1.init(2, 2, env, target,false);
			g1.goLeft();
			g1.goDown();
			g1.climbLeft();
			assertEquals(g1.getHgt(),1);
			assertEquals(g1.getWdt(),1);
	
	}
	
	//climbRight
	
	@Test
	public void test1ClimbRighttPrePositif() {
		try {
		for (int i = 0; i < h; i++) {
			es.setNature(i, 1, Cell.PLT);
		}
		
		env.init(h, w, es);
		env.dig(1, 1);
	
		g1.init(0, 2, env, target,false);
		g1.goRight();
		g1.goDown();
		g1.climbRight();
		}catch(PreconditionError e) {
			fail("error climbLeft");
			assertTrue(false);
		}
	}
	

	@Test
	public void test1ClimbRightPreNegatif() {
		try {
			g1.init(0, 1, env, target,false);
			g1.climbRight();
			assertTrue(false);
		} catch(PreconditionError e) {
			fail("error climbLeft");
			assertTrue(true);
		}
	}
	
	@Test
	public void test1ClimbRight() {
		for (int i = 0; i < h; i++) {
			es.setNature(i, 1, Cell.PLT);
		}
		
		env.init(h, w, es);
		env.dig(1, 1);
	
		g1.init(0, 2, env, target,false);
		g1.goRight();
		g1.goDown();
		g1.climbRight();
		
		assertEquals(g1.getWdt(), 2);
		assertEquals(g1.getHgt(), 2);
	}
	
	@Test
	public void test2ClimbRight() {
		
		for (int i = 0; i < h; i++) {
			es.setNature(i, 1, Cell.PLT);
		}
		es.setNature(2, 2, Cell.MTL);
		env.init(h, w, es);
		env.dig(1, 1);
		g1.init(0, 2, env, target,false);
		g1.goRight();
		g1.goDown();
		g1.climbRight();
		assertEquals(g1.getWdt(), 1);
		assertEquals(g1.getHgt(), 1);
	}
	
	@Test
	public void test3ClimbRight() {
		
		for (int i = 0; i < h; i++) {
			es.setNature(i, 1, Cell.PLT);
		}
		es.setNature(2, 2, Cell.MTL);
		env.init(h, w, es);
		env.dig(1, 1);
		target.init(env, 2, 2);
		g1.init(0, 2, env, target,false);
		g1.goRight();
		g1.goDown();
		g1.climbRight();
		assertEquals(g1.getWdt(), 1);
		assertEquals(g1.getHgt(), 1);
	}
	
	//step
	
	@Test	
	public void test1Step() {
		g1.init(0, 1, env, target,false); 
		g1.step();
		assertEquals(g1.getWdt(), 1);
		g1.step();
		assertEquals(g1.getWdt(), 2);
		g1.step();
		assertEquals(g1.getWdt(), 3);
		g1.step();
		assertEquals(g1.getWdt(), target.getWdt());
		assertEquals(g1.getHgt(), target.getHgt());
	}
	
	
	@Test	
	public void test2Step() {
		for(int i = 1; i<w-1; i++) {
			es.setNature(1, i, Cell.LAD);
		}
		es.setNature(2, 3, Cell.MTL);
		env.init(h, w, es);
		target.init(env, 2, 4);
		g1.init(0, 1, env, target,false); 
		g1.step();
		g1.step();
		g1.step();
		g1.step();
		g1.step();
		assertEquals(g1.getWdt(), target.getWdt());
		assertEquals(g1.getHgt(), target.getHgt());
	}
	
}
