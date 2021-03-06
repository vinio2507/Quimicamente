package aprenderbrincando.View;

/**
 * @author Vinicius Berto
 */

import javax.swing.JButton;
import static aprenderbrincando.Config.*;

public class BotaoTransparente extends JButton {

    public BotaoTransparente(String string) {
        super(string);
        alterarPropriedades(string);
    }

    private void alterarPropriedades(String string) {
        setText("");
        setName(string);
        setContentAreaFilled(false);
        setBounds(0, 0, convertTamanho(30, 1).width, convertTamanho(1, 17).height);
    }

}
