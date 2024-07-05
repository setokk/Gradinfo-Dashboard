package edu.gradinfo.dashboard.enums;

public interface IEnum<T extends Enum<?>> {
	default T fromOrdinal(int ordinal) {
		for (T value : getValues()) {
			if (value.ordinal() == ordinal)
				return value;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default T[] getValues() {
		try {
			Class<T> enumClass = (Class<T>) this.getClass();
			return (T[]) enumClass.getMethod("values").invoke(null);
		} catch (Exception e) {
			throw new RuntimeException("Unable to get enum values", e);
		}
	}
}
