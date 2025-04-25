package com.company;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class ParkingSpaceIterator implements Iterator<ParkingSpace> {
    private List<ParkingSpace> spaces;
    private Predicate<ParkingSpace> filter;
    private int currentIndex = 0;

    public ParkingSpaceIterator(List<ParkingSpace> spaces, Predicate<ParkingSpace> filter) {
        this.spaces = spaces;
        this.filter = filter;
        this.currentIndex = findNextIndex(0);  // Initialize current index to the first valid element
    }

    // find the next index that satisfies the filter condition
    private int findNextIndex(int start) {
        for (int i = start; i < spaces.size(); i++) {
            if (filter.test(spaces.get(i))) {
                return i;
            }
        }
        return -1;  
    }

    @Override
    public boolean hasNext() {
        return currentIndex != -1;
    }

    @Override
    public ParkingSpace next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more parking spaces available under current filter criteria.");
        }
        ParkingSpace currentSpace = spaces.get(currentIndex);
        currentIndex = findNextIndex(currentIndex + 1);  // Move to the next valid index
        return currentSpace;
    }
}
