import java.util.ArrayList;

public class PurpleGuard extends Guard {

    public PurpleGuard() {
        super(16, 12, new String[]{ "/img/purpleGuardLeft.png",
                                      "/img/purpleGuardRight.png",
                                      "/img/purpleGuardUp.png",
                                      "/img/purpleGuardDown.png" });

    }

    @Override
    public void spawnGuard(ArrayList<GameElement> wallArrayList) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'spawnGuard'");
    }

}
