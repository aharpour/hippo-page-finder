package nl.anwb.online.tfinder.utils;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.comparators.ComparableComparator;

public class ReplacementUtil {

	@SuppressWarnings("unchecked")
	private final SortedSet<ReplacemnetItem> items = new TreeSet<ReplacemnetItem>(new ComparableComparator());
	private final String originalString;

	public ReplacementUtil(String originalString) {
		this.originalString = originalString;
	}

	public void addReplacemnetItem(ReplacemnetItem replacemnetItem) {
		items.add(replacemnetItem);
	}

	public void addAllReplacemnetItems(List<ReplacemnetItem> replacemnetItems) {
		items.addAll(replacemnetItems);
	}

	public String replace() {
		StringBuilder sb = new StringBuilder(originalString);
		for (Iterator<ReplacemnetItem> iterator = items.iterator(); iterator.hasNext();) {
			ReplacemnetItem item = iterator.next();
			sb.replace(item.getStart(), item.getEnd(), item.getReplacement());
		}
		return sb.toString();
	}

	public static class ReplacemnetItem implements Comparable<ReplacemnetItem> {

		private final int start;
		private final int end;
		private final String replacement;

		public ReplacemnetItem(int start, int end, String replacement) {
			if (end < start) {
				throw new IllegalArgumentException("end should be bigger than start.");
			}
			if (replacement == null) {
				throw new IllegalArgumentException("replacement is required.");
			}
			this.start = start;
			this.end = end;
			this.replacement = replacement;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}

		public String getReplacement() {
			return replacement;
		}

		@Override
		public int compareTo(ReplacemnetItem o) {
			return o.start - this.start;
		}

	}

}
