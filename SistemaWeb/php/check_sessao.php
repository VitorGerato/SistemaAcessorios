<?php
// Verifica se há uma sessão de funcionário ativa

session_start();
if (!isset($_SESSION['id_funcionario'])) {
    http_response_code(401); // Não autenticado
} else {
    http_response_code(200); // OK
}
?>