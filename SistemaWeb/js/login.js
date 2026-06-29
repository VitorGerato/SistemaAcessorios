// Script responsável por validar e enviar dados de login para o servidor

document.getElementById("acessar").addEventListener("click", function() {
  const re = document.getElementById("registro").value;
  const senha = document.getElementById("senha").value;

  fetch("php/login.php", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    body: `re=${encodeURIComponent(re)}&senha=${encodeURIComponent(senha)}`
  })
  .then(res => res.json())
  .then(data => {
    if (data.status === "ok") {
      localStorage.clear(); 
      localStorage.setItem("id_funcionario", data.id_funcionario);
      localStorage.setItem("nome", data.nome);
      localStorage.setItem("re", re);
      localStorage.setItem("status_func", data.status_func);
      window.location.href = "perfil.html";
    } else if (data.status === "senha_incorreta") {
      document.getElementById("erro").textContent = "Senha incorreta.";
    } else {
      document.getElementById("erro").textContent = "Usuário não encontrado.";
    }
  })
  .catch(() => {
    document.getElementById("erro").textContent = "Erro de conexão.";
  });
});