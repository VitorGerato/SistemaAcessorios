<?php
// Retorna as reservas ativas ou pendentes do funcionário logado

session_start();
if (!isset($_SESSION['id_funcionario'])) {
    http_response_code(401); // Não autorizado
    exit;
}

require_once("conexao.php");

// Obtém o ID do funcionário via GET
$id_funcionario = $_GET["id_funcionario"] ?? null;

if (!$id_funcionario) {
  // Retorna um array vazio se o ID não for informado
  echo json_encode([]);
  exit;
}

// Consulta todas as reservas feitas pelo funcionário, com dados do item
$sql = "SELECT
          r.ID_Reserva,
          r.Data_Reserva,
          i.Nome AS Nome_Item,
          i.Categoria,
          r.Status_Reserva,
          r.Observacoes,
          r.Cancelado
        FROM reserva r
        JOIN item i ON r.ID_Item = i.ID_Item
        WHERE r.ID_Funcionario = ?
        ORDER BY r.Data_Reserva DESC";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $id_funcionario);
$stmt->execute();
$result = $stmt->get_result();

// Retorna os dados como JSON
$historico = $result->fetch_all(MYSQLI_ASSOC);
echo json_encode($historico);
?>