<?php
// Retorna todos os itens cadastrados, com status e categorias

session_start();
if (!isset($_SESSION['id_funcionario'])) {
    http_response_code(401);
    exit;
}

include 'conexao.php';

$sql = " SELECT i.ID_Item AS id, i.Nome AS nome, i.Categoria AS categoria, i.Status_Item AS status,
            CASE
              WHEN i.Status_Item != 'ativo' THEN 0
              WHEN EXISTS (
                  SELECT 1 FROM manutencao m
                  WHERE m.ID_Item = i.ID_Item AND m.Data_Saida IS NULL
              ) THEN 0
              WHEN EXISTS (
                  SELECT 1 FROM reserva r
                  WHERE r.ID_Item = i.ID_Item AND r.Status_Reserva = 'aceita' AND r.Cancelado = 0
              ) THEN 0
              WHEN EXISTS (
                  SELECT 1 FROM emprestimo_item ei
                  JOIN emprestimo e ON ei.ID_Emprestimo = e.ID_Emprestimo
                  WHERE ei.ID_Item = i.ID_Item AND ei.Status_Devolucao = 'pendente'
              ) THEN 0
              ELSE 1
            END AS disponivel
          FROM item i ";

$result = $conn->query($sql);

$dados = [];
while ($row = $result->fetch_assoc()) {
    $row['disponivel'] = $row['disponivel'] == 1;
    $dados[] = $row;
}

echo json_encode($dados);
?>