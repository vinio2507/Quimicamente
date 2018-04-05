package aprenderbrincando;

import aprenderbrincando.Controller.ControllerExecucao;
import aprenderbrincando.Controller.TratadoraAction;
import aprenderbrincando.View.Splash;

public class AprenderBrincando {

    public static void main(String[] args) {
        ControllerExecucao ctrl = new ControllerExecucao();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Splash s = new Splash();
                s.setVisible(false);
                s = null;
                ctrl.setTa(new TratadoraAction(ctrl));
                ctrl.carregarInicio();
            }
        });

    }
}