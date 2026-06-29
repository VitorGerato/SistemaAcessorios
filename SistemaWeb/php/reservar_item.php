<?php
session_start();
require_once 'conexao.php';

if (!isset($_POST['id_item']) || !isset($_POST['id_funcionario'])) {
    echo json_encode(["status" => "erro", "mensagem" => "Dados incompletos."]);
    exit;
}

$idItem = intval($_POST['id_item']);
$idFuncionario = intval($_POST['id_funcionario']);
$dataReserva = date('Y-m-d');

// Verifica se o status do funcionário é negativo (proteção extra no backend)
$stmt = $conn->prepare("SELECT Status_Func FROM funcionario WHERE ID_Funcionario = ?");
$stmt->bind_param("i", $idFuncionario);
$stmt->execute();
$result = $stmt->get_result();
$row = $result->fetch_assoc();

if ($row && strtolower($row['Status_Func']) === 'negativo') {
    echo json_encode(["status" => "erro", "mensagem" => "Funcionário com status negativo."]);
    exit;
}

// Insere a reserva
$stmt = $conn->prepare("INSERT INTO reserva (ID_Funcionario, ID_Item, Data_Reserva, Status_Reserva) VALUES (?, ?, ?, 'pendente')");
$stmt->bind_param("iis", $idFuncionario, $idItem, $dataReserva);

if ($stmt->execute()) {
    echo json_encode(["status" => "ok"]);
} else {
    echo json_encode(["status" => "erro", "mensagem" => "Erro ao registrar a reserva."]);
}

$stmt->close();
$conn->close();
?>