package darkness.generator.scripts.uka15;
import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbSet;
import darkness.generator.api.effects.EffectBase;
import java.util.Random;

/**
 * Created by morten on 26.09.2015.
 */
public class IntroTentom extends BaseScript {
    @Override
    public void run(){
        super.run();

        // phase1: neon sign start up
        // Phase2: fade to red

        BlinkyLetter LetterA = new BlinkyLetter();
        LetterA.setLetter(A);
        BlinkyLetter LetterB = new BlinkyLetter();
        LetterB.setLetter(B);
        BlinkyLetter LetterC = new BlinkyLetter();
        LetterC.setLetter(C);
        BlinkyLetter LetterD = new BlinkyLetter();
        LetterD.setLetter(D);
        BlinkyLetter LetterE = new BlinkyLetter();
        LetterE.setLetter(E);
        BlinkyLetter LetterF = new BlinkyLetter();
        LetterF.setLetter(F);
        BlinkyLetter LetterG = new BlinkyLetter();
        LetterG.setLetter(G);
        effect(LetterA);
        effect(LetterB);
        effect(LetterC);
        effect(LetterD);
        effect(LetterE);
        effect(LetterF);
        effect(LetterG);
        skip(100);
        LetterA.cancel();
        LetterB.cancel();
        LetterC.cancel();
        LetterD.cancel();
        LetterE.cancel();
        LetterF.cancel();
        LetterG.cancel();

        for (BulbGroup letter: letters){
            rgbFade(letter,255,0,0,50);
        }



        skip(100);


    }


    private class BlinkyLetter extends EffectBase {
        private BulbSet Letter;
        private boolean cancelled = false;
        private boolean timeOut = false;
        private int     time = 0;
        private int     timeOutTime = 100;
        private Random random = new Random(System.currentTimeMillis());

        @Override
        public void run(){

            while (!(cancelled || timeOut)){

                set(Letter,255,147,41);
                next();
                skip(Math.min((int) Math.exp(-random.nextInt(20) + Math.round(0.2*time)), 100));
                set(Letter,0,0,0);
                time = time + 1;

                if (time == timeOutTime){
                    timeOut = true;
                    time=0;
                }
                next();

            }
            set(Letter,255,147,41);
        }
        public void setLetter(BulbSet Letter){
            this.Letter = Letter;
        }
        @Override
        public String toString() {
            return "BlinkyLetter";
        }

        @Override
        public void cancel() {
            cancelled = true;
        }
    }
}
