<?php
// Atualiza o status de uma reserva para 'finalizada', cancelando a solicitação

session_start();
if (!isset($_SESSION['id_funcionario'])) {
    http_response_code(401); // Não autorizado
    exit;
}

include("conexao.php");

if (isset($_POST['id'])) {
    $id = intval($_POST['id']);

    // Atualiza o status para 'finalizada' apenas se ainda estiver como 'pendente'
    $query = "UPDATE reserva SET Status_Reserva = 'cancelada' WHERE ID_Reserva = ? AND Status_Reserva = 'pendente'";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("i", $id);

    if ($stmt->execute() && $stmt->affected_rows > 0) {
        echo "ok";
    } else {
        echo "erro";
    }

    $stmt->close();
}
?>