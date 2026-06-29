<?php
// Script de conexão ao banco de dados MySQL do sistema

$host = "localhost";
$usuario = "root";
$senha = "adm6402";
$banco = "db_sistema";

$conn = new mysqli($host, $usuario, $senha, $banco);

if ($conn->connect_error) {
  die("Falha na conexão: " . $conn->connect_error);
}
?>