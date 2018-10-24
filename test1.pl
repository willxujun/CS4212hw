class Main {
    Void main(Int i, Int a, Int b, Int d) {
        while (true) {
            b = 340;
            t1 = t2;
        }
    }
}

class Dummy {
    Dummy j;
    Int dummy() {
        Bool i;
        Bool j;
        return i;
    }
}

class Dummy {
    Dummy j;
    Int dummy() {
        Bool i;
        Bool j;
        return i;
    }
}

class DupField {
    Dummy i;
    Dummy i;
}

class DupFormal {
    Int dummy(Int i, Bool i) {
        return i;
    }
}

class DupMethod {
    Int dummy(Int i) {
        return i;
    }
    Int dummy(Int i) {
        return i;
    }
}

class OverloadMethod {
    Int dummy(Int i) {
        return i;
    }
    Int dummy(Int i, Int j) {
        return i;
    }
}
