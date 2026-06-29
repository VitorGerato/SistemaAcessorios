<?php
// Valida credenciais do funcionário e inicia sessão se estiver correto

session_start();
include 'conexao.php';

$re = $_POST['re'];
$senha = $_POST['senha'];

$sql = "SELECT * FROM funcionario WHERE RE = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $re);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 1) {
    $usuario = $result->fetch_assoc();

    if ($usuario['Senha'] === $senha) {
        $_SESSION['id_funcionario'] = $usuario['ID_Funcionario'];
        $_SESSION['nome'] = $usuario['Nome'];
        $_SESSION['status'] = $usuario['Status_Func'];

        echo json_encode([
            'status' => 'ok',
            'id_funcionario' => $usuario['ID_Funcionario'],
            'nome' => $usuario['Nome'],
            'status_func' => $usuario['Status_Func']
        ]);
    } else {
        echo json_encode(['status' => 'senha_incorreta']);
    }
} else {
    echo json_encode(['status' => 'usuario_nao_encontrado']);
}
?>