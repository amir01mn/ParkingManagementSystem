package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.function.Predicate;


public class ParkingSpaceIteratorTest {

	private List<ParkingSpace> spaces;

	@BeforeEach
	void setUp() {
		spaces = Arrays.asList(new ParkingSpace(1, 5, 10),
				               new ParkingSpace(2, 6, 11),
				               new ParkingSpace(3, 7, 12));

		spaces.get(0).setStatus("Available");
		spaces.get(1).setStatus("Occupied");
		spaces.get(2).setStatus("Available");

	}

	@Test
	void testIteratorFindOccupiedSpaces() {
		ParkingSpaceIterator iterator = new ParkingSpaceIterator(spaces, new Predicate<ParkingSpace>() {

        	@Override
        	public boolean test(ParkingSpace sp) {
        		return "Occupied".equals(sp.getStatus());
        	}
        });

		assertTrue(iterator.hasNext());
		assertEquals(2, iterator.next().getSpotID());
		assertFalse(iterator.hasNext());
	}

	@Test
    void testIteratorFindAvailableSpaces() {
        ParkingSpaceIterator iterator = new ParkingSpaceIterator(spaces, new Predicate<ParkingSpace>() {

        	@Override
        	public boolean test(ParkingSpace sp) {
        		return "Available".equals(sp.getStatus());
        	}
        });

        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().getSpotID());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().getSpotID());
        assertFalse(iterator.hasNext());
    }

	//Checks to see if every spot is visited even with true predicate.
	@Test
    void testIteratorTruePredicate() {
        ParkingSpaceIterator iterator = new ParkingSpaceIterator(spaces, new Predicate<ParkingSpace>() {
            @Override
            public boolean test(ParkingSpace s) {
                return true;
            }
        });

        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

	@Test
    void testIteratorNoneFound() {
        ParkingSpaceIterator iterator = new ParkingSpaceIterator(spaces, new Predicate<ParkingSpace>() {
            @Override
            public boolean test(ParkingSpace s) {
                return "Disabled".equals(s.getStatus());
            }
        });

        assertFalse(iterator.hasNext());
    }

	@Test
    void testIteratorAcceptOnlyOneSpecific() {
        ParkingSpaceIterator iterator = new ParkingSpaceIterator(spaces, new Predicate<ParkingSpace>() {
            @Override
            public boolean test(ParkingSpace s) {
                return s.getSensorID() == 10;
            }
        });

        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().getSpotID());
        assertFalse(iterator.hasNext());
    }

	//Checks when predicate is always false.
	@Test
    void testIteratorFalsePredicate() {
        ParkingSpaceIterator iterator = new ParkingSpaceIterator(spaces, new Predicate<ParkingSpace>() {
            @Override
            public boolean test(ParkingSpace s) {
                return false;
            }
        });

        assertFalse(iterator.hasNext());
    }

	@Test
    void testIteratorEmpty() {
        ParkingSpaceIterator iterator = new ParkingSpaceIterator(Collections.emptyList(), new Predicate<ParkingSpace>() {
            @Override
            public boolean test(ParkingSpace s) {
                return true;
            }
        });

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

	@Test
    void testNextWhenElementsRunOut() {
        ParkingSpaceIterator iterator = new ParkingSpaceIterator(spaces, new Predicate<ParkingSpace>() {
            @Override
            public boolean test(ParkingSpace s) {
                return "Disabled".equals(s.getStatus());
            }
        });

        assertThrows(NoSuchElementException.class, iterator::next);
    }

	@Test
	void testIteratorSuccessfulFind() {
        ParkingSpaceIterator iterator = new ParkingSpaceIterator(spaces, new Predicate<ParkingSpace>() {
            @Override
            public boolean test(ParkingSpace s) {
                return s.getSpotID() == 2;
            }
        });

        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().getSpotID());
        assertFalse(iterator.hasNext());
    }

	@Test
    void testFindNextSpot() {
        ParkingSpaceIterator iterator = new ParkingSpaceIterator(spaces, new Predicate<ParkingSpace>() {
            @Override
            public boolean test(ParkingSpace s) {
                return "Occupied".equals(s.getStatus());
            }
        });

        ParkingSpace occupied = iterator.next();
        assertEquals(2, occupied.getSpotID());
    }
}
