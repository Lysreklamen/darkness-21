package darkness.generator.scripts.uka17

class HorizontalLight : BaseScript() {
    override fun run() {
        super.run()

        whiteover()
        whiteover()
        whiteover()
    }

    private fun whiteover() {
        for (column in columns) {
            set(column, 255, 255, 255)
            skip(1)
        }
        for (column in columns) {
            set(column, 0, 0, 0)
            skip(2)
        }
    }
}
