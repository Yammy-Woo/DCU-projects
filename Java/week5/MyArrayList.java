interface MyList<E> {
    void add(E e);
    void add(E e, int pos);
    E remove();
    E remove(int pos);
    void clear();
    int size();
    int contains(E e);
}

class MyArrayList<E> implements MyList<E> {
    private E[] array = (E[])new Object[100]; // DANGEROUS!
    private int size = 0;

    public void add(E e) {
        this.array[size] = e;
        size += 1;
    }

    public String toString() {
        String output = "[";
        for(int i=0; i<size; i++) {
            output += array[i] + ",";
        }
        return output + "]";
    }

    // TODO: add
    public void add(E e, int pos) {
        for (int i = size - 1; i >= pos; i--) {
            array[i + 1] = array[i];
        }
        this.array[pos] = e;
        size += 1;
    }

    // TODO: remove from end
    public E remove() {
        size -= 1;
        return array[size + 1];
    }

    // TODO: remove at position
    public E remove(int pos) {
        E e = array[pos];
        for (int i = pos; i < size; i++) {
            array[i] = array[i + 1];
        }
        size -= 1;
        return e;
    }

    // TODO: clear
    public void clear() {
        size = 0;
    }

    // TODO: size
    public int size() {
        return size;
    }

    // TODO: contains
    public int contains(E e) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String args[]) {
        MyArrayList<Integer> numList = new MyArrayList<>();
        for(int i=0; i<10; i++) { numList.add(i); }
        System.out.println(numList);
        // output: [0,1,2,3,4,5,6,7,8,9,]
        numList.add(4, 5);
        System.out.println(numList);
        System.out.println(numList.size());
        numList.remove(5);
        System.out.println(numList);
        System.out.println(numList.contains(7));
    }
}