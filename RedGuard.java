import java.util.ArrayList;

public class RedGuard extends Guard {

    public RedGuard() {
        super(5, 12, new String[]{ "/img/redGuardLeft.png",
                                      "/img/redGuardRight.png",
                                      "/img/redGuardUp.png",
                                      "/img/redGuardDown.png" });

    }

    @Override
    public void spawnGuard(ArrayList<GameElement> wallArrayList) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'spawnGuard'");
    }

  
}
