package model.bean;

public class EmprestimoItem {
    private int idEmprestimoItem;
    private int idEmprestimo;
    private int idItem;
    private String statusDevolucao;
    private String observacoes;

    public int getIdEmprestimoItem() {
        return idEmprestimoItem;
    }

    public void setIdEmprestimoItem(int idEmprestimoItem) {
        this.idEmprestimoItem = idEmprestimoItem;
    }

    public int getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(int idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getStatusDevolucao() {
        return statusDevolucao;
    }

    public void setStatusDevolucao(String statusDevolucao) {
        this.statusDevolucao = statusDevolucao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
