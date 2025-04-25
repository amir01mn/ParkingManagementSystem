package com.company;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class BookingIterator implements Iterator<Booking> {

    private List<Booking> bookings;
    private Predicate<Booking> filter;
    private int currentIndex;
    private int nextIndex;  // Keeps track of next valid element

    public BookingIterator(List<Booking> bookings, Predicate<Booking> filter) {

        this.bookings = bookings;
        this.filter = filter;
        this.currentIndex = 0;
        findNext(); // Initialize nextIndex
    }

    private void findNext() {

        nextIndex = currentIndex;
        while (nextIndex < bookings.size() && !filter.test(bookings.get(nextIndex))) {
            nextIndex++;
        }
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return nextIndex < bookings.size();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Booking next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentIndex = nextIndex;
        Booking booking = bookings.get(currentIndex);
        currentIndex++;
        findNext();
        return booking;
    }
}
