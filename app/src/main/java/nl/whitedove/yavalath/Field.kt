package nl.whitedove.yavalath

class Field(nr: Int) {
    private var nr: Int
    var fieldState: FieldState

    init {
        this.nr = nr
        this.fieldState = FieldState.Empty
    }
}

