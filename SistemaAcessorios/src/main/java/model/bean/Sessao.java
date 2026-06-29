package model.bean;

/**
 * Classe para manter dados de sessão do adm logado.
 */
public class Sessao {
    private static Administrador administrador;

    public static void setAdministrador(Administrador adm) {
        Sessao.administrador = adm;
    }

    public static Administrador getAdministrador() {
        return administrador;
    }

    public static void limpar() {
        Sessao.administrador = null;
    }
}