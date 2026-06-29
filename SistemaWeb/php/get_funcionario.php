<?php
// Retorna os dados do funcionário logado com base na sessão atual

session_start();
if (!isset($_SESSION['id_funcionario'])) {
    http_response_code(401); // Não autorizado
    exit;
}

require_once 'conexao.php';

if (!isset($_GET['id'])) {
    echo json_encode(["erro" => "ID do funcionário não fornecido."]);
    exit;
}

$id = intval($_GET['id']);

// Busca os dados do funcionário
$sql = "SELECT Nome AS nome, RE AS re, Status_Func AS status_func FROM Funcionario WHERE ID_Funcionario = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $id);
$stmt->execute();

$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    echo json_encode($row);
} else {
    echo json_encode(["erro" => "Funcionário não encontrado."]);
}

$stmt->close();
$conn->close();
?>