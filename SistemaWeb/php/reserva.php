
<?php
// Insere uma nova reserva no banco

session_start();
if (!isset($_SESSION['id_funcionario'])) {
    http_response_code(401); // Não autorizado
    exit;
}

ini_set('display_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
include 'conexao.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST' && empty($_POST)) {
    parse_str(file_get_contents('php://input'), $_POST);
}

$id_funcionario = $_POST['id_funcionario'] ?? '';
$id_item = $_POST['id_item'] ?? '';
$data_reserva = $_POST['data_reserva'] ?? '';

if (empty($id_funcionario) || empty($id_item) || empty($data_reserva)) {
    echo json_encode([
        "status" => "erro",
        "erro" => "Campos obrigatórios ausentes.",
    ]);
    exit;
}

$sql = "INSERT INTO Reserva (ID_Funcionario, ID_Item, Data_Reserva, Status_Reserva)
        VALUES (?, ?, ?, 'pendente')";
$stmt = $conn->prepare($sql);
$stmt->bind_param("iis", $id_funcionario, $id_item, $data_reserva);

if ($stmt->execute()) {
    echo json_encode(["status" => "ok"]);
} else {
    echo json_encode(["status" => "erro", "erro" => $conn->error]);
}
?>
