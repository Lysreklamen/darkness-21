package darkness.generator.scripts.uka17

class CountUp : BaseScript() {
    override fun run() {
        super.run()
        for (i in 0..99) {
            setCounter(i, true)
            skip(10)
        }
    }
}
