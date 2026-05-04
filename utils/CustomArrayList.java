package utils;

public class CustomArrayList<T> {

	private T[] array;
	private int size;

	@SuppressWarnings("unchecked")
	public CustomArrayList() {
		this.array = (T[]) new Object[8];
		this.size = 0;
	}

	public boolean add(T t) {
		if (size < array.length) {
			array[size] = t;
			size++;
			return true;
		}
		increaseArray();
		array[size] = t;
		size++;
		return true;
	}

	public boolean add(int index, T t) {
		if (index < 0 || index > size) {
			return false;
		}
		if (size < this.array.length) {
			shiftArray(index);
			this.array[index] = t;
			size++;
			return true;
		}
		if (size >= this.array.length) {
			increaseAndShift(index);
			this.array[index] = t;
			size++;
			return true;
		}
		return true;
	}

	public boolean set(int index, T t) {
		if (index < 0 || index >= size) {
			return false;
		}
		array[index] = t;
		return true;
	}

	public boolean delete(int index) {
		if (index < 0 || index >= size) {
			return false;
		}
		reverseShift(index);
		size--;
		return true;
	}

	public boolean deleteFirst(T t) {
		for (int i = 0; i < size; i++) {
			if ((t == null && array[i] == null) || (t != null && array[i] != null && array[i].equals(t))) {
				delete(i);
				return true;
			}
		}
		return false;
	}

	public boolean deleteAll(T t) {
		int counter = 0;
		for (int i = 0; i < size; i++) {
			if ((t == null && array[i] == null) || (t != null && array[i] != null && array[i].equals(t))) {
				delete(i);
				counter++;
				i--;
			}
		}
		if (counter > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public CustomArrayList<T> clear() {
		this.array = (T[]) new Object[array.length];
		this.size = 0;
		return this;
	}

	public T get(int index) {
		if (index >= 0 && index < size) {
			return array[index];
		}
		throw new IndexOutOfBoundsException("Poterjalosja");
	}

	public boolean contains(T t) {
		for (int i = 0; i < size; i++) {
			if ((t == null && array[i] == null) || (t != null && array[i] != null && array[i].equals(t))) {
				return true;
			}
		}
		return false;
	}

	public int indexOf(T t) {

		for (int i = 0; i < size; i++) {
			if ((t == null && array[i] == null) || (t != null && array[i] != null && array[i].equals(t))) {
				return i;
			}
		}
		return -1;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < size; i++) {
			sb.append(array[i]);
			if (i < size - 1)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	private void increaseArray() {

		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new Object[this.array.length * 2];

		for (int i = 0; i < size; i++) {
			newArray[i] = this.array[i];
		}
		this.array = newArray;
	}

	private void shiftArray(int index) {
		for (int i = size - 1; i >= index; i--) {
			this.array[i + 1] = this.array[i];
		}
	}

	@SuppressWarnings("unchecked")
	private void increaseAndShift(int index) {

		T[] newArray = (T[]) new Object[array.length * 2];
		for (int i = 0; i < index; i++) {
			newArray[i] = this.array[i];
		}
		for (int k = index + 1; k < size + 1; k++) {
			newArray[k] = this.array[k - 1];
		}
		this.array = newArray;
	}

	private void reverseShift(int index) {
		for (int i = index; i < size - 1; i++) {
			array[i] = array[i + 1];
		}
		array[size - 1] = null;
	}
}
