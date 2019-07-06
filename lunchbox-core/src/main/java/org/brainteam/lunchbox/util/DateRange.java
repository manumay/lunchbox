package org.brainteam.lunchbox.util;

import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.joda.time.LocalDate;

public class DateRange implements Iterable<Date> {
	
    private final LocalDate start;
    private final LocalDate end;
    
    public DateRange(Date start, Date end) {
    	this.start = new LocalDate(start);
    	this.end = new LocalDate(end);
    }

    @Override
    public Iterator<Date> iterator() {
        return new LocalDateRangeIterator(start, end);
    }

    private static class LocalDateRangeIterator implements Iterator<Date> {
    	
        private LocalDate current;
        private final LocalDate end;

        private LocalDateRangeIterator(LocalDate start, LocalDate end)  {
            this.current = start;
            this.end = end;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Date next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            LocalDate ret = current;
            current = current.plusDays(1);
            if (current.compareTo(end) > 0) {
                current = null;
            }
            return ret.toDate();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}