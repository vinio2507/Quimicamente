package aprenderbrincando.Controller;

/**
 * @author Vinicius Berto
 */
import aprenderbrincando.View.Execucao.Execucao;
import aprenderbrincando.View.BotaoFormula;
import aprenderbrincando.View.ThreadBotoes;
import aprenderbrincando.View.Inicio.Inicio;
import aprenderbrincando.Config;
import aprenderbrincando.Model.Dao.RankingDAO;
import aprenderbrincando.Model.Dao.ValoresDAO;
import aprenderbrincando.Model.Vo.RankingVO;
import aprenderbrincando.Model.Vo.Valores;
import aprenderbrincando.View.Manipuladores;
import aprenderbrincando.View.Mensagens;
import aprenderbrincando.View.MenuPausa.MenuPausa;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControllerExecucao implements ActionListener {

    private Partida partida;
    private Execucao telaExecucao;
    private Inicio telaInicial;
    private Config config;
    private static TratadoraAction ta;
    private Nivel nivel;
    private Validacao validacao;
    private ThreadBotoes tbtn;
    private boolean Status = false;

    public ControllerExecucao() {
        this.config = new Config();
        tbtn = new ThreadBotoes(null, null, null);

    }

    public void carregarInicio() {
        this.telaInicial = new Inicio();
        this.telaInicial.setVisible(true);
    }

    public void carregarDados() {
        Valores val = new Valores();
        ValoresDAO dao = new ValoresDAO();

    }

    public void iniciarPartida() {
        try {
            String nome = Mensagens.exibirDialogoEntrada(telaInicial, "Entrada", "Digite seu nome: ");
            if (nome.equals("")) {
                throw new NullPointerException();
            } else {
                Config cnf = new Config();
                carregarDados();
                carregarNivel();
                partida = new Partida(null, nivel.formulaAtual(), nome);
            }

            telaExecucao = new Execucao();
            for (Valores v : nivel.getListaAtual()) {
                telaExecucao.adicionarMenuLateral(v.getNome(), v.getFormula());
            }

            adicionarBotoes(nivel.getListaBotoes());
            tbtn.alterarLista(nivel.getListaBotoes());
            tbtn.alterarFrame(telaInicial, telaExecucao);

            if (!Status) {
                Status = true;
                tbtn.start();
            }

//            atualizar(partida);
            Config.PAUSA = false;
            telaInicial.setVisible(false);
            telaExecucao.setVisible(true);
            telaExecucao.repaint();

        } catch (NullPointerException npe) {
            Mensagens.exibirMensagem(telaInicial, "Erro!", "<html><p>Digite seu nome para iniciar o jogo!</p>"
                    + "</html>", Mensagens.MSG_INFORMACAO);
        }

    }

    public void adicionarBotoes(List<BotaoFormula> lista) {
        for (int i = 0; i < lista.size(); i++) {
            telaExecucao.getMeio().add(lista.get(i));
        }

    }

    public void encerrarPartida() {
        Config.PAUSA = true;
        telaExecucao.setVisible(false);
        nivel = null;
        partida = null;
        telaInicial.setVisible(true);
    }

    public void removerBotoes() {
        telaExecucao.getMeio().removeAll();
    }

    public void carregarNivel() {
        nivel = new Nivel(null);
        nivel.gerarAtualizarListaAtual();
        nivel.criarBotoes();
    }

    public void validarResposta(String texto) {
        validacao = new Validacao(partida, telaExecucao, nivel, this);
        validacao.validar(texto);
    }

//    @Override
//    public void atualizar(Observado ob) {
//        if (ob.getClass().getName().indexOf("Partida") > 0) {
//            telaExecucao.getTopo().getLblPontos().setText(Manipuladores.estilizarLabels("Pontos: " + String.valueOf(partida.getPontuacao())));
//            telaExecucao.getTopo().getLblFormula().setText(Manipuladores.estilizarLabels("" + partida.getFormulaAtual().getNome()));
//            telaExecucao.getTopo().getLblNivel().setText(Manipuladores.estilizarLabels("Nível: " + String.valueOf(nivel.getId())));
//            telaExecucao.getMeio().repaint();
//        } else if ((ob.getClass().getName().indexOf("Nivel") > 0)) {
//
//            encerrarPartida();
//
//        }
//    }

    public void salvar() {
        RankingVO ranking = new RankingVO();
        ranking.setNome(partida.getNomeJogador());
        ranking.setPontuacao(partida.getMaiorPontuacao());
        ranking.setNivel(nivel.getId());
        ranking.setErros(partida.getQtdErros());
        ranking.setAcertos(partida.getTotalAcertos());
        RankingDAO dao = new RankingDAO();
        dao.salvar(ranking);
    }

    public void setTa(TratadoraAction ta) {
        this.ta = ta;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Execucao getTelaExecucao() {
        return telaExecucao;
    }

    public Inicio getTelaInicial() {
        return telaInicial;
    }

    public static TratadoraAction getTa() {
        return ta;
    }

    public Partida getPartida() {
        return partida;
    }

    public ThreadBotoes getTbtn() {
        return tbtn;
    }

    public void sair() {
        if (telaExecucao.isVisible() || !telaInicial.isVisible()) {
            Config.PAUSA = true;
            int r = MenuPausa.exibirMenuPausa(telaExecucao);
            if (r == MenuPausa.CANCELAR) {
                Config.PAUSA = false;
            } else if (r == MenuPausa.ENCERRAR) {
                int resp = Mensagens.exibirDialogo(telaExecucao, "Voltar ao menu inicial ", "Deseja salvar a sua pontuação atual?", Mensagens.BTN_SIM_NAO_CANCELAR);
                if (resp == Mensagens.BTN_SIM) {
                    salvar();
                    encerrarPartida();
                } else if (resp == Mensagens.BTN_NAO) {
                    encerrarPartida();
                } else {
                    Config.PAUSA = false;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        
        
    }

}
