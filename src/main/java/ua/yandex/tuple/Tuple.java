package ua.yandex.tuple;

public class Tuple<T1, T2> {
    private T1 firstObject;
    private T2 secondObject;

    public Tuple(T1 firstObject, T2 secondObject) {
        this.firstObject = firstObject;
        this.secondObject = secondObject;
    }

    public T1 getFirstObject() {
        return firstObject;
    }

    public void setFirstObject(T1 newFirstObject) {
        this.firstObject = newFirstObject;
    }

    public T2 getSecondObject() {
        return secondObject;
    }

    public void setSecondObject(T2 newSecondObject) {
        this.secondObject = newSecondObject;
    }

    @Override
    public String toString() {
        return "(" + firstObject.toString()
                + ", " + secondObject.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        return this == o
                || (o instanceof Tuple
                && ((Tuple) o).firstObject.equals(firstObject)
                && ((Tuple) o).secondObject.equals(secondObject));

    }
}
